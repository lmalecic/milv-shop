package com.lmalecic.milvshop.viewmodel;

import com.lmalecic.milvshop.model.Nation;
import com.lmalecic.milvshop.model.Tank;
import com.lmalecic.milvshop.model.TankRole;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class TankCreateViewModel {
    private final Tank tank;
    private final List<Nation> nations;
    private final List<TankRole> tankRoles;
}
