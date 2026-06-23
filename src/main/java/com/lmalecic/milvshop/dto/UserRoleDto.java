package com.lmalecic.milvshop.dto;

import lombok.Builder;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Builder
public record UserRoleDto(
        Long id,
        String name
) implements Displayable {
    @Override
    public String getDisplayName() {
        return Arrays.stream(this.name.split("_"))
                .skip(1)
                .map(word -> word.substring(0, 1).toUpperCase()
                        + word.substring(1).toLowerCase())
                .collect(Collectors.joining());
    }

    @Override
    public String getDisplayableType() {
        return "User Role";
    }

    @Override
    public List<DisplayElement> getDisplayElements() {
        return List.of();
    }
}
