package com.quora.app.events;

import com.quora.app.enumaration.TargetType;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ViewCountEvent {

    private String targetId;
    private TargetType targetType;
    private Instant viewedAt;
}
