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

  private final String baseUrl;
  private final String username;
  private final String password;

  private RequestSpecification spec() {
    return new RequestSpecBuilder()
        //        .setBaseUri(baseUrl)
        .setAuth(preemptive().basic(username, password))
        .build();
  }

  public Response getJson(String path) {
    return given(spec()).accept(ContentType.JSON).get(path);
  }

  public Response getPlain(String path) {
    return given(spec()).get(path);
  }

  public Response postJson(String path, Object body) {
    return given(spec())
        .accept(ContentType.JSON)
        .contentType(ContentType.JSON)
        .body(body)
        .post(path);
  }

  public Response postJson(String path) {
    return postJson(path, "{}");
  }

  public Response putJson(String path, Object body) {
    return given(spec())
        .accept(ContentType.JSON)
        .contentType(ContentType.JSON)
        .body(body)
        .put(path);
  }
}
