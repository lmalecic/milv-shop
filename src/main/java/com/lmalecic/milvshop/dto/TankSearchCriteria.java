package com.lmalecic.milvshop.dto;

import java.math.BigDecimal;
import java.util.List;

public record TankSearchCriteria(
        String searchQuery,
        List<Long> nationIds,
        List<Long> tankRoleIds,
        BigDecimal priceMin,
        BigDecimal priceMax,
        Integer mainGunCalibre,
        Integer armorThicknessMin,
        Integer armorThicknessMax,
        Integer maxSpeed,
        Integer crewSize,
        Boolean showDeleted
) {
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
