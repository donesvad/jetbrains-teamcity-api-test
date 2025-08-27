package com.donesvad.scenario;

import com.donesvad.configuration.SpringConfiguration;
import com.donesvad.configuration.TestConfig;
import io.restassured.RestAssured;
import io.restassured.filter.Filter;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import java.util.LinkedList;
import java.util.List;
import lombok.extern.apachecommons.CommonsLog;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;

@CommonsLog
@SpringBootTest(classes = SpringConfiguration.class)
public abstract class BaseTest {

  @Autowired protected TestConfig config;

  @BeforeAll
  public static void setup(@Autowired Environment env) {
    boolean logRequests =
        Boolean.parseBoolean(env.getProperty("log.rest-assured-requests", "false"));
    boolean logResponses =
        Boolean.parseBoolean(env.getProperty("log.rest-assured-responses", "false"));
    boolean logOnValidationFailOnly =
        Boolean.parseBoolean(env.getProperty("log.rest-assured-only-on-fail", "true"));

    List<Filter> filters = new LinkedList<>();

    if (logOnValidationFailOnly) {
      RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    } else {
      if (logRequests) filters.add(new RequestLoggingFilter());
      if (logResponses) filters.add(new ResponseLoggingFilter());
    }

    RestAssured.filters(filters);

    RestAssured.useRelaxedHTTPSValidation();
  }

  @AfterAll
  public static void tearDown() {}

  @BeforeEach
  public void init() {}

  @AfterEach
  public void cleanUp() {}
}
