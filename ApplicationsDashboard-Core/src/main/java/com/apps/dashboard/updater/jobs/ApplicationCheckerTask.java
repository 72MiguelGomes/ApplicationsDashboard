package com.apps.dashboard.updater.jobs;

import com.apps.dashboard.updater.scheduler.ApplicationChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ApplicationCheckerTask {

  private ApplicationChecker applicationChecker;

  @Autowired
  public ApplicationCheckerTask(
      ApplicationChecker applicationChecker) {
    this.applicationChecker = applicationChecker;
  }

  //TODO: remove as much spring dependencies as possible from core
  @Scheduled(fixedDelay = 5000)
  public void updateApplications() {
    this.applicationChecker.updateApplicationsInfo();
  }
}
