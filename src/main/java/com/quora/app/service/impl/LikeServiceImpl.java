package com.quora.app.service.impl;

import com.quora.app.dto.LikeRequestDTO;
import com.quora.app.dto.LikeResponseDTO;
import com.quora.app.service.ILikeService;
import reactor.core.publisher.Mono;

public class LikeServiceImpl implements ILikeService {

    @Override
    public Mono<LikeResponseDTO> createLike(LikeRequestDTO likeRequestDTO) {
        return null;
    }

    @Override
    public Mono<Integer> countLikesByTargetIdAndTargetType(String targetId, String targetType) {
        return null;
    }

    @Override
    public Mono<Integer> countDisLikesByTargetIdAndTargetType(String targetId, String targetType) {
        return null;
    }

    @Override
    public Mono<LikeResponseDTO> toggleLikes(String targetId, String targetType, boolean isLike) {
        return null;
    }
}
