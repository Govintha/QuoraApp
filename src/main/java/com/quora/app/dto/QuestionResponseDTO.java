package com.quora.app.dto;

import com.quora.app.entity.Tag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionResponseDTO {

    private String id;
    private String content;
    private String title;
    private Integer userId;
    private Integer viewCount;
    private Instant  createdAt;
    private List<Tag> tags=new ArrayList<>();
}
