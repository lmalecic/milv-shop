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
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TankDto implements Displayable {
    private Long id;

    @Length(max = 46)
    @NotBlank
    private String name;

    @Length(max = 1000)
    private String description;

    @UrlOrPath
    private String imgPath;

    @NotNull(message = "required")
    @Positive
    private BigDecimal price;

    @NotNull(message = "required")
    @PositiveOrZero
    private Integer mainGunCalibre;

    @NotNull(message = "required")
    @PositiveOrZero
    private Integer armorThickness;

    @NotNull(message = "required")
    @PositiveOrZero
    private Integer maxSpeed;

    @NotNull(message = "required")
    @PositiveOrZero
    private Integer crewSize;

    @NotNull(message = "required")
    private Nation nation;

    @NotNull(message = "required")
    private TankRole tankRole;

    @Builder.Default
    private boolean deleted = false;

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
