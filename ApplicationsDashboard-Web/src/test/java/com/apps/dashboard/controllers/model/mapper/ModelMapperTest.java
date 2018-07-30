package com.apps.dashboard.controllers.model.mapper;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import com.apps.dashboard.controllers.model.ApplicationModel;
import com.apps.dashboard.model.Application;
import com.apps.dashboard.services.ApplicationStatusService;
import java.util.Optional;
import name.falgout.jeffrey.testing.junit.mockito.MockitoExtension;
import org.junit.jupiter.api.Assertions;
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

    Assertions.assertEquals(id, applicationModel.getId());
    Assertions.assertEquals(applicationName, applicationModel.getName());
    Assertions.assertEquals(dns, applicationModel.getDns());
    Assertions.assertEquals(healthCheckEndpoint, applicationModel.getHealthEndpoint());
    Assertions.assertNull(applicationModel.getVersion());
    Assertions.assertFalse(applicationModel.isHealthy());
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

    Assertions.assertEquals(id, app.getId());
    Assertions.assertEquals(applicationName, app.getName());
    Assertions.assertEquals(dns, app.getDns());
    Assertions.assertEquals(healthCheckEndpoint, app.getHealthEndpoint());
  }

}