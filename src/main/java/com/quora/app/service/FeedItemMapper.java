package com.quora.app.service;

import com.quora.app.dto.FeedItemDTO;
import com.quora.app.enumaration.TargetType;

import java.time.Instant;

public interface FeedItemMapper<T> {
    TargetType supports();
    FeedItemDTO toFeedItemDTO(T entity);
}
