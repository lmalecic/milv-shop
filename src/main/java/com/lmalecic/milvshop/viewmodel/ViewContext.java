package com.lmalecic.milvshop.viewmodel;

public enum ViewContext {
    CREATE,
    EDIT,
    DELETE,
    VIEW,
    ADMIN;

    public static final String MODEL_ATTRIBUTE_NAME = "viewContext";

    public String getRouteWord() {
        if (this == VIEW || this == ADMIN) {
            return "";
        }

        return this.name().toLowerCase();
    }
}
