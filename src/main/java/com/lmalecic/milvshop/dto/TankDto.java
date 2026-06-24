package com.lmalecic.milvshop.dto;

import com.lmalecic.milvshop.entity.Nation;
import com.lmalecic.milvshop.entity.TankRole;
import com.lmalecic.milvshop.validation.UrlOrPath;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.util.List;

@Builder
public record TankDto(
        @With Long id,
        @Length(max = 46) @NotBlank String name,
        @Length(max = 1000) String description,
        @UrlOrPath String imgPath,
        @NotNull(message = "required") @Positive BigDecimal price,
        @NotNull(message = "required") @PositiveOrZero Integer mainGunCalibre,
        @NotNull(message = "required") @PositiveOrZero Integer armorThickness,
        @NotNull(message = "required") @PositiveOrZero Integer maxSpeed,
        @NotNull(message = "required") @PositiveOrZero Integer crewSize,
        @NotNull(message = "required") Nation nation,
        @NotNull(message = "required") TankRole tankRole,
        boolean deleted
) implements Displayable {

    public TankDto empty() {
        return TankDto.builder().build();
    }

    @Override
    public String getDisplayName() {
        return this.name();
    }

    @Override
    public String getDisplayableType() {
        return "Tank";
    }

    @Override
    public List<DisplayElement> getDisplayElements() {
        return List.of(
                new DisplayElement("Id", this.id.toString()),
                new DisplayElement("Name", this.name),
                new DisplayElement("Nation", this.nation.getName()),
                new DisplayElement("Main Role", this.tankRole.getName())
        );
    }
}
