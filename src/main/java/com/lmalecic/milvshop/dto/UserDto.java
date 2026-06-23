package com.lmalecic.milvshop.dto;

import lombok.Builder;

import java.util.List;
import java.util.stream.Collectors;

@Builder
public record UserDto(
        Long id,
        String username,
        List<UserRoleDto> roles,
        boolean deleted
) implements Displayable {

    @Override
    public String getDisplayName() {
        return this.username;
    }

    @Override
    public String getDisplayableType() {
        return "User";
    }

    @Override
    public List<DisplayElement> getDisplayElements() {
        return List.of(
                new DisplayElement("Id", this.id.toString()),
                new DisplayElement("Username", this.username),
                new DisplayElement("Roles", this.getRolesString())
        );
    }

    public String getRolesString() {
        return this.roles.stream()
                .map(UserRoleDto::getDisplayName)
                .collect(Collectors.joining(", "));
    }
}
