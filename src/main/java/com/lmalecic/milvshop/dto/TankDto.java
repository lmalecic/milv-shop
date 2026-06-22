package com.lmalecic.milvshop.dto;

import com.lmalecic.milvshop.model.Nation;
import com.lmalecic.milvshop.model.TankRole;
import com.lmalecic.milvshop.validation.UrlOrPath;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class TankDto implements Displayable {
    private Long id;

    @Length(max = 46)
    @NotBlank
    private final String name;

    @Length(max = 1000)
    private final String description;

    @UrlOrPath
    private final String imgPath;

    @NotNull(message = "required")
    @Positive
    private final BigDecimal price;

    @NotNull(message = "required")
    @PositiveOrZero
    private final Integer mainGunCalibre;

    @NotNull(message = "required")
    @PositiveOrZero
    private final Integer armorThickness;

    @NotNull(message = "required")
    @PositiveOrZero
    private final Integer maxSpeed;

    @NotNull(message = "required")
    @PositiveOrZero
    private final Integer crewSize;

    @NotNull(message = "required")
    private final Nation nation;

    @NotNull(message = "required")
    private final TankRole tankRole;

    @Builder.Default
    private final boolean deleted = false;

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
