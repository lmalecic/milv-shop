package com.lmalecic.milvshop.repository;

import com.lmalecic.milvshop.entity.Tank;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TankRepository extends JpaRepository<Tank, Long>, JpaSpecificationExecutor<Tank> {
    boolean existsByName(String name);
    List<Tank> findAllByDeleted(boolean deleted, Sort sort);
}
