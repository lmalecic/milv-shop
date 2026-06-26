package com.lmalecic.milvshop.entity;

import com.lmalecic.milvshop.cart.BadgeAttribute;
import com.lmalecic.milvshop.cart.Purchasable;
import com.lmalecic.milvshop.cart.PurchasableAttributes;
import com.lmalecic.milvshop.cart.PurchasableType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.util.List;

@Data
@Table
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public final class Tank implements Purchasable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(length = 1000)
    private String description;
    private String imgPath;
    @Column(nullable = false)
    private BigDecimal price;
    @Column(nullable = false)
    private Integer mainGunCalibre;
    @Column(nullable = false)
    private Integer armorThickness;
    @Column(nullable = false)
    private Integer maxSpeed;
    @Column(nullable = false)
    private Integer crewSize;
    @Builder.Default
    @ColumnDefault("false")
    @Column(nullable = false)
    private boolean deleted = false;

    @ManyToOne(optional = false)
    @JoinColumn(name = "nation_id", nullable = false)
    private Nation nation;

    @ManyToOne(optional = false)
    @JoinColumn(name = "tank_role_id", nullable = false)
    private TankRole tankRole;

    @Override
    public Long getPurchasableId() {
        return this.id;
    }

    @Override
    public String getPurchasableName() {
        return this.name;
    }

    @Override
    public PurchasableType getPurchasableType() {
        return PurchasableType.TANK;
    }

    @Override
    public BigDecimal getPurchasablePrice() {
        return this.price;
    }

    @Override
    public String getPurchasableImageUrl() {
        return this.imgPath;
    }

    @Override
    public PurchasableAttributes getPurchasableAttributes() {
        return PurchasableAttributes.builder()
                .badges(List.of(
                        new BadgeAttribute(this.nation.getName(), this.nation.getImgPath()),
                        new BadgeAttribute(this.tankRole.getName(), this.tankRole.getImgPath())
                ))
                .build();
    }
}
