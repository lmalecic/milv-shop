package com.lmalecic.milvshop.criteria;

import java.math.BigDecimal;
import java.util.List;

public record TankSearchCriteria(
        String query,
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
) implements SearchCriteria {

    @Override
    public boolean hasActiveFilters() {
        return this.query != null && !this.query.isBlank()
                || this.nationIds != null && !this.nationIds.isEmpty()
                || this.tankRoleIds != null && !this.tankRoleIds.isEmpty()
                || this.priceMin != null
                || this.priceMax != null
                || this.mainGunCalibre != null
                || this.armorThicknessMin != null
                || this.armorThicknessMax != null
                || this.maxSpeed != null
                || this.crewSize != null
                || Boolean.TRUE.equals(this.showDeleted);
    }
}
