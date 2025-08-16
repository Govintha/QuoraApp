package com.quora.app.service;

import com.quora.app.dto.LikeRequestDTO;
import com.quora.app.dto.LikeResponseDTO;
import org.springframework.data.mongodb.core.MongoAction;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ILikeService {

    Mono<LikeResponseDTO> createLike(LikeRequestDTO likeRequestDTO);
    Mono<Integer> countLikesByTargetIdAndTargetType(String targetId,String targetType);
    Mono<Integer> countDisLikesByTargetIdAndTargetType(String targetId,String targetType);
    Mono<LikeResponseDTO> toggleLikes(String targetId,String targetType,boolean isLike);
}
