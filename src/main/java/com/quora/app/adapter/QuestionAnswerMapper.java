package com.quora.app.adapter;

import com.quora.app.dto.QuestionAnswerDTO;
import com.quora.app.entity.Answer;
import org.springframework.boot.autoconfigure.quartz.QuartzDataSource;

import java.util.ArrayList;
import java.util.List;

public final class QuestionAnswerMapper {


    public  static QuestionAnswerDTO toReponse(List<Answer> answers,String questionId){

        return  QuestionAnswerDTO.builder()
                 .questionID(questionId)
                 .lisOfAnswers(AnswerMapper.toAnswerResponseDTOList(answers))
                 .build();
    }

}
