package com.donesvad.rest.endpoints;

import lombok.experimental.UtilityClass;

@UtilityClass
public class TeamCityEndpoints {

  public static final String SERVER_INFO = "/app/rest/server";
  public static final String PROJECTS = "/app/rest/projects";
  public static final String VCS_ROOTS = "/app/rest/vcs-roots";
  public static final String BUILD_QUEUE = "/app/rest/buildQueue";

  public static String projectById(String projectId) {
    return "/app/rest/projects/id:" + projectId;
  }

  public static String projectVsConfig(String projectId) {
    return "/app/rest/projects/" + projectId + "/versionedSettings/config";
  }

  public static String projectVsLoad(String projectId) {
    return "/app/rest/projects/" + projectId + "/versionedSettings/loadSettings";
  }

  public static String buildTypeById(String btId) {
    return "/app/rest/buildTypes/id:" + btId;
  }

  public static String buildTypesUnderProject(String projectId) {
    return "/app/rest/buildTypes?locator=affectedProject:(id:" + projectId + ")";
  }

  public static String parametersOfBuildType(String btId) {
    return "/app/rest/buildTypes/id:" + btId + "/parameters";
  }

  public static String buildById(long buildId) {
    return "/app/rest/builds/id:" + buildId;
  }

  public static String lastFinishedForBuildType(String btId, int count) {
    return "/app/rest/builds/?locator=buildType:(id:" + btId + "),state:finished,count:" + count;
  }

  public static String downloadBuildLog(long buildId) {
    return "/downloadBuildLog.html?buildId=" + buildId;
  }

  public static String agentsConnected() {
    return "/app/rest/agents?locator=connected:true";
  }

  public static String agentAuthorizedInfo(long agentId) {
    return "/app/rest/agents/id:" + agentId + "/authorizedInfo";
  }

  public static String agentEnabledInfo(long agentId) {
    return "/app/rest/agents/id:" + agentId + "/enabledInfo";
  }

    public static String setParameterOfBuildType(String btId, String paramName) {
        return "/app/rest/buildTypes/id:" + btId + "/parameters/" + paramName;
    }

}
