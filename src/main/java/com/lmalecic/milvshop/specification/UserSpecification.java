package com.lmalecic.milvshop.specification;

import com.lmalecic.milvshop.entity.User;
import com.lmalecic.milvshop.entity.UserRole_;
import com.lmalecic.milvshop.entity.User_;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class UserSpecification {

    private UserSpecification() {}

    public static Sort sortByDeletedAndId() {
        return Sort.by(
                Sort.Order.desc(User_.DELETED),
                Sort.Order.by(User_.ID)
        );
    }

    public static Specification<User> idEquals(String idString) {
        return (root, query, builder) -> {
            if (idString == null || idString.isBlank()) {
                return builder.disjunction();
            }

            try {
                return root.get(User_.id).equalTo(Long.parseLong(idString));
            } catch (NumberFormatException _) {
                return builder.disjunction();
            }
        };
    }

    public static Specification<User> usernameLike(String name) {
        return (root, query, builder) -> {
            if (name == null || name.isBlank()) {
                return builder.conjunction();
            }
            return builder.like(builder.lower(root.get(User_.USERNAME)), "%" + name.toLowerCase() + "%");
        };
    }

    public static Specification<User> includeDeleted(boolean include) {
        return (root, query, builder) -> {
            if (include) {
                return builder.conjunction();
            } else {
                return root.get(User_.DELETED).equalTo(false);
            }
        };
    }

    public static Specification<User> containsRoles(List<Long> roleIds) {
        return (root, query, builder) -> {
            if (roleIds == null || roleIds.isEmpty()) {
                return builder.conjunction();
            }
            return root.join(User_.ROLES, JoinType.INNER).get(UserRole_.ID).in(roleIds);
        };
    }
}
