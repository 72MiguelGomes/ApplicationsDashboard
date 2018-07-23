package com.apps.dashboard.controllers;

import com.apps.dashboard.model.Application;
import com.apps.dashboard.model.ServiceInfo;
import com.apps.dashboard.services.ApplicationService;
import com.apps.dashboard.services.ApplicationStatusService;
import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ApplicationController {

  private final ApplicationService applicationService;

  private final ApplicationStatusService applicationStatusService;

  @Autowired
  public ApplicationController(ApplicationService applicationService,
      ApplicationStatusService applicationStatusService) {
    this.applicationService = applicationService;
    this.applicationStatusService = applicationStatusService;
  }

  @GetMapping("/application")
  public String getApplications(Model model) {

    Collection<Application> applications = this.applicationService.getAllApplications();

    Map<Long, ServiceInfo> serviceInfoMap = applications.stream()
        .map(app -> this.applicationStatusService.getApplicationStatus(app.getId())
            .orElseGet(() -> this.createEmptyServiceInfo(app)))
        .collect(Collectors.toMap(ServiceInfo::getApplicationId, Function.identity()));

    model.addAttribute("apps", applications);
    model.addAttribute("serviceInfoMap", serviceInfoMap);

    return "applications";
  }

  @GetMapping("/application/{appId}")
  public String getApplication(@PathVariable("appId") Long id, Model model) {

    final Application application = this.applicationService.getApplicationById(id);

    final ServiceInfo serviceInfo = this.applicationStatusService.getApplicationStatus(id)
        .orElseGet(() -> this.createEmptyServiceInfo(application));

    model.addAttribute("app", application);
    model.addAttribute("serviceInfo", serviceInfo);

    return "application";
  }

  private ServiceInfo createEmptyServiceInfo(Application app) {
    return ServiceInfo.builder()
        .applicationId(app.getId())
        .healthy(app.getId() % 2 == 0) //TODO: This is an hack to test app status
        .build();
  }

}
