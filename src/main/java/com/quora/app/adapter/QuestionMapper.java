package com.quora.app.adapter;

import com.quora.app.dto.QuestionRequestDTO;
import com.quora.app.dto.QuestionResponseDTO;
import com.quora.app.entity.Question;

import java.time.Instant;

public final class QuestionMapper {


       public static final Question toEntity(QuestionRequestDTO questionRequestDTO){
            return Question.builder()
                    .title(questionRequestDTO.getTitle())
                    .content(questionRequestDTO.getContent())
                    .createdAt(Instant.now())
                    .updatedAt(Instant.now())
                    .build();
        }

        public static final QuestionResponseDTO toResponseDTO(Question question){

           return QuestionResponseDTO.builder()
                   .id(question.getId())
                   .title(question.getTitle())
                   .content(question.getContent())
                   .createdAt(question.getCreatedAt())
                   .build();
        }
}
