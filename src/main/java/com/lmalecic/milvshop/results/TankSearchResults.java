package com.lmalecic.milvshop.results;

import com.lmalecic.milvshop.dto.NationDto;
import com.lmalecic.milvshop.dto.TankDto;
import com.lmalecic.milvshop.dto.TankRoleDto;
import com.lmalecic.milvshop.model.TankRole;
import lombok.Builder;

import java.util.List;

@Builder
public record TankSearchResults(
        List<TankDto> tanks,
        List<Integer> mainGunCalibres,
        List<NationDto> nations,
        List<TankRoleDto> tankRoles
) {}
