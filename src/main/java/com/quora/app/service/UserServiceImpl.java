package com.quora.app.service;

import com.quora.app.adapter.UserMapper;
import com.quora.app.dto.NewUserRequestDTO;
import com.quora.app.dto.NewUserResponseDTO;
import com.quora.app.entity.User;
import com.quora.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService{

    private final UserRepository userRepository;
    private final SequenceGeneratorService sequenceGeneratorService;

    @Override
    public Mono<NewUserResponseDTO> createUser(NewUserRequestDTO newUserRequestDTO) {
        User user = UserMapper.toEntity(newUserRequestDTO);
        return sequenceGeneratorService.getNextSequence("userId")
                .flatMap(seq -> {
                    user.setUserId(seq);
                    return userRepository.save(user);
                })
                .map(UserMapper::toNewUserResponse)
                .doOnSuccess((response)->log.info("Successfully created"))
                .doOnError((error)-> log.info("Unable to create User"));
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
    public Mono<String> addUserAsFollower(Integer userId, Integer followerUserId) {

        Mono<User> userMono = userRepository.findByUserId(userId)
                .switchIfEmpty(Mono.error(new RuntimeException("User not found: " + userId)));

        Mono<User> followerMono = userRepository.findByUserId(followerUserId)
                .switchIfEmpty(Mono.error(new RuntimeException("Follower not found: " + followerUserId)));

        return Mono.zip(userMono, followerMono)
                .flatMap(tuple -> {
                    User user = tuple.getT1();
                    User follower = tuple.getT2();

                    if (user.getFollowerIds().contains(followerUserId)) {
                        return Mono.error(new RuntimeException("User already followed"));
                    }

                    user.getFollowerIds().add(followerUserId);
                    follower.getFollowingIds().add(userId);

                    return userRepository.save(user)
                            .then(userRepository.save(follower))
                            .thenReturn("User followed successfully");
                });
    }



}
