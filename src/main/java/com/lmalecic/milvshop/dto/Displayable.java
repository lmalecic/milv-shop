package com.lmalecic.milvshop.dto;

import java.util.List;

public interface Displayable {
    record DisplayElement(String fieldName, String fieldValue) {
        public String getDisplayName() {
            return this.fieldName + ": " + this.fieldValue;
        }
    }

    default String getDisplayName() { return this.toString(); }
    String getDisplayableType();
    List<DisplayElement> getDisplayElements();
}
