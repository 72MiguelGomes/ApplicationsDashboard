package com.apps.dashboard.inmemory;

import com.apps.dashboard.model.ServiceInfo;
import com.apps.dashboard.repositories.ApplicationStatusRepo;
import java.util.HashMap;
import java.util.Map;

public class InMemApplicationStatusRepo implements ApplicationStatusRepo {

  private Map<Long, ServiceInfo> applicationStatusMap = new HashMap<>();

  @Override
  public void updateApplicationStatus(ServiceInfo serviceInfo) {
    applicationStatusMap.put(serviceInfo.getApplicationId(), serviceInfo);
  }

  @Override
  public ServiceInfo getApplicationStatus(Long applicationId) {
    return this.applicationStatusMap.get(applicationId);
  }
}
