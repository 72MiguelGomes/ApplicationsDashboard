package com.apps.dashboard.controllers;

import com.apps.dashboard.model.Application;
import com.apps.dashboard.services.ApplicationService;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ApplicationController {

  private ApplicationService applicationService;

  @Autowired
  public ApplicationController(ApplicationService applicationService) {
    this.applicationService = applicationService;
  }

  @GetMapping("/applications")
  public String getApplications(Model model) {

    Collection<Application> applications = this.applicationService.getAllApplications();

    Application.builder()
        .name("1")
        .build();

    //model.addAllAttributes(applications);
    List<Application> apps = Arrays.asList(
        Application.builder()
            .name("app1")
            .build(),
        Application.builder()
            .name("app2")
            .build()
    );


    model.addAttribute("apps", apps);

    return "applications";
  }

}
