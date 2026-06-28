package com.lmalecic.milvshop.service;

import com.lmalecic.milvshop.criteria.TankRoleSearchCriteria;
import com.lmalecic.milvshop.dto.TankRoleDto;
import com.lmalecic.milvshop.exception.NoContentException;
import com.lmalecic.milvshop.exception.ResourceNotFoundException;
import com.lmalecic.milvshop.entity.TankRole;
import com.lmalecic.milvshop.repository.TankRoleRepository;
import com.lmalecic.milvshop.specification.TankRoleSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TankRoleService {

    private final TankRoleRepository tankRoleRepository;

    public List<TankRoleDto> findAllActive() {
        return this.tankRoleRepository.findAllByDeleted(false, TankRoleSpecification.sortByName())
                .stream()
                .map(this::toDto)
                .toList();
    }

    public Optional<TankRoleDto> findById(Long id) {
        return this.tankRoleRepository.findById(id)
                .map(this::toDto);
    }

    public TankRoleDto create(TankRoleDto dto) {
        return this.toDto(this.tankRoleRepository.save(this.toEntity(dto.withId(null))));
    }

    public TankRoleDto update(TankRoleDto dto) {
        if (!this.tankRoleRepository.existsById(dto.id())) {
            throw new IllegalArgumentException("Tank Role with id " + dto.id() + " does not exist.");
        }
        return this.toDto(this.tankRoleRepository.save(this.toEntity(dto)));
    }

    public TankRoleDto deleteById(Long id) {
        var entity = this.tankRoleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tank Role with id " + id + " does not exist."));
        if (entity.isDeleted()) {
            throw new NoContentException("Tank Role with id " + id + " is already deleted.");
        }
        entity.setDeleted(true);
        return this.toDto(this.tankRoleRepository.save(entity));
    }

    public List<TankRoleDto> findAllBySearchCriteria(TankRoleSearchCriteria searchCriteria) {
        if (!searchCriteria.hasActiveFilters()) {
            return this.findAllActive();
        }
        return this.tankRoleRepository.findAll(TankRoleSpecification.nameLike(searchCriteria.query())
                                .and(TankRoleSpecification.includeDeleted(searchCriteria.showDeleted())),
                        TankRoleSpecification.sortByDeletedAndName())
                .stream()
                .map(this::toDto)
                .toList();
    }

    public TankRoleDto recoverById(Long id) {
        var entity = this.tankRoleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Nation with id " + id + " does not exist."));
        if (!entity.isDeleted()) {
            throw new NoContentException("Nation with id " + id + " isn't deleted.");
        }
        entity.setDeleted(false);
        return this.toDto(this.tankRoleRepository.save(entity));
    }

    private TankRoleDto toDto(TankRole entity) {
        return TankRoleDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .imgPath(entity.getImgPath())
                .deleted(entity.isDeleted())
                .build();
    }

    private TankRole toEntity(TankRoleDto dto) {
        return TankRole.builder()
                .id(dto.id())
                .name(dto.name())
                .imgPath(dto.imgPath())
                .deleted(dto.deleted())
                .build();
    }
}
