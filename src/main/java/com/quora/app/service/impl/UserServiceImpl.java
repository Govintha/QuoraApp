package com.quora.app.service.impl;

import com.quora.app.adapter.FeedItemsMapper;
import com.quora.app.adapter.UserMapper;
import com.quora.app.dto.FeedItemDTO;
import com.quora.app.dto.FeedResponseDTO;
import com.quora.app.dto.NewUserRequestDTO;
import com.quora.app.dto.NewUserResponseDTO;
import com.quora.app.entity.Tag;
import com.quora.app.entity.User;
import com.quora.app.repository.AnswerRepository;
import com.quora.app.repository.QuestionRepository;
import com.quora.app.repository.UserRepository;
import com.quora.app.service.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.Comparator;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    private final UserRepository userRepository;
    private final SequenceGeneratorService sequenceGeneratorService;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

    @Override
    public Mono<NewUserResponseDTO> createUser(NewUserRequestDTO newUserRequestDTO) {
        User user = UserMapper.toEntity(newUserRequestDTO);
        return sequenceGeneratorService.getNextSequence("userId")
                .flatMap(seq -> {
                    user.setUserId(seq);
                    return userRepository.save(user);
                })
                .map(UserMapper::toNewUserResponse)
                .doOnSuccess(response->log.info("Successfully created"))
                .doOnError(error-> log.info("Unable to create User"));
    }

    @Override
    public Mono<NewUserResponseDTO> getUser(Integer userId) {
        return userRepository.findByUserId(userId)
                .switchIfEmpty(Mono.error(new RuntimeException("UserNotFound")))
                .doOnSuccess(response -> log.info("Able to fetch user"))
                .doOnError(error -> log.error("Something went wrong {}", error.getMessage()))
                .map(UserMapper::toNewUserResponse);
    }

    @Override
    public Mono<String> addUserAsFollower(Integer userId, Integer followingId) {

        Mono<User> userMono = userRepository.findByUserId(userId)
                .switchIfEmpty(Mono.error(new RuntimeException("User not found: " + userId)));

        Mono<User> followerMono = userRepository.findByUserId(followingId)
                .switchIfEmpty(Mono.error(new RuntimeException("Follower not found: " + followingId)));

        return Mono.zip(userMono, followerMono)
                .flatMap(tuple -> {
                    User user = tuple.getT1();
                    User follower = tuple.getT2();

                    if (user.getFollowingIds().contains(followingId)) {
                        return Mono.error(new RuntimeException("User already followed"));
                    }

                    user.getFollowingIds().add(followingId);
                    follower.getFollowerIds().add(userId);

                    return userRepository.save(user)
                            .then(userRepository.save(follower))
                            .thenReturn("User followed successfully");
                });
    }

    @Override
    public Mono<FeedResponseDTO> getUserFeed(Integer userId, String cursor, int size) {
        log.info("Getting From DB");
        Instant cursorInstant = (cursor == null || cursor.isEmpty())
                ? Instant.now()
                : Instant.parse(cursor);

        return userRepository.findByUserId(userId)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("User Not Found")))
                .map(User::getFollowingIds)
                .flatMapMany(followingIds -> Flux.merge(
                        questionRepository.findByUserIdInAndCreatedAtLessThanOrderByCreatedAtDesc(
                                followingIds, cursorInstant, PageRequest.of(0, size)
                        ).map(FeedItemsMapper::toFeedItemDTO),

                        answerRepository.findByUserIdInAndCreatedAtLessThanOrderByCreatedAtDesc(
                                followingIds, cursorInstant, PageRequest.of(0, size)
                        ).map(FeedItemsMapper::toFeedItemDTO)
                ))
                .sort(Comparator.comparing(FeedItemDTO::getCreatedAt).reversed())
                .take(size)
                .collectList()
                .map(feedList -> {
                    FeedResponseDTO response = new FeedResponseDTO();
                    response.setUserId(userId);
                    response.setFeed(feedList);

                    if (!feedList.isEmpty()) {
                        FeedItemDTO last = feedList.get(feedList.size() - 1);
                        response.setNextCursor(last.getCreatedAt().toString());
                    }
                    return response;
                });
    }




    @Override
    public Mono<FeedResponseDTO> getUserFeedByTag(Integer userId, String cursor, int pageSize) {

        Instant cursorInstant = (cursor == null || cursor.isEmpty())
                ? Instant.now()
                : Instant.parse(cursor);

        return userRepository.findByUserId(userId)
                .switchIfEmpty(Mono.error(new RuntimeException("User Not Found")))
                .map(User::getTags)
                .map(tags -> tags.stream().map(Tag::getTag).toList())
                .flatMapMany(tags -> Flux.merge(
                                        questionRepository.findByTags_TagInAndCreatedAtBeforeOrderByCreatedAtDesc(
                                                tags, cursorInstant, PageRequest.of(0, pageSize)
                                        ).map(FeedItemsMapper::toFeedItemDTO),
                                        answerRepository.findByTags_TagInAndCreatedAtBeforeOrderByCreatedAtDesc(
                                                tags, cursorInstant, PageRequest.of(0, pageSize)
                                        ).map(FeedItemsMapper::toFeedItemDTO)
                                )
                                .sort(Comparator.comparing(FeedItemDTO::getCreatedAt).reversed())
                                .take(pageSize)
                )
                .collectList()
                .map(feedList -> {
                    FeedResponseDTO response = new FeedResponseDTO();
                    response.setUserId(userId);
                    response.setFeed(feedList);

                    if(!feedList.isEmpty()){
                         FeedItemDTO last=feedList.get(feedList.size()-1);
                         response.setNextCursor(last.getCreatedAt().toString());
                    }
                    return response;
                });
    }



}
