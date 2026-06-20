package com.lmalecic.milvshop.viewmodel;

import com.lmalecic.milvshop.dto.TankDto;
import com.lmalecic.milvshop.model.Nation;
import com.lmalecic.milvshop.model.TankRole;
import lombok.Builder;
import lombok.Getter;
import org.jspecify.annotations.Nullable;

import java.util.List;

@Getter
@Builder
public class TankViewModel {
    private final TankDto tank;
    @Nullable
    private final List<Nation> nations;
    @Nullable
    private final List<TankRole> tankRoles;
}
