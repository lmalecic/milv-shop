package com.lmalecic.milvshop.viewmodel;

import org.jspecify.annotations.Nullable;
import org.springframework.web.util.UriComponentsBuilder;

public enum ViewContext {
    CREATE,
    EDIT,
    DELETE,
    VIEW,
    ADMIN;

    public String getRouteWord() {
        if (this == VIEW || this == ADMIN) {
            return "";
        }

        return this.name().toLowerCase();
    }
}
