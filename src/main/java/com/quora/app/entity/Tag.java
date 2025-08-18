package com.quora.app.entity;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@SuperBuilder
@Document(collection = "tags")
public class Tag  extends  BaseEntity{

    @NotBlank(message = "Tags cannot be null and empty ")
    String tag;
}
