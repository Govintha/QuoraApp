package com.quora.app.service;

import com.quora.app.dto.FeedResponseDTO;
import com.quora.app.dto.NewUserRequestDTO;
import com.quora.app.dto.NewUserResponseDTO;
import reactor.core.publisher.Mono;


public interface  IUserService {
    Mono<NewUserResponseDTO> createUser(NewUserRequestDTO newUserRequestDTO);
    Mono<NewUserResponseDTO> getUser(Integer userId);
    Mono<String> addUserAsFollower(Integer userId,Integer followerUserId);
    Mono<FeedResponseDTO> getUserFeed(Integer userId,String cursor,int size);
    Mono<FeedResponseDTO> getUserFeedByTag(Integer userId,String cursor,int pageSize);
}
