package com.apps.dashboard.controllers;

import com.apps.dashboard.api.ApplicationApi;
import com.apps.dashboard.api.model.Application;
import com.apps.dashboard.api.model.ApplicationCreate;
import com.apps.dashboard.api.model.ApplicationUpdate;
import com.apps.dashboard.api.model.ServiceInfo;
import com.apps.dashboard.exceptions.EntityNotFoundException;
import com.apps.dashboard.mappers.ApplicationMapper;
import com.apps.dashboard.services.ApplicationService;
import com.apps.dashboard.services.ApplicationStatusService;
import java.net.URI;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Controller
public class ApplicationController implements ApplicationApi {

  private final ApplicationService applicationService;

  private final ApplicationStatusService applicationStatusService;

  private final ModelMapper modelMapper;

  @Autowired
  public ApplicationController(ApplicationService applicationService,
      ApplicationStatusService applicationStatusService, ModelMapper modelMapper) {
    this.applicationService = applicationService;
    this.applicationStatusService = applicationStatusService;
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
      com.apps.dashboard.model.Application application = this.applicationService
          .getApplicationById(appId);

      return ResponseEntity.ok(this.modelMapper.map(application, Application.class));
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
      com.apps.dashboard.model.Application appToUpdate = ApplicationMapper.convert(application);

      this.applicationService.updateApplication(appId, appToUpdate);

      URI location = ServletUriComponentsBuilder
          .fromCurrentRequest()
          .build()
          .toUri();

      return ResponseEntity.noContent().location(location).build();
    };
  }

  @Override
  public Callable<ResponseEntity<ServiceInfo>> getServiceInfo(@PathVariable("appId") Long appId) {

    return () ->
        this.applicationStatusService
            .getApplicationStatus(appId)
            .map(
                serviceInfo -> ResponseEntity
                    .ok(this.modelMapper.map(serviceInfo, ServiceInfo.class))
            )
            .orElseGet(
                () -> ResponseEntity.notFound().build()
            );
  }

  @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Application Not Found")
  @ExceptionHandler(EntityNotFoundException.class)
  public @ResponseBody
  Callable<ResponseEntity> exceptionHandler(HttpServletRequest httpServletRequest, Exception ex) {
    return () -> ResponseEntity.notFound().build();
  }
}
