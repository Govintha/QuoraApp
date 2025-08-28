package com.quora.app.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.quora.app.enumaration.TargetType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedEntry{
    private TargetType type;
    private String postId;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Instant createdAt;
}
