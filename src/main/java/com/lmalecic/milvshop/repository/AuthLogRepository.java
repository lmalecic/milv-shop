package com.lmalecic.milvshop.repository;

import com.lmalecic.milvshop.entity.AuthLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuthLogRepository extends JpaRepository<AuthLog, Long> {
    List<AuthLog> findAllByOrderByTimestampDesc();
}
