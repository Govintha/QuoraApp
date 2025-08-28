package com.quora.app.service.impl;

import com.quora.app.dto.FeedItemDTO;
import com.quora.app.entity.Question;
import com.quora.app.enumaration.TargetType;
import com.quora.app.service.FeedItemMapper;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class QuestionFeedItemMapper implements FeedItemMapper<Question> {
    @Override
    public TargetType supports() {
        return TargetType.QUESTION;
    }

    @Override
    public FeedItemDTO toFeedItemDTO(Question q) {
        return FeedItemDTO.builder()
                .type(TargetType.QUESTION)
                .id(q.getId())
                .content(q.getContent())
                .authorId(q.getUserId())
                .questionId(q.getId())
                .createdAt(q.getCreatedAt())
                .build();
    }
}
