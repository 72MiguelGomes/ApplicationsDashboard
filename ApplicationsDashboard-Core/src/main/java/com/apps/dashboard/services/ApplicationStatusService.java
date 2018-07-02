package com.apps.dashboard.services;

import com.apps.dashboard.model.ServiceInfo;
import java.util.Optional;
import javax.annotation.Nonnull;

public interface ApplicationStatusService {

  void updateApplicationStatus(@Nonnull String applicationId, @Nonnull ServiceInfo serviceInfo);

  Optional<ServiceInfo> getApplicationStatus(@Nonnull String applicationId);

}
