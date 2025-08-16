package com.quora.app.dto;

import com.quora.app.enumaration.TargetType;

import java.time.Instant;

public class LikeResponseDTO {

    private String id;
    private String targetID;
    private TargetType targetType;
    private boolean isLike;
    private Instant createdAt;
}
