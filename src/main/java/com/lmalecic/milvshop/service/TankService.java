package com.lmalecic.milvshop.service;

import com.lmalecic.milvshop.dto.TankDto;
import com.lmalecic.milvshop.dto.TanksSearchCriteria;
import com.lmalecic.milvshop.model.Tank;
import com.lmalecic.milvshop.repository.TankRepository;
import com.lmalecic.milvshop.specification.TankSpecification;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    public Optional<TankDto> findById(Long id) {
        return this.tankRepository.findById(id)
                .map(this::toDto);
    }

    public TankDto create(TankDto tankDto) {
        tankDto.setId(null);
        return this.toDto(this.tankRepository.save(this.toEntity(tankDto)));
    }

    public TankDto update(Long id, TankDto tankDto) {
        if (!this.tankRepository.existsById(id)) {
            throw new IllegalArgumentException("Tank with id " + id + " does not exist.");
        }
        tankDto.setId(id);
        return this.toDto(this.tankRepository.save(this.toEntity(tankDto)));
    }

    public void deleteById(Long id) {
        this.tankRepository.deleteById(id);
    }

    public List<TankDto> findAllBySearchCriteria(TanksSearchCriteria filter) {
        if (!filter.hasActiveFilters()) {
            return this.findAll();
        }
        return this.tankRepository.findAll(TankSpecification.containsNameOrDescription(filter.getSearchQuery())
                        .and(TankSpecification.containsNation(filter.getNationIds()))
                        .and(TankSpecification.containsTankRole(filter.getTankRoleIds()))
                        .and(TankSpecification.priceBetween(filter.getPriceMin(), filter.getPriceMax()))
                        .and(TankSpecification.mainGunCalibreEquals(filter.getMainGunCalibre()))
                        .and(TankSpecification.armorThicknessBetween(filter.getArmorThicknessMin(), filter.getArmorThicknessMax()))
                        .and(TankSpecification.maxSpeedEquals(filter.getMaxSpeed()))
                        .and(TankSpecification.crewSizeEquals(filter.getCrewSize())))
                .stream()
                .map(this::toDto)
                .toList();
    }

    public List<Integer> findAllMainGunCalibres() {
        return this.findAll().stream()
                .map(TankDto::getMainGunCalibre)
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
                .build();
    }

    private Tank toEntity(TankDto dto) {
        return Tank.builder()
                .id(dto.getId())
                .name(dto.getName())
                .description(dto.getDescription())
                .imgPath(dto.getImgPath())
                .price(dto.getPrice())
                .mainGunCalibre(dto.getMainGunCalibre())
                .armorThickness(dto.getArmorThickness())
                .maxSpeed(dto.getMaxSpeed())
                .crewSize(dto.getCrewSize())
                .nation(dto.getNation())
                .tankRole(dto.getTankRole())
                .build();
    }
}
