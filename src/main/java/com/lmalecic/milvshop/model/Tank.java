package com.lmalecic.milvshop.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.NumberFormat;
import java.util.Locale;

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
    private String name;
    @Column(length = 1000)
    private String description;
    private String imgPath;
    private double price;
    private int mainGunCalibre;
    private int armorThickness;
    private int maxSpeed;
    private int crewSize;

    @ManyToOne
    @JoinColumn(name = "nation_id")
    private Nation nation;

    @ManyToOne
    @JoinColumn(name = "tank_role_id")
    private TankRole tankRole;

    public String getPriceString() {
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.forLanguageTag("hr-HR"));
        numberFormat.setMinimumFractionDigits(2);
        numberFormat.setMaximumFractionDigits(2);
        return numberFormat.format(price) + " €";
    }
}
