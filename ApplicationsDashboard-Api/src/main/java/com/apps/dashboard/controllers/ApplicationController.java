package com.apps.dashboard.controllers;

import com.apps.dashboard.api.ApplicationApi;
import com.apps.dashboard.api.NotFoundException;
import com.apps.dashboard.api.model.Application;
import com.apps.dashboard.api.model.ApplicationCreate;
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
  public Callable<ResponseEntity<List<Application>>> getApplications() throws NotFoundException {
    return () ->
      ResponseEntity.ok(
          this.applicationService.getAllApplications()
              .stream()
              .map(app -> this.modelMapper.map(app, Application.class))
              .collect(Collectors.toList())
      );
  }

  @Override
  public Callable<ResponseEntity<Void>> createApplication(@RequestBody ApplicationCreate application) {

    return () -> {
      com.apps.dashboard.model.Application newApp = ApplicationMapper.convert(application);

      com.apps.dashboard.model.Application createdApp = this.applicationService.createApplication(newApp);

      URI location = ServletUriComponentsBuilder
          .fromCurrentRequest()
          .path("/{id}")
          .buildAndExpand(createdApp.getId())
          .toUri();

      return ResponseEntity.created(location).build();
    };
  }
}
