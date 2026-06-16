package com.lmalecic.milvshop.config;

import io.github.wimdeblauwe.htmx.spring.boot.mvc.HtmxViewResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;

@Configuration
public class HtmxConfig {

    @Bean
    public ViewResolver htmxViewResolver() {
        HtmxViewResolver resolver = new HtmxViewResolver();
        resolver.setOrder(0);
        return resolver;
    }
}
