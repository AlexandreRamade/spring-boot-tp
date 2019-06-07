package com.training.spring.bigcorp.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "bigcorp")
public class BigCorpApplicationMeasureProperties {
    private int defaultFixed;
    private int defaultSimulated;
    private int defaultReal;


    public int getDefaultFixed() {
        return defaultFixed;
    }

    public void setDefaultFixed(int defaultFixed) {
        this.defaultFixed = defaultFixed;
    }

    public int getDefaultSimulated() {
        return defaultSimulated;
    }

    public void setDefaultSimulated(int defaultSimulated) {
        this.defaultSimulated = defaultSimulated;
    }

    public int getDefaultReal() {
        return defaultReal;
    }

    public void setDefaultReal(int defaultReal) {
        this.defaultReal = defaultReal;
    }
}
