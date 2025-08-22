package com.quora.app.dto;

import com.quora.app.entity.Tag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewUserResponseDTO {

    private Integer userId;
    private String userName;
    private Instant createdAt;
    private List<Integer> followerId=new ArrayList<>();
    private List<Integer> followingId=new ArrayList<>();
    private List<Tag> tags=new ArrayList<>();

}
