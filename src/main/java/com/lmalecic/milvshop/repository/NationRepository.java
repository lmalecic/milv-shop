package com.lmalecic.milvshop.repository;

import com.lmalecic.milvshop.entity.Nation;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NationRepository extends JpaRepository<Nation, Long>, JpaSpecificationExecutor<Nation> {
    List<Nation> findAllByOrderByNameAsc();

    @Query("select name from Nation")
    List<String> findAllNames();
    List<Nation> findAllByDeleted(boolean deleted, Sort sort);
}
