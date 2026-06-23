package com.lmalecic.milvshop.dto;

import com.lmalecic.milvshop.validation.UrlOrPath;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.With;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Builder
public record TankRoleDto(
        @With Long id,
        @Length(max = 32) @NotBlank String name,
        @UrlOrPath String imgPath,
        boolean deleted
) implements Displayable {

    public static TankRoleDto empty() {
        return TankRoleDto.builder().build();
    }

    @Override
    public String getDisplayName() {
        return this.name;
    }

    @Override
    public String getDisplayableType() {
        return "Tank Role";
    }

    @Override
    public List<DisplayElement> getDisplayElements() {
        return List.of(
                new DisplayElement("Id", this.id.toString()),
                new DisplayElement("Name", this.name)
        );
    }
}
