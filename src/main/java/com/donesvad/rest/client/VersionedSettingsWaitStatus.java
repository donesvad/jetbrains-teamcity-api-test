package com.donesvad.rest.client;

/**
 * Status phrases returned by TeamCity versioned settings status endpoint that we may want to wait
 * for.
 */
public enum VersionedSettingsWaitStatus {
  APPLIED_CHANGES("Changes from VCS are applied to project settings"),
  RUNNING_DSL("Running DSL");

  private final String phrase;

  VersionedSettingsWaitStatus(String phrase) {
    this.phrase = phrase;
  }

  public String phrase() {
    return phrase;
  }
}
