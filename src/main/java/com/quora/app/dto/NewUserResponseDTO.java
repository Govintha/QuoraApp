package com.quora.app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewUserResponseDTO {

    private Integer userId;
    private String userName;
    private Instant createdAt;
    private List<Integer> followerId;
    private List<Integer> followingId;

}
