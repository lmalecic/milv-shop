package com.lmalecic.milvshop.criteria;

import com.lmalecic.milvshop.dto.SearchCriteria;

import java.util.List;

public record UserSearchCriteria(
        String query,
        List<Long> roleIds,
        Boolean showDeleted
) implements SearchCriteria {

    @Override
    public boolean hasActiveFilters() {
        return this.query != null && !this.query.isBlank()
                || this.roleIds != null && !this.roleIds.isEmpty()
                || Boolean.TRUE.equals(this.showDeleted);
    }
}
