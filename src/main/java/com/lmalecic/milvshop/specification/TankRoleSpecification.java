package com.lmalecic.milvshop.specification;

import com.lmalecic.milvshop.entity.TankRole;
import com.lmalecic.milvshop.entity.TankRole_;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

public class TankRoleSpecification {

    private TankRoleSpecification() {}

    public static Sort sortByName() {
        return Sort.by(Sort.Order.by(TankRole_.NAME).ignoreCase());
    }

    public static Sort sortByDeletedAndName() {
        return Sort.by(
                Sort.Order.desc(TankRole_.DELETED),
                Sort.Order.by(TankRole_.NAME).ignoreCase()
        );
    }

    public static Specification<TankRole> nameLike(String name) {
        return (root, query, builder) -> {
            if (name == null || name.isBlank()) {
                return builder.conjunction();
            }
            return builder.like(builder.lower(root.get(TankRole_.NAME)), "%" + name.toLowerCase() + "%");
        };
    }

    public static Specification<TankRole> includeDeleted(boolean include) {
        return (root, query, builder) -> {
            if (include) {
                return builder.conjunction();
            } else {
                return root.get(TankRole_.DELETED).equalTo(false);
            }
        };
    }
}
