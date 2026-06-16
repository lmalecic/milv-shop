package com.lmalecic.milvshop.specification;

import com.lmalecic.milvshop.model.Nation;
import com.lmalecic.milvshop.model.Tank;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.List;

public class TankSpecification {

    // Field names
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String DESCRIPTION = "description";
    private static final String NATION = "nation";
    private static final String TANK_ROLE = "tankRole";
    private static final String PRICE = "price";
    private static final String MAIN_GUN_CALIBRE = "mainGunCalibre";
    private static final String ARMOR_THICKNESS = "armorThickness";
    private static final String MAX_SPEED = "maxSpeed";
    private static final String CREW_SIZE = "crewSize";

    private TankSpecification() {}


    public static Specification<Tank> containsNameOrDescription(String nameOrDescription) {
        return (root, query, builder) -> {
            if (nameOrDescription == null || nameOrDescription.isBlank()) {
                return builder.conjunction();
            }
            return builder.or(
                    builder.like(builder.lower(root.get(NAME)), "%" + nameOrDescription.toLowerCase() + "%"),
                    builder.like(builder.lower(root.get(DESCRIPTION)), "%" + nameOrDescription.toLowerCase() + "%"));
        };
    }

    public static Specification<Tank> containsNation(List<Long> nationIds) {
        return (root, query, builder) -> {
            if (nationIds == null || nationIds.isEmpty()) {
                return builder.conjunction();
            }
            return root.get(NATION).get(ID).in(nationIds);
        };
    }

    public static Specification<Tank> containsTankRole(List<Long> tankRoleIds) {
        return (root, query, builder) -> {
            if (tankRoleIds == null || tankRoleIds.isEmpty()) {
                return builder.conjunction();
            }
            return root.get(TANK_ROLE).get(ID).in(tankRoleIds);
        };
    }

    public static Specification<Tank> priceBetween(BigDecimal priceFrom, BigDecimal priceTo) {
        return (root, query, builder) -> {
            if (priceFrom == null && priceTo == null) {
                return builder.conjunction();
            } else if (priceFrom == null) {
                return builder.lessThanOrEqualTo(root.get(PRICE), priceTo);
            } else if (priceTo == null) {
                return builder.greaterThanOrEqualTo(root.get(PRICE), priceFrom);
            } else {
                return builder.between(root.get(PRICE), priceFrom, priceTo);
            }
        };
    }

    public static Specification<Tank> armorThicknessBetween(Integer armorThicknessMin, Integer armorThicknessMax) {
        return (root, query, builder) -> {
            if (armorThicknessMin == null && armorThicknessMax == null) {
                return builder.conjunction();
            } else if (armorThicknessMin == null) {
                return builder.lessThanOrEqualTo(root.get(ARMOR_THICKNESS), armorThicknessMax);
            } else if (armorThicknessMax == null) {
                return builder.greaterThanOrEqualTo(root.get(ARMOR_THICKNESS), armorThicknessMin);
            } else {
                return builder.between(root.get(ARMOR_THICKNESS), armorThicknessMin, armorThicknessMax);
            }
        };
    }

    public static Specification<Tank> mainGunCalibreEquals(Integer mainGunCalibre) {
        return (root, query, builder) -> {
            if (mainGunCalibre == null) {
                return builder.conjunction();
            }
            return root.get(MAIN_GUN_CALIBRE).equalTo(mainGunCalibre);
        };
    }

    public static Specification<Tank> maxSpeedEquals(Integer maxSpeed) {
        return (root, query, builder) -> {
            if (maxSpeed == null) {
                return builder.conjunction();
            }
            return root.get(MAX_SPEED).equalTo(maxSpeed);
        };
    }

    public static Specification<Tank> crewSizeEquals(Integer crewSize) {
        return (root, query, builder) -> {
            if (crewSize == null) {
                return builder.conjunction();
            }
            return root.get(CREW_SIZE).equalTo(crewSize);
        };
    }
}
