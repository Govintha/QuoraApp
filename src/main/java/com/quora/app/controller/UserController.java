package com.quora.app.controller;

import com.quora.app.dto.FeedResponseDTO;
import com.quora.app.dto.NewUserRequestDTO;
import com.quora.app.dto.NewUserResponseDTO;
import com.quora.app.service.IUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/user")
@Slf4j
@Validated
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;

    @PostMapping
    public Mono<NewUserResponseDTO> createUser(@RequestBody @Valid NewUserRequestDTO newUserRequestDTO){

        return userService.createUser(newUserRequestDTO)
                .doOnSuccess((response)->log.info("Successfully created"))
                .doOnError((error)-> log.info("Unable to create User"));
    }

    @GetMapping("/{userId}")
    public Mono<NewUserResponseDTO> getUser(@PathVariable  Integer userId){
        return userService.getUser(userId)
                .doOnSuccess(response -> log.info("Able to fetch user"))
                .doOnError(error -> log.error("Something went wrong {}", error.getMessage()));
    }

    @PostMapping("/{userId}/follow/{followingId}")
    public Mono<ResponseEntity<String>> addUserAsFollowers(@PathVariable Integer userId,
                                                           @PathVariable Integer followingId) {

        if (userId.equals(followingId)) {
            return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "User cannot follow themselves"));
        }

        return userService.addUserAsFollower(userId, followingId)
                .map(msg -> ResponseEntity.ok(msg))
                .doOnSuccess(response -> log.info("User followed"))
                .doOnError(error -> log.error("User not added due to {}", error.getMessage()));
    }

    @GetMapping("/{userId}/feed")
    public Mono<FeedResponseDTO> getUserFeed(@PathVariable  Integer userId,
                                             @RequestParam(required = false) String cursor,
                                             @RequestParam(defaultValue = "20") int pageSize){

        log.info("Cursor TIme and Size {},{}",cursor,pageSize);
         return userService.getUserFeed(userId,cursor,pageSize)
                 .doOnSuccess(response->log.info("Able get Feed"))
                 .doOnError(error->log.error("Unable to get Feed"));
    }

    @GetMapping("/{userId}/tagFeed")
    public Mono<FeedResponseDTO> getUserFeedByTag(@PathVariable  Integer userId,
                                                  @RequestParam(required = false) String cursor,
                                                  @RequestParam(defaultValue = "20") int pageSize){

        log.info("Cursor TIme and Size {},{}",cursor,pageSize);
        return userService.getUserFeedByTag(userId,cursor,pageSize)
                .doOnSuccess(response->log.info("Able get Feed"))
                .doOnError(error->log.error("Unable to get Feed"));
    }





}
