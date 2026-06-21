package com.lmalecic.milvshop.specification;

import com.lmalecic.milvshop.model.*;
import jakarta.persistence.metamodel.SingularAttribute;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.List;

public class TankSpecification {

    private TankSpecification() {}

    public static Sort sortByName() {
        return Sort.by(Tank_.NAME);
    }

    public static Sort sortByDeletedAndName() {
        return Sort.by(Sort.Direction.DESC, Tank_.DELETED)
                .and(Sort.by(Tank_.NAME));
    }

    public static Specification<Tank> containsNameOrDescription(String nameOrDescription) {
        return (root, query, builder) -> {
            if (nameOrDescription == null || nameOrDescription.isBlank()) {
                return builder.conjunction();
            }
            return builder.or(
                    builder.like(builder.lower(root.get(Tank_.NAME)), "%" + nameOrDescription.toLowerCase() + "%"),
                    builder.like(builder.lower(root.get(Tank_.DESCRIPTION)), "%" + nameOrDescription.toLowerCase() + "%"));
        };
    }

    public static Specification<Tank> containsNation(List<Long> nationIds) {
        return containsIds(Tank_.nation, Nation_.id, nationIds);
    }

    public static Specification<Tank> containsTankRole(List<Long> tankRoleIds) {
        return containsIds(Tank_.tankRole, TankRole_.id, tankRoleIds);
    }

    public static Specification<Tank> priceBetween(BigDecimal priceFrom, BigDecimal priceTo) {
        return between(Tank_.price, priceFrom, priceTo);
    }

    public static Specification<Tank> armorThicknessBetween(Integer armorThicknessMin, Integer armorThicknessMax) {
        return between(Tank_.armorThickness, armorThicknessMin, armorThicknessMax);
    }

    public static Specification<Tank> mainGunCalibreEquals(Integer mainGunCalibre) {
        return equals(Tank_.mainGunCalibre, mainGunCalibre);
    }

    public static Specification<Tank> maxSpeedEquals(Integer maxSpeed) {
        return equals(Tank_.maxSpeed, maxSpeed);
    }

    public static Specification<Tank> crewSizeEquals(Integer crewSize) {
        return equals(Tank_.crewSize, crewSize);
    }

    public static Specification<Tank> deletedEquals(Boolean isDeleted) {
        return equals(Tank_.deleted, isDeleted);
    }

    private static <T extends Comparable<? super T>> Specification<Tank> between(SingularAttribute<Tank, T> attribute, T min, T max) {
        return (root, query, builder) -> {
            if (min == null && max == null) {
                return builder.conjunction();
            } else if (min == null) {
                return builder.lessThanOrEqualTo(root.get(attribute), max);
            } else if (max == null) {
                return builder.greaterThanOrEqualTo(root.get(attribute), min);
            } else {
                return builder.between(root.get(attribute), min, max);
            }
        };
    }

    private static <T> Specification<Tank> equals(SingularAttribute<Tank, T> attribute, T other) {
        return (root, query, builder) -> {
            if (other == null) {
                return builder.conjunction();
            }
            return root.get(attribute).equalTo(other);
        };
    }

    private static <R> Specification<Tank> containsIds(SingularAttribute<Tank, R> relation, SingularAttribute<R, Long> idAttribute, List<Long> ids) {
        return (root, query, builder) -> {
            if (ids == null || ids.isEmpty()) {
                return builder.conjunction();
            }
            return root.get(relation).get(idAttribute).in(ids);
        };
    }
}
