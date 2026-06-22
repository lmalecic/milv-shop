package com.lmalecic.milvshop.service;

import com.lmalecic.milvshop.model.Nation;
import com.lmalecic.milvshop.repository.NationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NationService {

    private final NationRepository nationRepository;

    public List<Nation> findAll() {
        return this.nationRepository.findAll();
    }

    public List<Nation> findAllOrdered() { return this.nationRepository.findAllByOrderByNameAsc(); }

    public Optional<Nation> findById(Long id) {
        return this.nationRepository.findById(id);
    }

    public Nation create(Nation nation) {
        if (nation.getId() != null) {
            throw new IllegalArgumentException("New nation cannot have an id.");
        }
        return this.nationRepository.save(nation);
    }

    public Nation update(Nation nation) {
        if (!this.nationRepository.existsById(nation.getId())) {
            throw new IllegalArgumentException("Nation with id " + nation.getId() + " does not exist.");
        }
        return this.nationRepository.save(nation);
    }

    public void deleteById(Long id) {
        this.nationRepository.deleteById(id);
    }
}
