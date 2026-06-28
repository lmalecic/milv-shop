package com.lmalecic.milvshop.service;

import com.lmalecic.milvshop.criteria.UserSearchCriteria;
import com.lmalecic.milvshop.dto.UserAuthDto;
import com.lmalecic.milvshop.dto.UserDto;
import com.lmalecic.milvshop.entity.User_;
import com.lmalecic.milvshop.exception.NoContentException;
import com.lmalecic.milvshop.exception.ResourceNotFoundException;
import com.lmalecic.milvshop.entity.User;
import com.lmalecic.milvshop.repository.UserRepository;
import com.lmalecic.milvshop.repository.UserRoleRepository;
import com.lmalecic.milvshop.specification.UserSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private static final String ENTITY_NOT_FOUND_MESSAGE = "User with %s %s not found!";

    private final UserRoleService userRoleService;
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;

    public List<UserDto> findAllActive() {
        return this.userRepository.findAllByDeleted(false, UserSpecification.sortByDeletedAndId())
                .stream()
                .map(this::toDto)
                .toList();
    }

    public Optional<UserDto> findById(Long id) {
        return this.userRepository.findById(id)
                .map(this::toDto);
    }

    public UserDto update(UserDto dto) {
        var user = this.userRepository.findByUsername(dto.username())
                .orElseThrow(() -> new ResourceNotFoundException(ENTITY_NOT_FOUND_MESSAGE.formatted(User_.USERNAME, dto.username())));

        user.setUsername(dto.username());
        user.setRoles(dto.roles().stream()
                .map(this.userRoleService::toEntity)
                .toList());

        return this.toDto(this.userRepository.save(user));
    }

    public UserDto register(UserAuthDto userAuthDto) {
        return this.toDto(this.userRepository.save(User.builder()
                .username(userAuthDto.username())
                .pwdHash(this.passwordEncoder.encode(userAuthDto.password()))
                .roles(List.of(this.userRoleRepository.findByName("ROLE_USER")
                        .orElseThrow()))
                .build()));
    }

    public UserDto deleteById(Long id) {
        var entity = this.userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ENTITY_NOT_FOUND_MESSAGE.formatted(User_.ID, id)));
        if (entity.isDeleted()) {
            throw new NoContentException("Nation with id " + id + " is already deleted.");
        }
        entity.setDeleted(true);
        return this.toDto(this.userRepository.save(entity));
    }

    public UserDto recoverById(Long id) {
        var entity = this.userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ENTITY_NOT_FOUND_MESSAGE.formatted(User_.ID, id)));
        if (!entity.isDeleted()) {
            throw new NoContentException("Nation with id " + id + " isn't deleted.");
        }
        entity.setDeleted(false);
        return this.toDto(this.userRepository.save(entity));
    }

    public List<UserDto> findAllBySearchCriteria(UserSearchCriteria searchCriteria) {
        if (!searchCriteria.hasActiveFilters()) {
            return this.findAllActive();
        }
        return this.userRepository.findAll(UserSpecification.includeDeleted(searchCriteria.showDeleted())
                                .and(UserSpecification.idEquals(searchCriteria.query())
                                        .or(UserSpecification.usernameLike(searchCriteria.query())))
                                .and(UserSpecification.containsRoles(searchCriteria.roleIds())),
                        UserSpecification.sortByDeletedAndId())
                .stream()
                .map(this::toDto)
                .toList();
    }

    public boolean existsByUsername(String username) {
        return this.userRepository.existsByUsername(username);
    }

    public UserDto toDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .roles(user.getRoles().stream()
                        .map(this.userRoleService::toDto)
                        .toList())
                .deleted(user.isDeleted())
                .build();
    }
}
