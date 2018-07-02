package com.apps.dashboard.services.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.apps.dashboard.exceptions.EntityNotFoundException;
import com.apps.dashboard.model.Application;
import com.apps.dashboard.repositories.ApplicationRepo;
import java.util.Optional;
import name.falgout.jeffrey.testing.junit.mockito.MockitoExtension;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

@ExtendWith(MockitoExtension.class)
public class ApplicationServiceImplTest {

  @InjectMocks
  private ApplicationServiceImpl applicationService;

  @Mock
  private ApplicationRepo applicationRepo;

  /**
   * Test createApplication
   */

  @Test
  public void testCreateApplication() {
    final String name = "name";
    final String dns = "https://localhost:9090";

    Application application = Application.builder()
        .name(name)
        .dns(dns)
        .build();

    this.applicationService.createApplication(application);

    ArgumentCaptor<Application> applicationArgumentCaptor = ArgumentCaptor.forClass(Application.class);
    verify(this.applicationRepo).saveOrUpdate(applicationArgumentCaptor.capture());
    Application capturedApplication = applicationArgumentCaptor.getValue();

    assertEquals(name, capturedApplication.getName());
    assertEquals(dns, capturedApplication.getDns());
  }

  /**
   * Test getApplicationById
   */

  @Test
  public void testGetApplicationByIdEmptyId() {

    final String applicationID = StringUtils.EMPTY;

    assertThrows(IllegalArgumentException.class, () -> {
      applicationService.getApplicationById(applicationID);
    });
  }

  @Test
  public void testGetApplicationByIdNotFound() {

    final String applicationID = "1234";

    when(this.applicationRepo.getApplicationById(anyString())).thenReturn(Optional.empty());

    assertThrows(EntityNotFoundException.class, () -> {
      applicationService.getApplicationById(applicationID);
    });
  }

  /**
   * Test updateApplication
   */

  @Test
  public void testUpdateApplication() {

    final String appId = "123";
    final String name = "finalName";
    final String dns = "http://localhost";

    final Application initialApp = Application.builder()
        .id(appId)
        .name("....")
        .dns("....")
        .build();

    final Application newApp = Application.builder()
        .name(name)
        .dns(dns)
        .build();

    when(this.applicationRepo.getApplicationById(eq(appId))).thenReturn(Optional.of(initialApp));


    Application updatedApp = this.applicationService.updateApplication(appId, newApp);

    verify(this.applicationRepo).saveOrUpdate(eq(updatedApp));

    // Just to make sure that a new instance is created
    assertTrue(updatedApp != newApp);

    assertEquals(appId, updatedApp.getId());
    assertEquals(name, updatedApp.getName());
    assertEquals(dns, updatedApp.getDns());
  }


}