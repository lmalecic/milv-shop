package com.lmalecic.milvshop.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
public class StringToLongListConverter implements Converter<String, List<Long>> {
    
    @Override
    public List<Long> convert(String source) {
        if (source == null || source.isEmpty()) {
            return Collections.emptyList();
        }
        return Arrays.stream(source.split(","))
                .map(String::trim)
                .map(Long::parseLong)
                .toList();
    }
}

