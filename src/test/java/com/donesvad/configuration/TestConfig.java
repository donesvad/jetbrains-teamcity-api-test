package com.donesvad.configuration;

import io.restassured.RestAssured;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
//@Configuration
@ConfigurationProperties(prefix = "tc")
public class TestConfig {

  private String baseUrl;
  private String username;
  private String password;

  private String projectId;
  private String compileBuildTypeId;
  private String publishBuildTypeId;
  private String deletedBuildTypeId;

  private String dslRepoUrl;
  private String dslRepoBranch;

  private String dslBuildTypeFilePath;
  private String roundtripParamName;

  private String gitAuthorName;
  private String gitAuthorEmail;

  private Boolean versionedSettingsServerCanCommit;

    @PostConstruct
    public void initRestAssured() {
      RestAssured.baseURI = baseUrl;
    }
}
