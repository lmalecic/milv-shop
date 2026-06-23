package com.lmalecic.milvshop.service;

import com.lmalecic.milvshop.dto.UserRoleDto;
import com.lmalecic.milvshop.model.UserRole;
import com.lmalecic.milvshop.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserRoleService {

    private final UserRoleRepository userRoleRepository;

    public List<UserRoleDto> findAllActive() {
        return this.userRoleRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    public UserRoleDto toDto(UserRole userRole) {
        return UserRoleDto.builder()
                .id(userRole.getId())
                .name(userRole.getName())
                .build();
    }

    public UserRole toEntity(UserRoleDto userRoleDto) {
        return UserRole.builder()
                .id(userRoleDto.id())
                .name(userRoleDto.name())
                .build();
    }
}
