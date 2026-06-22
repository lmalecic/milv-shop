package com.lmalecic.milvshop.dto;

import com.lmalecic.milvshop.model.Nation;
import com.lmalecic.milvshop.model.TankRole;
import lombok.Builder;

import java.util.List;

@Builder
public record TankSearchResults(
        List<TankDto> tanks,
        List<Integer> mainGunCalibres,
        List<Nation> nations,
        List<TankRole> tankRoles,
        TankSearchCriteria filter
) {}
