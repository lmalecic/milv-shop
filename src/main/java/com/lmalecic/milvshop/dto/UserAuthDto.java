package com.lmalecic.milvshop.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserAuthDto {
    @NotNull(message = "Username is required!")
    @NotBlank(message = "Username can't be blank!")
    private String username;
    @NotNull(message = "Password is required!")
    @NotBlank(message = "Password can't be blank!")
    private String password;
}
