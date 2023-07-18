package org.example.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix="app.product.validation")
@Configuration
public class ProductControllerProperties {
    private String productIdValidation;

    public String getProductIdValidation() {
        return this.productIdValidation;
    }

    public void setProductIdValidation(String productIdValidation) {
        this.productIdValidation = productIdValidation;
    }
}
