package com.donesvad.rest.client;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.preemptive;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ApiClient {

  private final String username;
  private final String password;

  private RequestSpecification spec() {
    return new RequestSpecBuilder()
        .setAuth(preemptive().basic(username, password))
        .setAccept(ContentType.JSON)
        .setContentType(ContentType.JSON)
        .build();
  }

  public Response get(String path) {
    return given(spec()).get(path);
  }

  public Response post(String path, Object body) {
    return given(spec()).body(body).post(path);
  }

  public Response post(String path) {
    return post(path, "{}");
  }

  public Response put(String path, Object body) {
    return given(spec()).body(body).put(path);
  }

  public Response delete(String path) {
    return given(spec()).delete(path);
  }
}
