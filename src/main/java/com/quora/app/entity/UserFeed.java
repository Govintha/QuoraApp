package com.quora.app.entity;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "user_feeds")
public class UserFeed {
    private String userId;
    private List<String> questionIds;
}
