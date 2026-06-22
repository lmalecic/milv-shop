package com.lmalecic.milvshop.dto;

public record NationSearchCriteria(
        String searchQuery,
        Boolean showDeleted
) implements SearchCriteria {

    @Override
    public boolean hasActiveFilters() {
        return (this.searchQuery != null && !this.searchQuery.isBlank())
                || (this.showDeleted != null);
    }
}
