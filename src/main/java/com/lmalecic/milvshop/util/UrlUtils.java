package com.lmalecic.milvshop.util;

import org.springframework.web.util.UriComponentsBuilder;

import java.lang.reflect.Field;
import java.util.Collection;

public final class UrlUtils {
    private UrlUtils() {}
    public static UriComponentsBuilder fromObject(String path, Object obj) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromPath(path);

        for (Field field : obj.getClass().getDeclaredFields()) {
            field.setAccessible(true);

            try {
                Object value = field.get(obj);
                if (value == null) continue;

                String name = field.getName();

                switch (value) {
                    case String s -> {
                        if (s.isEmpty()) continue;
                        builder.queryParam(name, s);
                    }
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
                System.out.println("Failed to access field: " + field.getName());
            }
        }

        return builder;
    }
}
