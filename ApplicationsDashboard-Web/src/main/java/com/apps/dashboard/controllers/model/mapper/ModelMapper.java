package com.apps.dashboard.controllers.model.mapper;

import com.apps.dashboard.controllers.model.ApplicationModel;
import com.apps.dashboard.model.Application;
import com.apps.dashboard.services.ApplicationStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ModelMapper {

  private final ApplicationStatusService applicationStatusService;

  @Autowired
  public ModelMapper(ApplicationStatusService applicationStatusService) {
    this.applicationStatusService = applicationStatusService;
  }

  public ApplicationModel convertApplication(Application application) {

    ApplicationModel applicationModel = new ApplicationModel();
    applicationModel.setName(application.getName());
    applicationModel.setDns(application.getDns());
    applicationModel.setId(application.getId());
    applicationModel.setHealthEndpoint(application.getHealthEndpoint());

    this.applicationStatusService.getApplicationStatus(application.getId())
        .ifPresent(appStatus -> {
              applicationModel.setHealthy(appStatus.isHealthy());
              applicationModel.setVersion(appStatus.getVersion());
            }
        );

    return applicationModel;
  }

  public Application convertApplication(ApplicationModel applicationModel) {

    return Application.builder()
        .id(applicationModel.getId())
        .name(applicationModel.getName())
        .dns(applicationModel.getDns())
        .healthEndpoint(applicationModel.getHealthEndpoint())
        .build();

  }

}
