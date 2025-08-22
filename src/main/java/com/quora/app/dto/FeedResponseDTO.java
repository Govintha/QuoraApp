package com.quora.app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FeedResponseDTO {
    private Integer userId;
    private List<FeedItemDTO> feed;
    private String nextCursor;
}
