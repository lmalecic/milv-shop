package com.lmalecic.milvshop.service;

import com.lmalecic.milvshop.dto.TankDto;
import com.lmalecic.milvshop.criteria.TankSearchCriteria;
import com.lmalecic.milvshop.exception.NoContentException;
import com.lmalecic.milvshop.exception.ResourceNotFoundException;
import com.lmalecic.milvshop.model.Tank;
import com.lmalecic.milvshop.repository.TankRepository;
import com.lmalecic.milvshop.specification.TankSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TankService {

    private final TankRepository tankRepository;

    public List<TankDto> findAll() {
        return this.tankRepository.findAll()
                .stream().map(this::toDto)
                .toList();
    }

    public List<TankDto> findAllActive() {
        return this.tankRepository.findAllByDeleted(false, TankSpecification.sortByName())
                .stream().map(this::toDto)
                .toList();
    }

    public Optional<TankDto> findById(Long id) {
        return this.tankRepository.findById(id)
                .map(this::toDto);
    }

    public TankDto create(TankDto tankDto) {
        return this.toDto(this.tankRepository.save(this.toEntity(tankDto.withId(null))));
    }

    public TankDto update(TankDto tankDto) {
        if (!this.tankRepository.existsById(tankDto.id())) {
            throw new IllegalArgumentException("Tank with id " + tankDto.id() + " does not exist.");
        }
        return this.toDto(this.tankRepository.save(this.toEntity(tankDto)));
    }

    public TankDto deleteById(Long id) {
        Tank tank = this.tankRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tank with id " + id + " does not exist."));
        if (tank.isDeleted()) {
            throw new NoContentException("Tank with id " + id + " is already deleted.");
        }
        tank.setDeleted(true);
        return this.toDto(this.tankRepository.save(tank));
    }

    public TankDto recoverById(Long id) {
        Tank tank = this.tankRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tank with id " + id + " does not exist."));
        if (!tank.isDeleted()) {
            throw new NoContentException("Tank with id " + id + " isn't deleted.");
        }
        tank.setDeleted(false);
        return this.toDto(this.tankRepository.save(tank));
    }

    public List<TankDto> findAllBySearchCriteria(TankSearchCriteria criteria) {
        if (!criteria.hasActiveFilters()) {
            return this.findAllActive();
        }
        return this.tankRepository.findAll(TankSpecification.containsNameOrDescription(criteria.query())
                                .and(TankSpecification.containsNation(criteria.nationIds()))
                                .and(TankSpecification.containsTankRole(criteria.tankRoleIds()))
                                .and(TankSpecification.priceBetween(criteria.priceMin(), criteria.priceMax()))
                                .and(TankSpecification.mainGunCalibreEquals(criteria.mainGunCalibre()))
                                .and(TankSpecification.armorThicknessBetween(criteria.armorThicknessMin(), criteria.armorThicknessMax()))
                                .and(TankSpecification.maxSpeedEquals(criteria.maxSpeed()))
                                .and(TankSpecification.crewSizeEquals(criteria.crewSize()))
                                .and(TankSpecification.includeDeleted(criteria.showDeleted())),
                        TankSpecification.sortByDeletedAndName())
                .stream()
                .map(this::toDto)
                .toList();
    }

    public List<Integer> findAllMainGunCalibres() {
        return this.findAll().stream()
                .map(TankDto::mainGunCalibre)
                .distinct()
                .sorted()
                .toList();
    }

    private TankDto toDto(Tank entity) {
        return TankDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .imgPath(entity.getImgPath())
                .price(entity.getPrice())
                .mainGunCalibre(entity.getMainGunCalibre())
                .armorThickness(entity.getArmorThickness())
                .maxSpeed(entity.getMaxSpeed())
                .crewSize(entity.getCrewSize())
                .nation(entity.getNation())
                .tankRole(entity.getTankRole())
                .deleted(entity.isDeleted())
                .build();
    }

    private Tank toEntity(TankDto dto) {
        return Tank.builder()
                .id(dto.id())
                .name(dto.name())
                .description(dto.description())
                .imgPath(dto.imgPath())
                .price(dto.price())
                .mainGunCalibre(dto.mainGunCalibre())
                .armorThickness(dto.armorThickness())
                .maxSpeed(dto.maxSpeed())
                .crewSize(dto.crewSize())
                .nation(dto.nation())
                .tankRole(dto.tankRole())
                .deleted(dto.deleted())
                .build();
    }
}
