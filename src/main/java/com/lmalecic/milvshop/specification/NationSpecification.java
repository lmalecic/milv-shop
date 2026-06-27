package com.lmalecic.milvshop.specification;

import com.lmalecic.milvshop.entity.Nation;
import com.lmalecic.milvshop.entity.Nation_;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

public final class NationSpecification {

    private NationSpecification() {}

    public static Sort sortByName() {
        return Sort.by(Sort.Order.by(Nation_.NAME).ignoreCase());
    }

    public static Sort sortByDeletedAndName() {
        return Sort.by(
                Sort.Order.desc(Nation_.DELETED),
                Sort.Order.by(Nation_.NAME).ignoreCase()
        );
    }

    public static Specification<Nation> nameLike(String name) {
        return (root, query, builder) -> {
            if (name == null || name.isBlank()) {
                return builder.conjunction();
            }
            return builder.like(builder.lower(root.get(Nation_.NAME)), "%" + name.toLowerCase() + "%");
        };
    }

    public static Specification<Nation> includeDeleted(boolean include) {
        return (root, query, builder) -> {
            if (include) {
                return builder.conjunction();
            } else {
                return root.get(Nation_.DELETED).equalTo(false);
            }
        };
    }
}
