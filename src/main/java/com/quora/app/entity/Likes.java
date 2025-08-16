package com.quora.app.entity;

import com.quora.app.enumaration.TargetType;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Setter
@Getter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Document(collection = "likes")
public class Likes extends BaseEntity{


    private String targetId;

    private TargetType targetType;

    private boolean isLike;
}
