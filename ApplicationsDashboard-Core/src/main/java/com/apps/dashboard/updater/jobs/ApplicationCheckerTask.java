package com.apps.dashboard.updater.jobs;

import com.apps.dashboard.updater.scheduler.ApplicationChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ApplicationCheckerTask {

  private static final long INTERVAL = 5000L;

  private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationCheckerTask.class);

  private final ApplicationChecker applicationChecker;

  @Autowired
  public ApplicationCheckerTask(
      ApplicationChecker applicationChecker) {
    this.applicationChecker = applicationChecker;
  }

  //TODO: remove as much spring dependencies as possible from core
  @Scheduled(fixedDelay = INTERVAL)
  public void updateApplications() {
    try {
      LOGGER.debug("[Start] Updating applications info");
      this.applicationChecker.updateApplicationsInfo();
    } finally {
      LOGGER.debug("[FINISHED] Updating applications info");
    }
  }
}
