package com.quora.app.service.impl;

import com.quora.app.dto.FeedEntry;
import com.quora.app.dto.FeedItemDTO;
import com.quora.app.entity.Answer;
import com.quora.app.entity.Question;
import com.quora.app.enumaration.TargetType;
import com.quora.app.repository.AnswerRepository;
import com.quora.app.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class FeedService {

    private final FeedItemMapperRegistry registry;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

    public Mono<List<FeedItemDTO>> buildFeed(List<FeedEntry> entries) {

        List<String> questionIds = entries.stream()
                .filter(e -> e.getType() == TargetType.QUESTION)
                .map(FeedEntry::getPostId).toList();

        List<String> answerIds = entries.stream()
                .filter(e -> e.getType() == TargetType.ANSWER)
                .map(FeedEntry::getPostId).toList();

        Mono<Map<String, Question>> questionMapMono =
                questionRepository.findAllById(questionIds)
                        .collectMap(Question::getId, q -> q);

        Mono<Map<String, Answer>> answerMapMono =
                answerRepository.findAllById(answerIds)
                        .collectMap(Answer::getId, a -> a);

        return Mono.zip(questionMapMono, answerMapMono)
                .map(tuple -> {
                    Map<String, Question> questionMap = tuple.getT1();
                    Map<String, Answer> answerMap = tuple.getT2();

                    return entries.stream()
                            .map(e -> {
                                if (e.getType() == TargetType.QUESTION) {
                                    Question q = questionMap.get(e.getPostId());
                                    return registry.get(TargetType.QUESTION)
                                            .toFeedItemDTO(q);
                                } else {
                                    Answer a = answerMap.get(e.getPostId());
                                    return registry.get(TargetType.ANSWER)
                                            .toFeedItemDTO(a);
                                }
                            })
                            .toList();
                });
    }


}
