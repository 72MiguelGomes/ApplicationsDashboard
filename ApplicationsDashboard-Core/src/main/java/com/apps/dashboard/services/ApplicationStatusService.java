package com.apps.dashboard.services;

import com.apps.dashboard.model.Application;
import com.apps.dashboard.model.EndpointInfo;
import com.apps.dashboard.model.ServiceInfo;
import java.util.Collection;
import java.util.Optional;
import javax.annotation.Nonnull;

public interface ApplicationStatusService {

  void updateApplicationStatus(@Nonnull Long applicationId, @Nonnull ServiceInfo serviceInfo);

  Optional<ServiceInfo> getApplicationStatus(@Nonnull Long applicationId);

  @Nonnull
  Collection<EndpointInfo> getEndpointsInfo(@Nonnull Application application);

}
