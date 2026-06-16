package com.lmalecic.milvshop.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
public class TanksSearchCriteria {
    private String searchQuery = null;
    private List<Long> nationIds = null;
    private List<Long> tankRoleIds = null;
    private BigDecimal priceMin = null;
    private BigDecimal priceMax = null;
    private Integer mainGunCalibre = null;
    private Integer armorThicknessMin = null;
    private Integer armorThicknessMax = null;
    private Integer maxSpeed = null;
    private Integer crewSize = null;

    public boolean hasActiveFilters() {
        return (this.searchQuery != null && !this.searchQuery.isEmpty()) ||
                (this.nationIds != null && !this.nationIds.isEmpty()) ||
                (this.tankRoleIds != null && !this.tankRoleIds.isEmpty()) ||
                (this.priceMin != null) || (this.priceMax != null) ||
                (this.mainGunCalibre != null) ||
                (this.armorThicknessMin != null) || (this.armorThicknessMax != null) ||
                (this.maxSpeed != null) ||
                (this.crewSize != null);
    }
}
