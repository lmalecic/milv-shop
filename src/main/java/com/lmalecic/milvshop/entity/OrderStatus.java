package com.lmalecic.milvshop.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderStatus {
    PENDING("Pending", "bg-warning"),
    CANCELLED("Cancelled", "bg-danger"),
    COMPLETED("Completed", "bg-success");

    private final String displayName;
    private final String badgeClass;
}
