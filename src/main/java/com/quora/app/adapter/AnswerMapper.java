package com.quora.app.adapter;

import com.quora.app.dto.AnswerRequestDTO;
import com.quora.app.dto.AnswerResponseDTO;
import com.quora.app.entity.Answer;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class AnswerMapper {

     public static AnswerResponseDTO toAnswerResponseDTO(Answer answer){

         return  AnswerResponseDTO.builder()
                 .id(answer.getId())
                 .content(answer.getContent())
                 .userId(answer.getUserId())
                 .createdAt(Instant.now())
                 .build();

     }

     public static Answer toEntity(AnswerRequestDTO requestDTO){

        return Answer.builder()
                 .questionId(requestDTO.getQuestionId())
                 .content(requestDTO.getContent())
                 .createdAt(Instant.now())
                .userId(requestDTO.getUserId())
                 .updatedAt(Instant.now())
                 .build();
     }

     public static List<AnswerResponseDTO> toAnswerResponseDTOList(List<Answer> answers){

         return answers.stream()
                 .map(AnswerMapper::toAnswerResponseDTO)
                 .collect(Collectors.toList());
     }
}
