package com.quora.app.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class AnswerResponseDTO {

    private String id;
    private String content;
    private String title;
    private Instant createdAt;
}
