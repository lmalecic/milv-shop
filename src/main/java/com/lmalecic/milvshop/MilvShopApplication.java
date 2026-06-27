package com.lmalecic.milvshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class MilvShopApplication {

    public static void main(String[] args) {
        SpringApplication.run(MilvShopApplication.class, args);
    }
}
