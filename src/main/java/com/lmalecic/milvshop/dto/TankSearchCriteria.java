package com.lmalecic.milvshop.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
public class TankSearchCriteria {
    private String searchQuery;
    private List<Long> nationIds;
    private List<Long> tankRoleIds;
    private BigDecimal priceMin;
    private BigDecimal priceMax;
    private Integer mainGunCalibre;
    private Integer armorThicknessMin;
    private Integer armorThicknessMax;
    private Integer maxSpeed;
    private Integer crewSize;
    private Boolean showDeleted;

    public boolean hasActiveFilters() {
        return (this.searchQuery != null && !this.searchQuery.isEmpty()) ||
                (this.nationIds != null && !this.nationIds.isEmpty()) ||
                (this.tankRoleIds != null && !this.tankRoleIds.isEmpty()) ||
                (this.priceMin != null) || (this.priceMax != null) ||
                (this.mainGunCalibre != null) ||
                (this.armorThicknessMin != null) || (this.armorThicknessMax != null) ||
                (this.maxSpeed != null) ||
                (this.crewSize != null) ||
                (this.showDeleted != null);
    }
}
