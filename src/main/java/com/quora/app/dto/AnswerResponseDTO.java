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
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnswerResponseDTO {

    private String id;
    private String content;
    private Integer userId;
    private Integer views;
    private List<Tag> tags=new ArrayList<>();
    private Instant createdAt;
}
