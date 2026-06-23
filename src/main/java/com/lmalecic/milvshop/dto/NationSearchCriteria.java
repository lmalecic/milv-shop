package com.lmalecic.milvshop.dto;

public record NationSearchCriteria(
        String query,
        Boolean showDeleted
) implements SearchCriteria {

    @Override
    public boolean hasActiveFilters() {
        return this.query != null && !this.query.isBlank()
                || Boolean.TRUE.equals(this.showDeleted);
    }
}
