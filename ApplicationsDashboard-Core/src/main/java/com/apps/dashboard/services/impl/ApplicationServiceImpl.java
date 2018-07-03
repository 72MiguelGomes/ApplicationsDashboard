package com.apps.dashboard.services.impl;

import com.apps.dashboard.exceptions.EntityNotFoundException;
import com.apps.dashboard.model.Application;
import com.apps.dashboard.repositories.ApplicationRepo;
import com.apps.dashboard.services.ApplicationService;
import com.google.common.base.Preconditions;
import javax.annotation.Nonnull;
import org.apache.commons.lang3.StringUtils;

public class ApplicationServiceImpl implements ApplicationService {

  private ApplicationRepo applicationRepo;

  public ApplicationServiceImpl(ApplicationRepo applicationRepo) {
    this.applicationRepo = applicationRepo;
  }

  @Override
  public void createApplication(@Nonnull Application application) {

    // TODO: Validate Application

    applicationRepo.saveOrUpdate(application);
  }

  @Override
  @Nonnull
  public Application getApplicationById(@Nonnull String id) {

    Preconditions.checkArgument(StringUtils.isNoneBlank(id), "ApplicationId must non blank String");

    return applicationRepo.getApplicationById(id).orElseThrow(EntityNotFoundException::new);
  }

  @Override
  @Nonnull
  public Application updateApplication(@Nonnull String id, @Nonnull Application application) {

    Application newApplication = getApplicationById(id).update(application);

    applicationRepo.saveOrUpdate(newApplication);

    return newApplication;
  }
}
