package com.lmalecic.milvshop.repository;

import com.lmalecic.milvshop.model.Tank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface TankRepository extends JpaRepository<Tank, Long>, JpaSpecificationExecutor<Tank> {
    boolean existsByName(String name);
}
