package com.quora.app.entity;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@SuperBuilder
public class Tag{

    @NotBlank(message = "Tags cannot be null and empty ")
    String tag;
}
