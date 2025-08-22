package com.quora.app.adapter;

import com.quora.app.dto.FeedItemDTO;
import com.quora.app.entity.Answer;
import com.quora.app.entity.Question;
import com.quora.app.enumaration.TargetType;

public final class FeedItemsMapper {

    public static  FeedItemDTO toFeedItemDTO(Question q) {
        return FeedItemDTO.builder()
                .type(TargetType.QUESTION)
                .id(q.getId())
                .content(q.getContent())
                .authorId(q.getUserId())
                .questionId(q.getId())
                .createdAt(q.getCreatedAt())
                .build();
    }

    public static FeedItemDTO toFeedItemDTO(Answer a) {
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
