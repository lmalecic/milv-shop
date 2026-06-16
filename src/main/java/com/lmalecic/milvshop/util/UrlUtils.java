package com.lmalecic.milvshop.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.util.UriComponentsBuilder;

import java.lang.reflect.Field;
import java.util.Collection;

public final class UrlUtils {

    private static final Logger logger = LoggerFactory.getLogger(UrlUtils.class);

    private UrlUtils() {}

    public static UriComponentsBuilder fromObject(String path, Object obj) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromPath(path);

        for (Field field : obj.getClass().getDeclaredFields()) {
            if (field.trySetAccessible()) {
                tryFieldToQueryParam(obj, field, builder);
            }
        }

        return builder;
    }

    private static void tryFieldToQueryParam(Object obj, Field field, UriComponentsBuilder builder) {
        try {
            Object value = field.get(obj);

            if (value == null || value instanceof String valueStr && valueStr.isEmpty()) return;

            String name = field.getName();

            switch (value) {
                case String s -> builder.queryParam(name, s);
                case Collection<?> col -> {
                    for (Object item : col) {
                        if (item != null) {
                            builder.queryParam(name, item);
                        }
                    }
                }
                default -> builder.queryParam(name, value);
            }
        } catch (IllegalAccessException _) {
            logger.warn("Failed to access field: {}", field.getName());
        }
    }
}
