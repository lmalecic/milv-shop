package com.lmalecic.milvshop.service;

import com.lmalecic.milvshop.dto.TanksFilterModel;
import com.lmalecic.milvshop.model.Tank;
import com.lmalecic.milvshop.repository.TankRepository;
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

    public List<Tank> findAll() {
        return tankRepository.findAll();
    }

    public Optional<Tank> findById(Long id) {
        return tankRepository.findById(id);
    }

    public Tank create(Tank tank) {
        if (tank.getId() != null) {
            throw new IllegalArgumentException("New tank cannot have an id.");
        }
        return tankRepository.save(tank);
    }

    public Tank update(Long id, Tank tank) {
        if (!tankRepository.existsById(id)) {
            throw new IllegalArgumentException("Tank with id " + id + " does not exist.");
        }
        tank.setId(id);
        return tankRepository.save(tank);
    }

    public void deleteById(Long id) {
        tankRepository.deleteById(id);
    }

    public List<Tank> findAllFiltered(TanksFilterModel filter) {
        return this.tankRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter.getSearchQuery() != null && !filter.getSearchQuery().isBlank()) {
                String q = "%" + filter.getSearchQuery().trim().toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("name")), q),
                        cb.like(cb.lower(root.get("description")), q)
                ));
            }

            if (filter.getNationIds() != null && !filter.getNationIds().isEmpty()) {
                predicates.add(root.get("nation").get("id").in(filter.getNationIds()));
            }

            if (filter.getTankRoleIds() != null && !filter.getTankRoleIds().isEmpty()) {
                predicates.add(root.get("tankRole").get("id").in(filter.getTankRoleIds()));
            }

            if (filter.getPriceMin() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("price"), filter.getPriceMin()));
            }

            if (filter.getPriceMax() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("price"), filter.getPriceMax()));
            }

            if (filter.getMainGunCalibre() != null) {
                predicates.add(root.get("mainGunCalibre").equalTo(filter.getMainGunCalibre()));
            }

            if (filter.getArmorThicknessMin() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("armorThickness"), filter.getArmorThicknessMin()));
            }

            if (filter.getArmorThicknessMax() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("armorThickness"), filter.getArmorThicknessMax()));
            }

            if (filter.getMaxSpeed() != null) {
                predicates.add(root.get("maxSpeed").equalTo(filter.getMaxSpeed()));
            }

            if (filter.getCrewSize() != null) {
                predicates.add(root.get("crewSize").equalTo(filter.getCrewSize()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        });
    }

    public List<Integer> findAllMainGunCalibres() {
        return this.findAll().stream()
                .map(Tank::getMainGunCalibre)
                .distinct()
                .sorted()
                .toList();
    }
}
