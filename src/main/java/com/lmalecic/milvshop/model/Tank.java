package com.lmalecic.milvshop.model;

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
public final class Tank {
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
}
