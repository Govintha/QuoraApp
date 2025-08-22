package com.quora.app.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
@Document(collection = "questions")
public class Question extends BaseEntity {

    @NotBlank(message = "Title should be not Blank")
    @Size(min = 5,max = 100,message = "Content must be between 10 to 100 character")
    private String title;

    @NotBlank(message = "Content should be not Blank")
    @Size(min = 5,max = 1000,message = "Content must be between 10 to 100 character")
    private String content;

    private Integer views=0;
    private List<Tag> tags=new ArrayList<>();
    private List<String> answerID=new ArrayList<>();


    @Indexed
    private Integer userId;

    public void incrementViews() {
        if (this.views == null) {
            this.views = 0;
        }
        this.views++;
    }
}
