package com.apps.dashboard.model;

import java.util.HashSet;
import java.util.Set;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ApplicationConfig {

  private Long applicationId;

  private Set<String> infoEndpoints;

  public ApplicationConfig update(ApplicationConfig serviceInfo) {
    return ApplicationConfig.builder()
        .applicationId(this.applicationId)
        .infoEndpoints(clone(serviceInfo.infoEndpoints))
        .build();
  }

  public static ApplicationConfig empty(Long applicationId) {
    return ApplicationConfig.builder()
        .applicationId(applicationId)
        .build();
  }

  private <A> Set<A> clone(Set<A> setList) {
    return setList != null ? new HashSet<>(setList) : null;
  }

}
