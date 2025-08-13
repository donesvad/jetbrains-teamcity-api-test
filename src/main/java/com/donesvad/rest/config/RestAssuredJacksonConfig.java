package com.donesvad.rest.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.config.RestAssuredConfig;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class RestAssuredJacksonConfig {

  private final ObjectMapper objectMapper;

  @PostConstruct
  public void init() {
    RestAssured.config =
        RestAssuredConfig.config()
            .objectMapperConfig(
                new ObjectMapperConfig()
                    .jackson2ObjectMapperFactory((cls, charset) -> objectMapper));
  }
}
