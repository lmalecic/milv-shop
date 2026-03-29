package com.lmalecic.milvshop.service;

import com.lmalecic.milvshop.model.Nation;
import com.lmalecic.milvshop.model.Tank;
import com.lmalecic.milvshop.repository.NationRepository;
import com.lmalecic.milvshop.repository.TankRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NationService {

    private final NationRepository nationRepository;

    public List<Nation> findAll() {
        return nationRepository.findAll();
    }

    public Optional<Nation> findById(Long id) {
        return nationRepository.findById(id);
    }

    public Nation create(Nation nation) {
        if (nation.getId() != null) {
            throw new IllegalArgumentException("New nation cannot have an id.");
        }
        return nationRepository.save(nation);
    }

    public Nation update(Nation nation) {
        if (!nationRepository.existsById(nation.getId())) {
            throw new IllegalArgumentException("Nation with id " + nation.getId() + " does not exist.");
        }
        return nationRepository.save(nation);
    }

    public void deleteById(Long id) {
        nationRepository.deleteById(id);
    }
}
