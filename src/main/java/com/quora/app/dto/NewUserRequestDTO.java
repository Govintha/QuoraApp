package com.quora.app.dto;

import com.quora.app.entity.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewUserRequestDTO {

    @NotBlank
    private String userName;
    @NotBlank
    private String password;
    private List<Tag> tags=new ArrayList<>();
}
