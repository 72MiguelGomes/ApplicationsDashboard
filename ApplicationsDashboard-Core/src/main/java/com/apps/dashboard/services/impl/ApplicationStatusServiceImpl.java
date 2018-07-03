package com.apps.dashboard.services.impl;

import com.apps.dashboard.exceptions.EntityNotFoundException;
import com.apps.dashboard.model.ServiceInfo;
import com.apps.dashboard.repositories.ApplicationStatusRepo;
import com.apps.dashboard.services.ApplicationStatusService;
import com.google.common.base.Preconditions;
import java.util.Optional;
import javax.annotation.Nonnull;
import org.apache.commons.lang3.StringUtils;

public class ApplicationStatusServiceImpl implements ApplicationStatusService {

  private ApplicationStatusRepo applicationStatusRepo;

  public ApplicationStatusServiceImpl(
      ApplicationStatusRepo applicationStatusRepo) {
    this.applicationStatusRepo = applicationStatusRepo;
  }

  @Override
  public void updateApplicationStatus(@Nonnull String applicationId, @Nonnull ServiceInfo serviceInfo) {

    ServiceInfo newServiceInfo = getApplicationStatus(applicationId)
        .orElseThrow(EntityNotFoundException::new)
        .update(serviceInfo);

    applicationStatusRepo.updateApplicationStatus(newServiceInfo);
  }

  @Override
  public Optional<ServiceInfo> getApplicationStatus(@Nonnull String applicationId) {
    Preconditions.checkArgument(StringUtils.isNoneBlank(applicationId), "ApplicationId must non blank String");

    return Optional.ofNullable(applicationStatusRepo.getApplicationStatus(applicationId));
  }
}
