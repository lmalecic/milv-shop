package com.lmalecic.milvshop.entity;

import com.lmalecic.milvshop.cart.Purchasable;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;

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
    @JoinColumn(name = "nation_id")
    private Nation nation;

    @ManyToOne(optional = false)
    @JoinColumn(name = "tank_role_id")
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
    public String getPurchasableType() {
        return "Tank";
    }

    @Override
    public BigDecimal getPurchasablePrice() {
        return this.price;
    }

    @Override
    public String getPurchasableImageUrl() {
        return this.imgPath;
    }
}
