package com.lmalecic.milvshop.specification;

import com.lmalecic.milvshop.entity.Order;
import com.lmalecic.milvshop.entity.Order_;
import com.lmalecic.milvshop.entity.User_;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public final class OrderSpecification {

    private OrderSpecification() {}

    public static Sort orderByOrderDateDesc() {
        return Sort.by(Sort.Direction.DESC, Order_.ORDER_DATE);
    }

    public static Specification<Order> orderDateBetween(LocalDate before, LocalDate after) {
        return (root, query, builder) -> {
            if (before == null && after == null) {
                return builder.conjunction();
            } else if (before == null) {
                return builder.greaterThanOrEqualTo(root.get(Order_.ORDER_DATE), after);
            } else if (after == null) {
                return builder.lessThanOrEqualTo(root.get(Order_.ORDER_DATE), before.plusDays(1));
            } else {
                return builder.between(root.get(Order_.ORDER_DATE), after, before.plusDays(1));
            }
        };
    }

    public static Specification<Order> userIdEquals(String idString) {
        return (root, query, builder) -> {
            if (idString == null || idString.isBlank()) {
                return builder.disjunction();
            }

            try {
                return root.get(Order_.USER).get(User_.ID).equalTo(Long.parseLong(idString));
            } catch (NumberFormatException _) {
                return builder.disjunction();
            }
        };
    }

    public static Specification<Order> userIdEquals(Long id) {
        return (root, query, builder) -> {
            if (id == null) {
                return builder.disjunction(); // show no orders, don't wanna reveal all orders
            }
            return root.get(Order_.USER).get(User_.ID).equalTo(id);
        };
    }

    public static Specification<Order> usernameLike(String name) {
        return (root, query, builder) -> {
            if (name == null || name.isBlank()) {
                return builder.conjunction();
            }
            return builder.like(builder.lower(root.get(Order_.USER).get(User_.USERNAME)), "%" + name.toLowerCase() + "%");
        };
    }

    public static Specification<Order> includeDeleted(boolean include) {
        return (root, query, builder) -> {
            if (include) {
                return builder.conjunction();
            } else {
                return root.get(Order_.DELETED).equalTo(false);
            }
        };
    }
}
