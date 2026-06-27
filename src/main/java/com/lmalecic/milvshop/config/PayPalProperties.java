package com.lmalecic.milvshop.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "paypal")
public class PayPalProperties {

    private boolean enabled = false;
    private String clientId = "";
    private String clientSecret = "";
    private Mode mode = Mode.SANDBOX;
    private String currency = "EUR";
    private String brandName = "Milv Shop";

    public boolean isConfigured() {
        return this.enabled
                && this.clientId != null
                && !this.clientId.isBlank()
                && this.clientSecret != null
                && !this.clientSecret.isBlank();
    }

    public enum Mode {
        SANDBOX,
        LIVE
    }
}
