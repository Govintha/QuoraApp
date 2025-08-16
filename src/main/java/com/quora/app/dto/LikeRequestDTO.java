package com.quora.app.dto;

import com.quora.app.enumaration.TargetType;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LikeRequestDTO {

    @NotBlank(message = "Target ID cannot be null or Empty")
    private String targetID;

    @NotBlank(message = "targetType is required")
    private TargetType targetType;

    @NotBlank(message = "Like cannot be null or empty")
    private boolean isLike;

    @AssertTrue(message = "targetType cannot be QUESTION or ANSWER")
    public boolean isValidTargetType() {
        return targetType != TargetType.QUESTION && targetType != TargetType.ANSWER;
    }
}
