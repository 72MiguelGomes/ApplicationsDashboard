package com.apps.dashboard.model;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Application {

  private String id;

  private String name;

  private String endpoint;

  public Application update(Application application) {
    return Application.builder()
        .id(this.id)
        .name(application.name)
        .endpoint(application.endpoint)
        .build();
  }

}
