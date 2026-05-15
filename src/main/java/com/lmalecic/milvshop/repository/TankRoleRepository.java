package com.lmalecic.milvshop.repository;

import com.lmalecic.milvshop.model.TankRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TankRoleRepository extends JpaRepository<TankRole, Long> {
    List<TankRole> findAllByOrderByNameAsc();

    @Query("select name from TankRole")
    List<String> findAllNames();
}
