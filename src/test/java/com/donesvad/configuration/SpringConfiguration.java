package com.donesvad.configuration;

import com.donesvad.util.YamlPropertySourceFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;

@Configuration
@ComponentScan(basePackages = {"com.donesvad"})
@PropertySource(
    value = {
      "classpath:application-${environment:docker}.yml",
    },
    factory = YamlPropertySourceFactory.class)
@EnableConfigurationProperties({TestConfig.class})
@ContextConfiguration
public class SpringConfiguration {}
