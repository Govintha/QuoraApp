package com.quora.app.service.impl;

import com.quora.app.enumaration.TargetType;
import com.quora.app.service.FeedItemMapper;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class FeedItemMapperRegistry {

    private final Map<TargetType, FeedItemMapper<?>> registry = new HashMap<>();

    public FeedItemMapperRegistry(List<FeedItemMapper<?>> mappers) {
        for (FeedItemMapper<?> mapper : mappers) {
            registry.put(mapper.supports(), mapper);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> FeedItemMapper<T> get(TargetType type) {
        return (FeedItemMapper<T>) registry.get(type);
    }
}
