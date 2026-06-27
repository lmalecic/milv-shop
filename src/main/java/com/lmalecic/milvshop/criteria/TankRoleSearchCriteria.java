package com.lmalecic.milvshop.criteria;

public record TankRoleSearchCriteria(
        String query,
        Boolean showDeleted
) implements SearchCriteria {

    @Override
    public boolean hasActiveFilters() {
        return this.query != null && !this.query.isBlank()
                || Boolean.TRUE.equals(this.showDeleted);
    }
}
