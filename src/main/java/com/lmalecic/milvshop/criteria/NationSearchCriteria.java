package com.lmalecic.milvshop.criteria;

import com.lmalecic.milvshop.dto.SearchCriteria;

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
