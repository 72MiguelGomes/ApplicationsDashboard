package com.apps.dashboard.services.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.apps.dashboard.exceptions.EntityNotFoundException;
import com.apps.dashboard.model.Application;
import com.apps.dashboard.repositories.ApplicationRepo;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

public class ApplicationServiceImplTest {

  private ApplicationRepo applicationRepo;

  private ApplicationServiceImpl applicationService;

  public ApplicationServiceImplTest() {
    this.applicationRepo = mock(ApplicationRepo.class);
    this.applicationService = new ApplicationServiceImpl(this.applicationRepo);
  }

  /**
   * Test createApplication
   */

  @Test
  public void testCreateApplication() {

    final String name = "name";
    final String endpoint = "https://localhost:9090";

    Application application = Application.builder()
        .name(name)
        .endpoint(endpoint)
        .build();

    this.applicationService.createApplication(application);

    ArgumentCaptor<Application> applicationArgumentCaptor = ArgumentCaptor.forClass(Application.class);
    verify(this.applicationRepo).saveOrUpdate(applicationArgumentCaptor.capture());
    Application capturedApplication = applicationArgumentCaptor.getValue();

    assertEquals(name, capturedApplication.getName());
    assertEquals(endpoint, capturedApplication.getEndpoint());
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
    final String endpoint = "http://localhost";

    final Application initialApp = Application.builder()
        .id(appId)
        .name("....")
        .endpoint("....")
        .build();

    final Application newApp = Application.builder()
        .name(name)
        .endpoint(endpoint)
        .build();

    when(this.applicationRepo.getApplicationById(eq(appId))).thenReturn(Optional.of(initialApp));


    Application updatedApp = this.applicationService.updateApplication(appId, newApp);

    verify(this.applicationRepo).saveOrUpdate(eq(updatedApp));

    // Just to make sure that a new instance is created
    assertTrue(updatedApp != newApp);

    assertEquals(appId, updatedApp.getId());
    assertEquals(name, updatedApp.getName());
    assertEquals(endpoint, updatedApp.getEndpoint());
  }


}