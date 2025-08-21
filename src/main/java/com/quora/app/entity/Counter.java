package com.quora.app.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "counters")
public class Counter {
    @Id
    private String id;   // e.g., "userId"
    private Integer seq;
}
