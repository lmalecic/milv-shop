package com.lmalecic.milvshop.repository;

import com.lmalecic.milvshop.model.Nation;
import com.lmalecic.milvshop.model.Tank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NationRepository extends JpaRepository<Nation, Long> {
    List<Nation> findAllByOrderByNameAsc();

    @Query("select name from Nation")
    List<String> findAllNames();
    Nation findByName(String usa);
}
