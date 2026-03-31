package com.lmalecic.milvshop.dto;

import jakarta.annotation.Nullable;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class TanksFilterModel {
    private String searchQuery = null;
    private List<Long> nationIds = null;
    private List<Long> tankRoleIds = null;
    private Double priceMin = null;
    private Double priceMax = null;
    private Integer mainGunCalibre = null;
    private Integer armorThicknessMin = null;
    private Integer armorThicknessMax = null;
    private Integer maxSpeed = null;
    private Integer crewSize = null;
}
