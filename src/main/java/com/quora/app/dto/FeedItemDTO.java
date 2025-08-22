package com.quora.app.dto;

import com.quora.app.enumaration.TargetType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FeedItemDTO {
    private TargetType type;
    private String id;
    private String content;
    private Integer authorId;
    private String questionId;
    private Instant createdAt;
}
