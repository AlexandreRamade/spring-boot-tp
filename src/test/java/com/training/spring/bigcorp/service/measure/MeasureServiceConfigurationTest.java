package com.training.spring.bigcorp.service.measure;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;


@Configuration
@ComponentScan({"com.training.spring.bigcorp.service.measure", "com.training.spring.bigcorp.config.properties"})
@PropertySource("classpath:application.properties")
@EnableConfigurationProperties
public class MeasureServiceConfigurationTest {
}
