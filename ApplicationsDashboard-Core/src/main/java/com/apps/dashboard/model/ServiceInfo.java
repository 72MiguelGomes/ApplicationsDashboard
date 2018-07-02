package com.apps.dashboard.model;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ServiceInfo {

  private String applicationId;

  private boolean healthy;

  private String version;

}
