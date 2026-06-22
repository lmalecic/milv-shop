package com.lmalecic.milvshop.specification;

import com.lmalecic.milvshop.model.Nation;
import com.lmalecic.milvshop.model.Nation_;
import com.lmalecic.milvshop.model.Tank;
import com.lmalecic.milvshop.model.Tank_;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

public class NationSpecification {

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
            if (name == null) {
                return builder.conjunction();
            }
            return builder.like(builder.lower(root.get(Nation_.NAME)), "%" + name.toLowerCase() + "%");
        };
    }

    public static Specification<Nation> includeDeleted(Boolean include) {
        return (root, query, builder) -> {
            if (Boolean.TRUE.equals(include)) {
                return builder.conjunction();
            } else {
                return root.get(Tank_.DELETED).equalTo(false);
            }
        };
    }
}
