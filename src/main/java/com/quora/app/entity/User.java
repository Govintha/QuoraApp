package com.quora.app.entity;

import jakarta.validation.constraints.NotBlank;
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
@Document(collection = "users")
public class User extends BaseEntity {

    @NotBlank
    @Indexed(unique = true)
    private Integer userId;

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    private List<Integer> followingIds = new ArrayList<>();

    private List<Integer> followerIds = new ArrayList<>();
}
