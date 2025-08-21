package com.quora.app.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AnswerRequestDTO {

    @NotBlank(message = "Content should be not Blank")
    @Size(min = 5,max = 1000,message = "Content must be between 10 to 100 character")
    private String content;

    @NotBlank(message = "Question Id cannot be Null or Empty")
    private String questionId;

    @JsonProperty("userId")
    private Integer userId;

}
