package com.apps.dashboard.controllers;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.apps.dashboard.api.model.ApplicationCreate;
import com.apps.dashboard.api.model.ApplicationUpdate;
import com.apps.dashboard.exceptions.EntityNotFoundException;
import com.apps.dashboard.model.Application;
import com.apps.dashboard.model.ServiceInfo;
import com.apps.dashboard.services.ApplicationService;
import com.apps.dashboard.services.ApplicationStatusService;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.ws.rs.core.MediaType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.stubbing.Answer;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ApplicationController.class)
public class ApplicationControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private ApplicationService applicationService;

  @MockBean
  private ApplicationStatusService applicationStatusService;

  /**
   * Test getApplications
   */
  @Test
  public void testGetApplications() throws Exception {

    final Set<Long> applicationIds = Sets.newHashSet(1L, 2L, 3L);
    final String name = "AppName";
    final String dns = "http://localhost";
    final String healthCheck = "/ping";

    List<Application> applicationList = createDummyApps(applicationIds,
        name,
        dns,
        healthCheck);

    when(applicationService.getAllApplications())
        .thenReturn(applicationList);

    MvcResult mvcResult = mockMvc.perform(get("/application")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(request().asyncStarted())
        .andReturn();

    this.mockMvc.perform(asyncDispatch(mvcResult))
        .andExpect(status().isOk());

    String jsonResult = mvcResult.getResponse().getContentAsString();

    List<Application> result = new Gson().fromJson(jsonResult, new TypeToken<List<Application>>(){}.getType());

    assertEquals(applicationIds.size(), result.size());

    result.forEach(app -> {
              assertTrue(applicationIds.contains(app.getId()));
              assertEquals(name, app.getName());
              assertEquals(dns, app.getDns());
              assertEquals(healthCheck, app.getHealthEndpoint());
            });
  }

  private List<Application> createDummyApps(Set<Long> applicationIds,
      String name,
      String dns,
      String healthCheck) {

    return applicationIds.stream()
        .map(appId ->
          Application.builder()
              .id(appId)
              .name(name)
              .dns(dns)
              .healthEndpoint(healthCheck)
              .build()
        )
        .collect(Collectors.toList());
  }

  /**
   * Test createApplication
   */
  @Test
  public void testCreateApplication() throws Exception {

    final String name = "AppName";
    final String dns = "http://localhost";
    final String healthCheck = "/ping";
    final Long appId = 123L;

    when(this.applicationService
        .createApplication(any(com.apps.dashboard.model.Application.class)))
        .then((Answer<Application>) invocationOnMock -> {

          Application application = invocationOnMock.getArgument(0);

          assertEquals(name, application.getName());
          assertEquals(dns, application.getDns());
          assertEquals(healthCheck, application.getHealthEndpoint());
          assertNull(application.getId());

          return application.update()
              .id(appId)
              .build();
        });

    ApplicationCreate applicationCreate = new ApplicationCreate();
    applicationCreate.setName(name);
    applicationCreate.setDns(dns);
    applicationCreate.setHealthEndpoint(healthCheck);

    MvcResult mvcResult = mockMvc.perform(post("/application")
        .contentType(MediaType.APPLICATION_JSON)
        .content(new Gson().toJson(applicationCreate)))
        .andExpect(request().asyncStarted())
        .andReturn();

    this.mockMvc.perform(asyncDispatch(mvcResult))
        .andExpect(status().isCreated())
        .andExpect(header()
            .string("Location", "http://localhost/application/" + appId));
  }

  /**
   * Test getApplication
   */
  @Test
  public void testGetApplication() throws Exception {
    final String name = "AppName";
    final String dns = "http://localhost";
    final String healthCheck = "/ping";
    final Long appId = 12345L;

    Application application = Application.builder()
        .id(appId)
        .name(name)
        .dns(dns)
        .healthEndpoint(healthCheck)
        .build();

    when(applicationService.getApplicationById(eq(appId)))
        .thenReturn(application);

    MvcResult mvcResult = mockMvc.perform(get("/application/" + appId)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(request().asyncStarted())
        .andReturn();

    this.mockMvc.perform(asyncDispatch(mvcResult))
        .andExpect(status().isOk());

    String jsonResult = mvcResult.getResponse().getContentAsString();

    com.apps.dashboard.api.model.Application result = new Gson().fromJson(jsonResult, com.apps.dashboard.api.model.Application.class);

    assertEquals(appId.toString(), result.getId());
    assertEquals(name, result.getName());
    assertEquals(dns, result.getDns());
    assertEquals(healthCheck, result.getHealthEndpoint());
  }

  @Test
  public void testGetApplicationNotFound() throws Exception {

    final Long appId = 10L;

    when(this.applicationService.getApplicationById(eq(appId)))
        .thenThrow(EntityNotFoundException.class);

    MvcResult mvcResult = mockMvc.perform(get("/application/" + appId)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(request().asyncStarted())
        .andReturn();

    this.mockMvc.perform(asyncDispatch(mvcResult))
        .andExpect(status().isNotFound());
  }

  /**
   * Test updateApplication
   */
  @Test
  public void testUpdateApplication() throws Exception {

    final String name = "AppName";
    final String dns = "http://localhost";
    final String healthCheck = "/ping";
    final Long appId = 123L;

    when(this.applicationService
        .updateApplication(eq(appId), any(com.apps.dashboard.model.Application.class)))
        .then((Answer<Application>) invocationOnMock -> {

          Application application = invocationOnMock.getArgument(1);

          assertEquals(name, application.getName());
          assertEquals(dns, application.getDns());
          assertEquals(healthCheck, application.getHealthEndpoint());
          assertNull(application.getId());

          return application.update()
              .id(appId)
              .build();
        });

    ApplicationUpdate applicationUpdate = new ApplicationUpdate();
    applicationUpdate.setName(name);
    applicationUpdate.setDns(dns);
    applicationUpdate.setHealthEndpoint(healthCheck);

    MvcResult mvcResult = mockMvc.perform(put("/application/" + appId)
        .contentType(MediaType.APPLICATION_JSON)
        .content(new Gson().toJson(applicationUpdate)))
        .andExpect(request().asyncStarted())
        .andReturn();

    this.mockMvc.perform(asyncDispatch(mvcResult))
        .andExpect(status().isNoContent())
        .andExpect(header()
            .string("Location", "http://localhost/application/" + appId));

  }

  @Test
  public void testUpdateApplicationThatNotExist() throws Exception {

    final String name = "AppName";
    final String dns = "http://localhost";
    final String healthCheck = "/ping";
    final Long appId = 123L;

    when(this.applicationService
        .updateApplication(eq(appId), any(com.apps.dashboard.model.Application.class)))
        .thenThrow(EntityNotFoundException.class);

    ApplicationUpdate applicationUpdate = new ApplicationUpdate();
    applicationUpdate.setName(name);
    applicationUpdate.setDns(dns);
    applicationUpdate.setHealthEndpoint(healthCheck);

    MvcResult mvcResult = mockMvc.perform(put("/application/" + appId)
        .contentType(MediaType.APPLICATION_JSON)
        .content(new Gson().toJson(applicationUpdate)))
        .andExpect(request().asyncStarted())
        .andReturn();

    this.mockMvc.perform(asyncDispatch(mvcResult))
        .andExpect(status().isNotFound());
  }

  /**
   * Test getServiceInfo
   */
  @Test
  public void testGetServiceInfo() throws Exception {

    final Long appId = 123L;
    final boolean healthy = false;
    final String version = "v2";

    ServiceInfo serviceInfo = ServiceInfo.builder()
        .applicationId(appId)
        .healthy(healthy)
        .version(version)
        .build();

    when(this.applicationStatusService.getApplicationStatus(eq(appId)))
        .thenReturn(Optional.of(serviceInfo));

    MvcResult mvcResult = mockMvc.perform(get("/application/" + appId + "/serviceInfo")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(request().asyncStarted())
        .andReturn();

    this.mockMvc.perform(asyncDispatch(mvcResult))
        .andExpect(status().isOk());

    String jsonResult = mvcResult.getResponse().getContentAsString();

    com.apps.dashboard.api.model.ServiceInfo result = new Gson().fromJson(jsonResult, com.apps.dashboard.api.model.ServiceInfo.class);

    assertEquals(healthy, result.getHealthy());
    assertEquals(version, result.getVersion());
  }

  @Test
  public void testGetServiceInfoNotFound() throws Exception {

    final Long appId = 10L;

    when(this.applicationStatusService.getApplicationStatus(eq(appId)))
        .thenReturn(Optional.empty());

    MvcResult mvcResult = mockMvc.perform(get("/application/" + appId + "/serviceInfo")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(request().asyncStarted())
        .andReturn();

    this.mockMvc.perform(asyncDispatch(mvcResult))
        .andExpect(status().isNotFound());
  }

}
