package com.apps.dashboard.controllers;

import com.apps.dashboard.api.ApplicationApi;
import com.apps.dashboard.api.NotFoundException;
import com.apps.dashboard.api.model.Application;
import com.apps.dashboard.api.model.ApplicationCreate;
import com.apps.dashboard.services.ApplicationService;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

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
  public Callable<ResponseEntity<Void>> createApplication(ApplicationCreate application)
      throws NotFoundException {
    return null;
  }
}
