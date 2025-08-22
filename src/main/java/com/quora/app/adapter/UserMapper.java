package com.quora.app.adapter;

import com.quora.app.dto.NewUserRequestDTO;
import com.quora.app.dto.NewUserResponseDTO;
import com.quora.app.entity.User;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;

@Slf4j
public final class UserMapper {

    public static NewUserResponseDTO toNewUserResponse(User user){

       return NewUserResponseDTO.builder()
               .userId(user.getUserId())
               .followerId(user.getFollowerIds())
               .followingId(user.getFollowingIds())
               .tags(user.getTags())
               .userName(user.getUsername())
               .createdAt(user.getCreatedAt())
               .build();
    }

    public static User toEntity(NewUserRequestDTO dto) {
        return User.builder()
                .username(dto.getUserName())
                .createdAt(Instant.now())
                .tags(dto.getTags())
                .updatedAt(Instant.now())
                .build();
    }


}
