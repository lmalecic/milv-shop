package com.lmalecic.milvshop.dto;

import com.lmalecic.milvshop.model.Nation;
import com.lmalecic.milvshop.model.TankRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TankDto {
    private Long id;
    @NotBlank(message = "Name is required and can't be blank!")
    private String name;
    @Length(max = 1000)
    private String description;
    @URL(message = "Image path must be a valid URL!")
    private String imgPath;
    @NotNull
    @Positive(message = "Price must be a positive non-zero number!")
    private BigDecimal price;
    @NotNull
    @PositiveOrZero(message = "Main gun calibre must be a positive number or zero!")
    private Integer mainGunCalibre;
    @NotNull
    @PositiveOrZero(message = "Armor thickness must be a positive number or zero!")
    private Integer armorThickness;
    @NotNull
    @PositiveOrZero(message = "Max speed must be a positive number or zero!")
    private Integer maxSpeed;
    @NotNull
    private Integer crewSize;
    @NotNull
    private Nation nation;
    @NotNull
    private TankRole tankRole;
    @Builder.Default
    private boolean deleted = false;
}
