package com.apps.dashboard.controllers;

import com.apps.dashboard.model.Application;
import com.apps.dashboard.services.ApplicationService;
import java.util.Collection;
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

    model.addAttribute("apps", applications);

    return "applications";
  }

}
