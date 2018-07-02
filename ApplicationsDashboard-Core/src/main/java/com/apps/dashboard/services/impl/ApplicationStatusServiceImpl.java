package com.apps.dashboard.services.impl;

import com.apps.dashboard.model.ServiceInfo;
import com.apps.dashboard.repositories.ApplicationStatusRepo;
import com.apps.dashboard.services.ApplicationStatusService;
import java.util.Optional;

public class ApplicationStatusServiceImpl implements ApplicationStatusService {

  private ApplicationStatusRepo applicationStatusRepo;

  @Override
  public void updateApplicationStatus(ServiceInfo serviceInfo) {
    applicationStatusRepo.updateApplicationStatus(serviceInfo);
  }

  @Override
  public Optional<ServiceInfo> getApplicationStatus(String applicationId) {
    return Optional.ofNullable(applicationStatusRepo.getApplicationStatus(applicationId));
  }
}
