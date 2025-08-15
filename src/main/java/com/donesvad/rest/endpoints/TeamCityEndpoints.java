package com.donesvad.rest.endpoints;

import lombok.experimental.UtilityClass;

@UtilityClass
public class TeamCityEndpoints {

  public static final String ID_PARAM = "/id:";
  public static final String VERSIONED_SETTINGS = "/versionedSettings";
  public static final String CONFIG = "/config";
  public static final String LOAD_SETTINGS = "/loadSettings";
  public static final String REST = "/app/rest";
  public static final String PROJECTS = REST + "/projects";
  public static final String VCS_ROOTS = REST + "/vcs-roots";
  public static final String BUILD_QUEUE = REST + "/buildQueue";

  public static String projectById(String projectId) {
    return PROJECTS + ID_PARAM + projectId;
  }

  public static String projectVsConfig(String projectId) {
    return PROJECTS + ID_PARAM + projectId + VERSIONED_SETTINGS + CONFIG;
  }

  public static String projectVsLoad(String projectId) {
    return PROJECTS + ID_PARAM + projectId + VERSIONED_SETTINGS + LOAD_SETTINGS;
  }
}
