package com.apps.dashboard.model;

import java.util.HashSet;
import java.util.Set;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ServiceInfo {

  private Long applicationId;

  private boolean healthy;

  private String version;

  private Set<String> infoEndpoints;

  public ServiceInfo update(ServiceInfo serviceInfo) {
    return ServiceInfo.builder()
        .applicationId(this.applicationId)
        .healthy(serviceInfo.healthy)
        .version(serviceInfo.version)
        .infoEndpoints(clone(serviceInfo.infoEndpoints))
        .build();
  }

  public static ServiceInfo empty(Long applicationId) {
    return ServiceInfo.builder()
        .applicationId(applicationId)
        .build();
  }

  private <A> Set<A> clone(Set<A> setList) {
    return setList != null ? new HashSet<>(setList) : null;
  }

}
