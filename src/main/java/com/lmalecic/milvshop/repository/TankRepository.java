package com.lmalecic.milvshop.repository;

import com.lmalecic.milvshop.dto.TanksFilterModel;
import com.lmalecic.milvshop.model.Nation;
import com.lmalecic.milvshop.model.Tank;
import com.lmalecic.milvshop.model.TankRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface TankRepository extends JpaRepository<Tank, Long>, JpaSpecificationExecutor<Tank> {
}
