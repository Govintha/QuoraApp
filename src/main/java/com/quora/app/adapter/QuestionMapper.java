package com.quora.app.adapter;

import com.quora.app.dto.QuestionRequestDTO;
import com.quora.app.dto.QuestionResponseDTO;
import com.quora.app.entity.Question;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;

@Slf4j
public final class QuestionMapper {


       public static final Question toEntity(QuestionRequestDTO questionRequestDTO){
           log.info("Question Request DTO {}",questionRequestDTO);
            return Question.builder()
                    .title(questionRequestDTO.getTitle())
                    .content(questionRequestDTO.getContent())
                    .tags(questionRequestDTO.getTags())
                    .userId(questionRequestDTO.getUserId())
                    .createdAt(Instant.now())
                    .updatedAt(Instant.now())
                    .build();
        }

        public static final QuestionResponseDTO toResponseDTO(Question question){
           log.info("Question Entity {}",question);
           return QuestionResponseDTO.builder()
                   .id(question.getId())
                   .title(question.getTitle())
                   .content(question.getContent())
                   .createdAt(question.getCreatedAt())
                   .tags(question.getTags())
                   .userId(question.getUserId())
                   .viewCount(question.getViews())
                   .build();
        }
}
