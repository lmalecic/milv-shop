package com.lmalecic.milvshop.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class TanksFilterModel {
    private String searchQuery = null;
    private List<Long> nationIds = null;
    private List<Long> tankRoleIds = null;
}
