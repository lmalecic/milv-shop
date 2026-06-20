package com.lmalecic.milvshop.dto;

import com.lmalecic.milvshop.model.Nation;
import com.lmalecic.milvshop.model.TankRole;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class TankSearchResults {
    private final List<TankDto> tanks;
    private final List<Integer> mainGunCalibres;
    private final List<Nation> nations;
    private final List<TankRole> tankRoles;
    private final TankSearchCriteria filter;
}
