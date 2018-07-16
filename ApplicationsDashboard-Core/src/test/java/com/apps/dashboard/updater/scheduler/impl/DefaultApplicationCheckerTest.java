package com.apps.dashboard.updater.scheduler.impl;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.apps.dashboard.model.Application;
import com.apps.dashboard.model.ServiceInfo;
import com.apps.dashboard.services.ApplicationService;
import com.apps.dashboard.services.ApplicationStatusService;
import com.apps.dashboard.updater.requests.ApplicationMonitor;
import java.util.Collections;
import name.falgout.jeffrey.testing.junit.mockito.MockitoExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

@ExtendWith(MockitoExtension.class)
class DefaultApplicationCheckerTest {

  @InjectMocks
  private DefaultApplicationChecker applicationChecker;

  @Mock
  private ApplicationService applicationService;

  @Mock
  private ApplicationStatusService applicationStatusService;

  @Mock
  private ApplicationMonitor applicationMonitor;

  @Test
  public void testUpdateApplicationsInfo() {

    final Long appId = 1234L;

    final Application application = mock(Application.class);
    when(application.getId()).thenReturn(appId);

    final ServiceInfo serviceInfo = mock(ServiceInfo.class);

    when(this.applicationService.getAllApplications())
        .thenReturn(Collections.singletonList(application));

    when(this.applicationMonitor.checkApplicationStatus(eq(application)))
        .thenReturn(serviceInfo);

    this.applicationChecker.updateApplicationsInfo();

    verify(this.applicationService, only()).getAllApplications();
    verify(this.applicationMonitor, only())
        .checkApplicationStatus(eq(application));
    verify(this.applicationStatusService, only())
        .updateApplicationStatus(eq(appId), eq(serviceInfo));
  }

  @Test
  public void testUpdateApplicationsInfoEmptyList() {

    when(this.applicationService.getAllApplications())
        .thenReturn(Collections.emptyList());

    this.applicationChecker.updateApplicationsInfo();

    verify(this.applicationService, only()).getAllApplications();
    verify(this.applicationMonitor, never())
        .checkApplicationStatus(any(Application.class));
    verify(this.applicationStatusService, never())
        .updateApplicationStatus(anyLong(), any(ServiceInfo.class));

  }

}