package com.quora.app.service.impl;

import com.quora.app.dto.FeedItemDTO;
import com.quora.app.entity.Answer;
import com.quora.app.enumaration.TargetType;
import com.quora.app.service.FeedItemMapper;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class AnswerFeedItemMapper implements FeedItemMapper<Answer> {
    @Override
    public TargetType supports() {
        return TargetType.ANSWER;
    }

    @Override
    public FeedItemDTO toFeedItemDTO(Answer a) {
        return FeedItemDTO.builder()
                .type(TargetType.ANSWER)
                .id(a.getId())
                .content(a.getContent())
                .authorId(a.getUserId())
                .questionId(a.getQuestionId())
                .createdAt(a.getCreatedAt())
                .build();
    }
}
