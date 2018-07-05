package com.apps.dashboard.inmemory;

import com.apps.dashboard.model.Application;
import com.apps.dashboard.repositories.ApplicationRepo;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemApplicationRepo implements ApplicationRepo {

  private Map<Long, Application> applicationMap = new HashMap<>();

  @Override
  public void saveOrUpdate(Application application) {
    applicationMap.put(application.getId(), application);
  }

  @Override
  public Optional<Application> getApplicationById(Long id) {
    return Optional.ofNullable(applicationMap.get(id));
  }
}
