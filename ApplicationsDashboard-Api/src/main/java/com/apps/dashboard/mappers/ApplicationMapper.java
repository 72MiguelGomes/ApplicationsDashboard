package com.apps.dashboard.mappers;

import com.apps.dashboard.api.model.ApplicationCreate;
import com.apps.dashboard.api.model.ApplicationUpdate;

public class ApplicationMapper {

  public static com.apps.dashboard.model.Application convert(ApplicationCreate application) {
    return com.apps.dashboard.model.Application.builder()
        .name(application.getName())
        .dns(application.getDns())
        .healthEndpoint(application.getHealthEndpoint())
        .build();
  }

  public static com.apps.dashboard.model.Application convert(ApplicationUpdate application) {
    return com.apps.dashboard.model.Application.builder()
        .name(application.getName())
        .dns(application.getDns())
        .healthEndpoint(application.getHealthEndpoint())
        .build();
  }
}