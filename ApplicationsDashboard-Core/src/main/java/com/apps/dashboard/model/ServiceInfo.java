package com.apps.dashboard.model;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ServiceInfo {

  private Long applicationId;

  private boolean healthy;

  private String version;

  public ServiceInfo update(ServiceInfo serviceInfo) {
    return ServiceInfo.builder()
        .applicationId(this.applicationId)
        .healthy(serviceInfo.healthy)
        .version(serviceInfo.version)
        .build();
  }

}
