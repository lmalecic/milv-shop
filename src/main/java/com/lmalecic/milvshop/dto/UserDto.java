package com.lmalecic.milvshop.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class UserDto {
    @NotNull
    @NotBlank
    private final String username;
    @NotNull
    @NotBlank
    private final String password;
}
