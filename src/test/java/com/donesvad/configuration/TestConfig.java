package com.donesvad.configuration;

import io.restassured.RestAssured;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "tc")
public class TestConfig {

  private String baseUrl;
  private String username;
  private String password;

  private String projectId;

  private String dslRepoUrl;
  private String dslRepoBranch;

  private String vcsAuthMethod; // e.g., "PASSWORD" to use a personal access token as password
  private String vcsUsername; // e.g., your VCS username (for GitHub can be your username)
  private String vcsToken; // the personal access token (will be sent as secure:password)

  @PostConstruct
  public void initRestAssured() {
    RestAssured.baseURI = baseUrl;
  }
}
