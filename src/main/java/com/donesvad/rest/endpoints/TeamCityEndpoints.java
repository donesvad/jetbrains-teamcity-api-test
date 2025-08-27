package com.donesvad.rest.endpoints;

import lombok.experimental.UtilityClass;

@UtilityClass
public class TeamCityEndpoints {

  public static final String ID_PARAM = "/id:";
  public static final String VERSIONED_SETTINGS = "/versionedSettings";
  public static final String CONFIG = "/config";
  public static final String LOAD_SETTINGS = "/loadSettings";
  public static final String REST = "/app/rest";
  public static final String BUILD_TYPES = "/buildTypes";
  public static final String PARAMETERS = "/parameters";
  public static final String PROJECTS = REST + "/projects";
  public static final String VCS_ROOTS = REST + "/vcs-roots";

  public static String projectById(String projectId) {
    return PROJECTS + ID_PARAM + projectId;
  }

  public static String projectBuildTypes(String projectId) {
    return projectById(projectId) + BUILD_TYPES;
  }

  public static String projectVsConfig(String projectId) {
    return PROJECTS + ID_PARAM + projectId + VERSIONED_SETTINGS + CONFIG;
  }

  public static String projectVsLoad(String projectId) {
    return PROJECTS + ID_PARAM + projectId + VERSIONED_SETTINGS + LOAD_SETTINGS;
  }

  public static String versionedSettingsStatus(String projectId) {
    return PROJECTS + ID_PARAM + projectId + VERSIONED_SETTINGS + "/status";
  }

  public static String buildTypeById(String buildTypeId) {
    return REST + BUILD_TYPES + ID_PARAM + buildTypeId;
  }

  public static String buildTypeParameters(String buildTypeId) {
    return buildTypeById(buildTypeId) + PARAMETERS;
  }
}
