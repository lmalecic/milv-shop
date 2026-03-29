package com.lmalecic.milvshop.repository;

import com.lmalecic.milvshop.model.TankRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TankRoleRepository extends JpaRepository<TankRole, Long> {
}
