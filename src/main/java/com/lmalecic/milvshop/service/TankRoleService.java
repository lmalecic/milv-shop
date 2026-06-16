package com.lmalecic.milvshop.service;

import com.lmalecic.milvshop.model.TankRole;
import com.lmalecic.milvshop.repository.TankRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TankRoleService {

    private final TankRoleRepository tankRoleRepository;

    public List<TankRole> findAll() {
        return tankRoleRepository.findAll();
    }

    public List<TankRole> findAllOrdered() { return this.tankRoleRepository.findAllByOrderByNameAsc(); }

    public Optional<TankRole> findById(Long id) {
        return tankRoleRepository.findById(id);
    }

    public TankRole create(TankRole tankRole) {
        if (tankRole.getId() != null) {
            throw new IllegalArgumentException("New nation cannot have an id.");
        }
        return tankRoleRepository.save(tankRole);
    }

    public TankRole update(TankRole tankRole) {
        if (!tankRoleRepository.existsById(tankRole.getId())) {
            throw new IllegalArgumentException("Nation with id " + tankRole.getId() + " does not exist.");
        }
        return tankRoleRepository.save(tankRole);
    }

    public void deleteById(Long id) {
        tankRoleRepository.deleteById(id);
    }
}
