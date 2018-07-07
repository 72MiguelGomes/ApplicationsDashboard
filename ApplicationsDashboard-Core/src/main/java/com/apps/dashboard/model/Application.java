package com.apps.dashboard.model;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Application {

  private Long id;

  private String name;

  private String dns;

  private String healthEndpoint;

  public Application update(Application application) {
    return Application.builder()
        .id(this.id)
        .name(application.name)
        .dns(application.dns)
        .healthEndpoint(application.healthEndpoint)
        .build();
  }

  public ApplicationBuilder update() {
    return Application.builder()
        .id(this.id)
        .name(this.name)
        .dns(this.dns)
        .healthEndpoint(this.healthEndpoint);
  }

  public String getHealthCheckUrl() {
    return this.dns + this.healthEndpoint;
  }

}
