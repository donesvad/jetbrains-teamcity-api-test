package com.donesvad.rest.dto.vcs;

import lombok.Builder;

@Builder
public record VersionedSettingsConfigRequest(
    String format,
    String synchronizationMode,
    boolean allowUIEditing,
    boolean storeSecureValuesOutsideVcs,
    boolean portableDsl,
    boolean showSettingsChanges,
    String vcsRootId,
    String buildSettingsMode,
    String importDecision) {}
