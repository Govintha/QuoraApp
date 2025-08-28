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

        // Get entries newer → older (reverse order)
        Set<FeedEntry> raw = redisTemplate.opsForZSet()
                .reverseRangeByScore(key, 0, cursorScore-1, 0, pageSize);

        List<FeedEntry> pageEntries = new ArrayList<>(raw);

//        //  If cache empty → fallback to DB
//        if (pageEntries.isEmpty()) {
//            return feedService.fetchFromDB(userId, cursor, pageSize) // implement in your service
//                    .map(feedList -> buildResponse(userId, feedList));
//        }

        return feedService.buildFeed(pageEntries)
                .map(feedList -> buildResponse(userId, feedList));
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
