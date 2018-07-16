package com.apps.dashboard.updater.scheduler.impl;

import com.apps.dashboard.model.Application;
import com.apps.dashboard.model.ServiceInfo;
import com.apps.dashboard.services.ApplicationService;
import com.apps.dashboard.services.ApplicationStatusService;
import com.apps.dashboard.updater.requests.ApplicationMonitor;
import com.apps.dashboard.updater.scheduler.ApplicationChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DefaultApplicationCheker implements ApplicationChecker {

  private final ApplicationService applicationService;

  private final ApplicationStatusService applicationStatusService;

  private final ApplicationMonitor applicationMonitor;

  @Autowired
  public DefaultApplicationCheker(ApplicationService applicationService,
      ApplicationStatusService applicationStatusService,
      ApplicationMonitor applicationMonitor) {
    this.applicationService = applicationService;
    this.applicationStatusService = applicationStatusService;
    this.applicationMonitor = applicationMonitor;
  }

  @Override
  public void updateApplicationsInfo() {

    this.applicationService.getAllApplications()
        .parallelStream()
        .forEach(this::updateApplicationStatus);
  }

  private void updateApplicationStatus(Application application) {
    ServiceInfo serviceInfo = this.applicationMonitor.checkApplicationStatus(application);

    this.applicationStatusService.updateApplicationStatus(application.getId(), serviceInfo);
  }
}
