package com.lmalecic.milvshop.component;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@RequiredArgsConstructor
@Component("viewUtils")
public class ViewUtils {

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

    public <T> boolean isOptionSelected(T option, T selected) {
        return option.equals(selected);
    }
}
