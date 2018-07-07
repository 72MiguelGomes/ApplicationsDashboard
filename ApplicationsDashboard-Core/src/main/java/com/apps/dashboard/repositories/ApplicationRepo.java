package com.apps.dashboard.repositories;

import com.apps.dashboard.model.Application;
import java.util.Collection;
import java.util.Optional;

public interface ApplicationRepo {

  void saveOrUpdate(Application application);

  Optional<Application> getApplicationById(Long id);

  Collection<Application> getAllApplications();

}
