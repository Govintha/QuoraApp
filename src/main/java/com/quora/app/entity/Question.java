package com.quora.app.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Document(collection = "questions")
@ToString(callSuper = true)
public class Question extends BaseEntity {

    @NotBlank(message = "Title should be not Blank")
    @Size(min = 5,max = 100,message = "Content must be between 10 to 100 character")
    private String title;

    @NotBlank(message = "Content should be not Blank")
    @Size(min = 5,max = 1000,message = "Content must be between 10 to 100 character")
    private String content;

    private Integer views=0;
    private List<Tag> tags;

    public void incrementViews() {
        if (views == null) {
            views = 0;
        }
        views++;
    }
}
