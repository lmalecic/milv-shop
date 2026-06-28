package com.lmalecic.milvshop;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:milv-shop-test",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "app.admin.username=admin",
        "app.admin.password=test-admin-password",
        "jwt.secret=MDEyMzQ1Njc4OWFiY2RlZjAxMjM0NTY3ODlhYmNkZWY=",
        "jwt.expiration=900000"
})
class MilvShopApplicationTests {

    @Test
    void contextLoads() {
    }

}
