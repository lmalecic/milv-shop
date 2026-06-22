package com.lmalecic.milvshop.service;

import com.lmalecic.milvshop.dto.NationDto;
import com.lmalecic.milvshop.dto.NationSearchCriteria;
import com.lmalecic.milvshop.exception.NoContentException;
import com.lmalecic.milvshop.exception.ResourceNotFoundException;
import com.lmalecic.milvshop.model.Nation;
import com.lmalecic.milvshop.repository.NationRepository;
import com.lmalecic.milvshop.specification.NationSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NationService {

    private final NationRepository nationRepository;

    public List<NationDto> findAllActive() {
        return this.nationRepository.findAllByDeleted(false, NationSpecification.sortByName())
                .stream()
                .map(this::toDto)
                .toList();
    }

    public Optional<NationDto> findById(Long id) {
        return this.nationRepository.findById(id)
                .map(this::toDto);
    }

    public NationDto create(NationDto dto) {
        return this.toDto(this.nationRepository.save(this.toEntity(dto.withId(null))));
    }

    public NationDto update(NationDto dto) {
        if (!this.nationRepository.existsById(dto.id())) {
            throw new IllegalArgumentException("Nation with id " + dto.id() + " does not exist.");
        }
        return this.toDto(this.nationRepository.save(this.toEntity(dto)));
    }

    public NationDto deleteById(Long id) {
        var entity = this.nationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Nation with id " + id + " does not exist."));
        if (entity.isDeleted()) {
            throw new NoContentException("Nation with id " + id + " is already deleted.");
        }
        entity.setDeleted(true);
        return this.toDto(this.nationRepository.save(entity));
    }

    public NationDto recoverById(Long id) {
        var entity = this.nationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Nation with id " + id + " does not exist."));
        if (!entity.isDeleted()) {
            throw new NoContentException("Nation with id " + id + " isn't deleted.");
        }
        entity.setDeleted(false);
        return this.toDto(this.nationRepository.save(entity));
    }

    public List<NationDto> findAllBySearchCriteria(NationSearchCriteria searchCriteria) {
        if (!searchCriteria.hasActiveFilters()) {
            return this.findAllActive();
        }
        return this.nationRepository.findAll(NationSpecification.nameLike(searchCriteria.searchQuery())
                                .and(NationSpecification.includeDeleted(searchCriteria.showDeleted())),
                        NationSpecification.sortByDeletedAndName())
                .stream()
                .map(this::toDto)
                .toList();
    }

    private NationDto toDto(Nation entity) {
        return NationDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .imgPath(entity.getImgPath())
                .build();
    }

    private Nation toEntity(NationDto dto) {
        return Nation.builder()
                .id(dto.id())
                .name(dto.name())
                .imgPath(dto.imgPath())
                .build();
    }
}
