package com.donesvad.rest.dto.vcs;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class VersionedSettingsConfigRequest {
  String format;
  String synchronizationMode;
  boolean allowUIEditing;
  boolean storeSecureValuesOutsideVcs;
  boolean portableDsl;
  boolean showSettingsChanges;
  String vcsRootId;
  String buildSettingsMode;
  String importDecision;
}
