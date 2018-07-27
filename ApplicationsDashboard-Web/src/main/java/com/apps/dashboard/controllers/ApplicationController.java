package com.apps.dashboard.controllers;

import com.apps.dashboard.controllers.model.ApplicationModel;
import com.apps.dashboard.model.Application;
import com.apps.dashboard.services.ApplicationService;
import com.apps.dashboard.services.ApplicationStatusService;
import java.util.Collection;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

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

    Collection<ApplicationModel> applications = this.applicationService.getAllApplications()
        .stream()
        .map(this::convertApplication)
        .collect(Collectors.toList());

    model.addAttribute("apps", applications);

    return "applications";
  }

  @GetMapping("/application/create")
  public String getCreateApplication(Model model) {

    model.addAttribute("application", new ApplicationModel());

    return "application_create";
  }

  @PostMapping("/application/create")
  public ModelAndView createApplication(@ModelAttribute ApplicationModel applicationModel, Model model) {

    Application application = this.convertApplication(applicationModel);

    this.applicationService.createApplication(application);

    return new ModelAndView("redirect:/application");
  }

  @GetMapping("/application/{appId}")
  public String getApplication(@PathVariable("appId") Long id, Model model) {

    final ApplicationModel application = convertApplication(
        this.applicationService.getApplicationById(id));

    model.addAttribute("app", application);

    return "application";
  }

  @GetMapping("/application/{appId}/update")
  public String getApplicationToUpdate(@PathVariable("appId") Long id, Model model) {

    final ApplicationModel application = convertApplication(
        this.applicationService.getApplicationById(id));

    model.addAttribute("app", application);

    return "application_update";
  }

  @PostMapping("/application/{appId}/update")
  public ModelAndView updateApplication(@PathVariable("appId") Long id, @ModelAttribute ApplicationModel applicationModel, Model model) {

    Application application = this.convertApplication(applicationModel);

    this.applicationService.updateApplication(id, application);

    return new ModelAndView("redirect:/application");
  }

  private ApplicationModel convertApplication(Application application) {

    ApplicationModel applicationModel = new ApplicationModel();
    applicationModel.setName(application.getName());
    applicationModel.setDns(application.getDns());
    applicationModel.setId(application.getId());

    this.applicationStatusService.getApplicationStatus(application.getId())
        .ifPresent(appStatus -> {
              applicationModel.setHealthy(appStatus.isHealthy());
              applicationModel.setVersion(appStatus.getVersion());
            }
        );

    return applicationModel;
  }

  private Application convertApplication(ApplicationModel applicationModel) {

    return Application.builder()
        .id(applicationModel.getId())
        .name(applicationModel.getName())
        .dns(applicationModel.getDns())
        .healthEndpoint(applicationModel.getHealthEndpoint())
        .build();

  }

}
