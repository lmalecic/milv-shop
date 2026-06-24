package com.lmalecic.milvshop.repository;

import com.lmalecic.milvshop.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    Optional<UserRole> findByName(String name);

    boolean existsByName(String name);

    @Query("select name from UserRole")
    List<String> findAllNames();
}
