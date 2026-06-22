package com.lmalecic.milvshop.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserAuthDto(
        @NotNull(message = "Username is required!")
        @NotBlank(message = "Username can't be blank!")
        String username,
        @NotNull(message = "Password is required!")
        @NotBlank(message = "Password can't be blank!")
        String password
) {
    public static UserAuthDto empty() {
        return new UserAuthDto("", "");
    }
}
