package com.lmalecic.milvshop.results;

import com.lmalecic.milvshop.dto.*;
import lombok.Builder;

import java.util.List;

@Builder
public record UserSearchResults(
        List<UserDto> users,
        List<UserRoleDto> roles
) {}
