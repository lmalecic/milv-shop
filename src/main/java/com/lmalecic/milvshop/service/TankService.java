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

    public Tank update(Tank tank) {
        if (!tankRepository.existsById(tank.getId())) {
            throw new IllegalArgumentException("Tank with id " + tank.getId() + " does not exist.");
        }
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

            return cb.and(predicates.toArray(new Predicate[0]));
        });
    }
}
