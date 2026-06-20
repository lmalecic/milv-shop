package com.lmalecic.milvshop;

import org.jspecify.annotations.Nullable;
import org.springframework.web.util.UriComponentsBuilder;

public enum ViewContext {
    CREATE,
    EDIT,
    DELETE,
    VIEW,
    ADMIN;

    public String getRoute(@Nullable Long id) {
        String path = switch (this) {
            case CREATE -> "create";
            case EDIT -> "edit";
            case DELETE -> "delete";
            case VIEW, ADMIN -> "";
        };

        return UriComponentsBuilder.fromPath(path).pathSegment(id != null ? id.toString() : "").toUriString();
    }
}
