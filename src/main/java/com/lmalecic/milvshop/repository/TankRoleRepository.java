package com.lmalecic.milvshop.repository;

import com.lmalecic.milvshop.entity.TankRole;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TankRoleRepository extends JpaRepository<TankRole, Long>, JpaSpecificationExecutor<TankRole> {
    @Query("select name from TankRole")
    List<String> findAllNames();

    List<TankRole> findAllByDeleted(boolean deleted, Sort sort);
}
