package com.quora.app.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Document(collection = "answers")
public class Answer extends  BaseEntity{

    @NotBlank(message = "Content should be not Blank")
    @Size(min = 5,max = 1000,message = "Content must be between 10 to 100 character")
    private String content;
    @Indexed
    private String questionId;
    @Indexed
    private Integer  userId;
    private List<Tag> tags=new ArrayList<>();
    private Integer views=0;

    public void incrementViews() {
        if (this.views == null) {
            this.views = 0;
        }
        this.views++;
    }
}
