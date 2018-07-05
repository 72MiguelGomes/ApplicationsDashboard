package com.apps.dashboard.repositories;

import com.apps.dashboard.model.ServiceInfo;

public interface ApplicationStatusRepo {

  void updateApplicationStatus(ServiceInfo serviceInfo);

  ServiceInfo getApplicationStatus(Long applicationId);

}
