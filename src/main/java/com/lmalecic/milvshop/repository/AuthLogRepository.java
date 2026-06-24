package com.lmalecic.milvshop.repository;

import com.lmalecic.milvshop.entity.AuthLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthLogRepository extends JpaRepository<AuthLog, Long> {
}
