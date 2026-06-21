package com.lmalecic.milvshop.dto;

import com.lmalecic.milvshop.model.UserRole;

import java.util.List;

public record UserDto(
        Long id,
        String username,
        List<UserRole> roles
) {}
