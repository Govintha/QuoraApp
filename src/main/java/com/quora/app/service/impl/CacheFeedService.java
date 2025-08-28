package com.quora.app.service.impl;

import com.quora.app.dto.FeedEntry;
import com.quora.app.dto.FeedItemDTO;
import com.quora.app.dto.FeedResponseDTO;
import com.quora.app.service.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class CacheFeedService {

    private final RedisTemplate<String, FeedEntry> redisTemplate;
    private final IUserService userService;
    private final FeedService feedService;

    private String getCacheKey(Integer userId) {
        return "feed:" + userId;
    }

    //  When user creates a new post will call this method to cache
    public Mono<Void> distributePostToFollowers(Integer authorId, FeedEntry entry) {
        return userService.getUser(authorId) // fetch author
                .flatMap(user -> {
                    List<Integer> followers = user.getFollowerId();
                    if (followers == null || followers.isEmpty()) {
                        return Mono.empty();
                    }

                    long score = entry.getCreatedAt().toEpochMilli();

                    followers.forEach(followerId -> {
                        String key = getCacheKey(followerId);

                        // Add feed entry with createdAt as score
                        redisTemplate.opsForZSet().add(key, entry, score);

                        // Keep only latest 10 items (trim older ones)
                        redisTemplate.opsForZSet().removeRange(key, 0, -11);
                    });

                    return Mono.empty();
                });
    }



    public Mono<FeedResponseDTO> getUserFeed(Integer userId, String cursor, int pageSize) {
        String key = getCacheKey(userId);

        // If no cursor, start from "now"
        long cursorScore = (cursor == null || cursor.isEmpty())
                ? Long.MAX_VALUE
                : Instant.parse(cursor).toEpochMilli();

        // 1. Get from Redis
        Set<FeedEntry> raw = redisTemplate.opsForZSet()
                .reverseRangeByScore(key, 0, cursorScore - 1, 0, pageSize);

        List<FeedEntry> cacheEntries = new ArrayList<>(raw);

        int remaining = pageSize - cacheEntries.size();
        Instant newCursor = cacheEntries.isEmpty()
                ? Instant.ofEpochMilli(cursorScore)
                : cacheEntries.get(cacheEntries.size() - 1).getCreatedAt();

        // 2. Build feed from cache
        Mono<List<FeedItemDTO>> cacheFeedMono = feedService.buildFeed(cacheEntries);

        if (remaining <= 0) {
            // Enough posts in cache
            return cacheFeedMono.map(feedList -> buildResponse(userId, feedList));
        }

        // Fetch remaining from DB
        Mono<FeedResponseDTO> dbFeedMono = userService.getUserFeed(userId, newCursor.toString(), remaining);

        //  Merge cache + DB results
        return Mono.zip(cacheFeedMono, dbFeedMono)
                .map(tuple -> {
                    log.info("Getting from DB+Cache");
                    List<FeedItemDTO> merged = new ArrayList<>();
                    merged.addAll(tuple.getT1()); // cache feed
                    merged.addAll(tuple.getT2().getFeed()); // db feed

                    // Ensure correct descending order by createdAt
                    merged.sort((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()));

                    return buildResponse(userId, merged);
                });
    }




    private FeedResponseDTO buildResponse(Integer userId, List<FeedItemDTO> feedList) {
        FeedResponseDTO response = new FeedResponseDTO();
        response.setUserId(userId);
        response.setFeed(feedList);

        if (!feedList.isEmpty()) {
            FeedItemDTO last = feedList.get(feedList.size() - 1);
            response.setNextCursor(last.getCreatedAt().toString());
        }
        return response;
    }



}
