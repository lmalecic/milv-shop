package com.lmalecic.milvshop.service;

import com.lmalecic.milvshop.dto.TankDto;
import com.lmalecic.milvshop.dto.TankSearchCriteria;
import com.lmalecic.milvshop.exception.NoContentException;
import com.lmalecic.milvshop.exception.ResourceNotFoundException;
import com.lmalecic.milvshop.model.Tank;
import com.lmalecic.milvshop.repository.TankRepository;
import com.lmalecic.milvshop.specification.TankSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
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

    public List<TankDto> findAllBySearchCriteria(TankSearchCriteria filter) {
        if (!filter.hasActiveFilters()) {
            return this.findAllActive();
        }
        return this.tankRepository.findAll(TankSpecification.containsNameOrDescription(filter.searchQuery())
                        .and(TankSpecification.containsNation(filter.nationIds()))
                        .and(TankSpecification.containsTankRole(filter.tankRoleIds()))
                        .and(TankSpecification.priceBetween(filter.priceMin(), filter.priceMax()))
                        .and(TankSpecification.mainGunCalibreEquals(filter.mainGunCalibre()))
                        .and(TankSpecification.armorThicknessBetween(filter.armorThicknessMin(), filter.armorThicknessMax()))
                        .and(TankSpecification.maxSpeedEquals(filter.maxSpeed()))
                        .and(TankSpecification.crewSizeEquals(filter.crewSize())),
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

    private TankDto toDto(Tank tank) {
        return TankDto.builder()
                .id(tank.getId())
                .name(tank.getName())
                .description(tank.getDescription())
                .imgPath(tank.getImgPath())
                .price(tank.getPrice())
                .mainGunCalibre(tank.getMainGunCalibre())
                .armorThickness(tank.getArmorThickness())
                .maxSpeed(tank.getMaxSpeed())
                .crewSize(tank.getCrewSize())
                .nation(tank.getNation())
                .tankRole(tank.getTankRole())
                .deleted(tank.isDeleted())
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
