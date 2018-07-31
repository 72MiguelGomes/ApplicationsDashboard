package com.apps.dashboard.controllers.model.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import com.apps.dashboard.controllers.model.ApplicationModel;
import com.apps.dashboard.model.Application;
import com.apps.dashboard.model.ServiceInfo;
import com.apps.dashboard.services.ApplicationStatusService;
import java.util.Optional;
import name.falgout.jeffrey.testing.junit.mockito.MockitoExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

@ExtendWith(MockitoExtension.class)
class ModelMapperTest {

  @InjectMocks
  private ModelMapper modelMapper;

  @Mock
  private ApplicationStatusService applicationStatusService;

  /**
   * Test convertApplication
   */
  @Test
  public void testConvertApplicationWithoutAppStatus() {
    final Long id = 123L;
    final String applicationName = "appName";
    final String dns = "https:localhost.com";
    final String healthCheckEndpoint = "/ping";

    final Application application = Application.builder()
        .id(id)
        .name(applicationName)
        .dns(dns)
        .healthEndpoint(healthCheckEndpoint)
        .build();

    when(this.applicationStatusService.getApplicationStatus(eq(id)))
        .thenReturn(Optional.empty());

    ApplicationModel applicationModel = this.modelMapper.convertApplication(application);

    assertEquals(id, applicationModel.getId());
    assertEquals(applicationName, applicationModel.getName());
    assertEquals(dns, applicationModel.getDns());
    assertEquals(healthCheckEndpoint, applicationModel.getHealthEndpoint());
    assertNull(applicationModel.getVersion());
    assertFalse(applicationModel.isHealthy());
  }

  @Test
  public void testConvertApplicationWithAppStatus() {
    final Long id = 123L;
    final String applicationName = "appName";
    final String dns = "https:localhost.com";
    final String healthCheckEndpoint = "/ping";

    final boolean healthy = true;
    final String version = "v1";

    final ServiceInfo serviceInfo = ServiceInfo.builder()
        .applicationId(id)
        .healthy(healthy)
        .version(version)
        .build();

    final Application application = Application.builder()
        .id(id)
        .name(applicationName)
        .dns(dns)
        .healthEndpoint(healthCheckEndpoint)
        .build();

    when(this.applicationStatusService.getApplicationStatus(eq(id)))
        .thenReturn(Optional.of(serviceInfo));

    ApplicationModel applicationModel = this.modelMapper.convertApplication(application);

    assertEquals(id, applicationModel.getId());
    assertEquals(applicationName, applicationModel.getName());
    assertEquals(dns, applicationModel.getDns());
    assertEquals(healthCheckEndpoint, applicationModel.getHealthEndpoint());
    assertEquals(healthy, applicationModel.isHealthy());
    assertEquals(version, applicationModel.getVersion());
  }

  /**
   * Test convertApplication
   */
  @Test
  public void testConvertApplicationModel() {
    final Long id = 123L;
    final String applicationName = "appName";
    final String dns = "https:localhost.com";
    final String healthCheckEndpoint = "/ping";

    final ApplicationModel applicationModel = new ApplicationModel();
    applicationModel.setId(id);
    applicationModel.setName(applicationName);
    applicationModel.setDns(dns);
    applicationModel.setHealthEndpoint(healthCheckEndpoint);

    Application app = modelMapper.convertApplication(applicationModel);

    assertEquals(id, app.getId());
    assertEquals(applicationName, app.getName());
    assertEquals(dns, app.getDns());
    assertEquals(healthCheckEndpoint, app.getHealthEndpoint());
  }

}