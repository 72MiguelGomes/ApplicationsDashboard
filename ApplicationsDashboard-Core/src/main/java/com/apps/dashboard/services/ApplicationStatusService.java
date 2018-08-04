package com.apps.dashboard.services;

import com.apps.dashboard.model.ServiceInfo;
import java.util.Map;
import java.util.Optional;
import javax.annotation.Nonnull;

public interface ApplicationStatusService {

  void updateApplicationStatus(@Nonnull Long applicationId, @Nonnull ServiceInfo serviceInfo);

  Optional<ServiceInfo> getApplicationStatus(@Nonnull Long applicationId);

  @Nonnull
  Map<String, String> getEndpointInfo(Long applicationId);

}
