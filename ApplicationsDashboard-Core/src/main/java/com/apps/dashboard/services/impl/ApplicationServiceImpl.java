package com.apps.dashboard.services.impl;

import com.apps.dashboard.exceptions.EntityNotFoundException;
import com.apps.dashboard.model.Application;
import com.apps.dashboard.repositories.ApplicationRepo;
import com.apps.dashboard.services.ApplicationService;
import com.google.common.base.Preconditions;
import java.util.Objects;
import javax.annotation.Nonnull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ApplicationServiceImpl implements ApplicationService {

  private ApplicationRepo applicationRepo;

  @Autowired
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
  public Application getApplicationById(@Nonnull Long id) {

    Preconditions.checkArgument(Objects.nonNull(id), "ApplicationId must not be null");

    return applicationRepo.getApplicationById(id).orElseThrow(EntityNotFoundException::new);
  }

  @Override
  @Nonnull
  public Application updateApplication(@Nonnull Long id, @Nonnull Application application) {

    Application newApplication = getApplicationById(id).update(application);

    applicationRepo.saveOrUpdate(newApplication);

    return newApplication;
  }
}
