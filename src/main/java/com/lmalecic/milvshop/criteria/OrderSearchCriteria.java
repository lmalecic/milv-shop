package com.lmalecic.milvshop.criteria;

import java.time.LocalDate;

public record OrderSearchCriteria(
        LocalDate afterDate,
        LocalDate beforeDate,
        String userQuery,
        Boolean showDeleted
) implements SearchCriteria {

    @Override
    public boolean hasActiveFilters() {
        return this.afterDate != null
                || this.beforeDate != null
                || (this.userQuery != null && !this.userQuery.isEmpty())
                || Boolean.TRUE.equals(this.showDeleted);
    }
}
