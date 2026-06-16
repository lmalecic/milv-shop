package com.lmalecic.milvshop.viewmodel;

import com.lmalecic.milvshop.dto.TankDto;
import com.lmalecic.milvshop.dto.TanksSearchCriteria;
import com.lmalecic.milvshop.model.Nation;
import com.lmalecic.milvshop.model.TankRole;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class TanksViewModel {
    private final List<TankDto> tanks;
    private final List<Integer> mainGunCalibres;
    private final List<Nation> nations;
    private final List<TankRole> tankRoles;
    private final TanksSearchCriteria filter;
}
