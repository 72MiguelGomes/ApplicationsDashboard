package com.apps.dashboard.controllers;

import com.apps.dashboard.api.ApplicationApi;
import com.apps.dashboard.api.model.Application;
import com.apps.dashboard.api.model.ApplicationCreate;
import com.apps.dashboard.api.model.ApplicationUpdate;
import com.apps.dashboard.exceptions.EntityNotFoundException;
import com.apps.dashboard.mappers.ApplicationMapper;
import com.apps.dashboard.services.ApplicationService;
import java.net.URI;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Controller
public class ApplicationController implements ApplicationApi {

  private ApplicationService applicationService;

  private ModelMapper modelMapper;

  @Autowired
  public ApplicationController(ApplicationService applicationService,
      ModelMapper modelMapper) {
    this.applicationService = applicationService;
    this.modelMapper = modelMapper;
  }

  @Override
  public Callable<ResponseEntity<List<Application>>> getApplications() {
    return () ->
        ResponseEntity.ok(
            this.applicationService.getAllApplications()
                .stream()
                .map(app -> this.modelMapper.map(app, Application.class))
                .collect(Collectors.toList())
        );
  }

  @Override
  public Callable<ResponseEntity<Application>> getApplication(@PathVariable("appId") Long appId) {
    return () -> {
      try {
        com.apps.dashboard.model.Application application = this.applicationService
            .getApplicationById(appId);

        return ResponseEntity.ok(this.modelMapper.map(application, Application.class));

      } catch (EntityNotFoundException e) {
        //TODO: Change this to use spring MVC Exception Handler
        return ResponseEntity.notFound().build();
      }
    };
  }

  @Override
  public Callable<ResponseEntity<Void>> createApplication(
      @RequestBody ApplicationCreate application) {

    return () -> {
      com.apps.dashboard.model.Application newApp = ApplicationMapper.convert(application);

      com.apps.dashboard.model.Application createdApp = this.applicationService
          .createApplication(newApp);

      URI location = ServletUriComponentsBuilder
          .fromCurrentRequest()
          .path("/{id}")
          .buildAndExpand(createdApp.getId())
          .toUri();

      return ResponseEntity.created(location).build();
    };
  }

  @Override
  public Callable<ResponseEntity<Void>> updateApplication(@PathVariable("appId") Long appId,
      @RequestBody ApplicationUpdate application) {
    return () -> {
      try {
      com.apps.dashboard.model.Application appToUpdate = ApplicationMapper.convert(application);

      com.apps.dashboard.model.Application updatedApp = this.applicationService
          .updateApplication(appId, appToUpdate);

      URI location = ServletUriComponentsBuilder
          .fromCurrentRequest()
          .path("/{id}")
          .buildAndExpand(updatedApp.getId())
          .toUri();

      return ResponseEntity.noContent().location(location).build();
      } catch (EntityNotFoundException e) {
        //TODO: Change this to use spring MVC Exception Handler
        return ResponseEntity.notFound().build();
      }
    };
  }
}
