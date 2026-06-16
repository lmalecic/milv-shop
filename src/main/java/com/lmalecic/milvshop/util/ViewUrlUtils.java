package com.lmalecic.milvshop.util;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@RequiredArgsConstructor
@Component("urlUtils")
public class ViewUrlUtils {

    private final HttpServletRequest request;

    public boolean isActive(String... paths) {
        String uri = this.request.getRequestURI();
        return Arrays.stream(paths)
                .anyMatch(path -> "/".equals(path)
                        ? "/".equals(uri)
                        : uri.startsWith(path));
    }

    public String activeClass(String... paths) {
        return this.isActive(paths) ? "active" : "";
    }
}
