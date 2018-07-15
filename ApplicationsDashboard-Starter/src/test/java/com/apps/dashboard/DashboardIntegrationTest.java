package com.apps.dashboard;

import com.apps.dashboard.api.model.Application;
import com.apps.dashboard.api.model.ApplicationCreate;
import com.apps.dashboard.api.model.ApplicationUpdate;
import com.google.common.collect.Sets;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = com.apps.dashboard.ApplicationsDashboard.class)
public class DashboardIntegrationTest {

  @Autowired
  private TestRestTemplate restTemplate;

  @Test
  public void happyPath() {

    final String appName = "appName";
    final String dns = "https://localhost:8080";
    final String healthCheckEndpoint = "/ping";

    final String updatedAppName = "appName2";
    final String updatedDns = "https://localhost:8081";
    final String updatedHealthCheckEndpoint = "/ping.html";

    getAllApplicationValidator(Sets.newHashSet());

    String newAppId = createApplication(appName, dns, healthCheckEndpoint);

    validateApplication(appName, dns, healthCheckEndpoint, newAppId);

    updateApplication(newAppId, updatedAppName, updatedDns, updatedHealthCheckEndpoint);

    validateApplication(updatedAppName, updatedDns, updatedHealthCheckEndpoint, newAppId);

    getAllApplicationValidator(Sets.newHashSet(newAppId));
  }

  private void getAllApplicationValidator(final Set<String> appIds) {

    ResponseEntity<Application[]> getAllAppsResp = this.restTemplate
        .getForEntity("/application", Application[].class);

    Assertions.assertNotNull(getAllAppsResp.getBody());

    Application[] applications = getAllAppsResp.getBody();

    Assertions.assertEquals(appIds.size(), applications.length);

    Arrays.stream(applications)
        .forEach(app -> Assertions.assertTrue(appIds.contains(app.getId())));
  }

  private void updateApplication(String newAppId, String updatedAppName, String updatedDns,
      String updatedHealthCheckEndpoint) {
    ApplicationUpdate applicationUpdate = new ApplicationUpdate();
    applicationUpdate.setName(updatedAppName);
    applicationUpdate.setDns(updatedDns);
    applicationUpdate.setHealthEndpoint(updatedHealthCheckEndpoint);

    HttpEntity<ApplicationUpdate> requestEntity = new HttpEntity<>(applicationUpdate);

    ResponseEntity<Void> updateAppResponse = this.restTemplate
        .exchange("/application/" + newAppId, HttpMethod.PUT, requestEntity, Void.class);

    Assertions.assertEquals(HttpStatus.NO_CONTENT, updateAppResponse.getStatusCode());
  }

  private void validateApplication(String appName, String dns, String healthCheckEndpoint,
      String newAppId) {
    ResponseEntity<Application> getAppResponse = this.restTemplate
        .getForEntity("/application/" + newAppId, Application.class);

    Assertions.assertEquals(HttpStatus.OK, getAppResponse.getStatusCode());

    Application appCreated = getAppResponse.getBody();

    Assertions.assertNotNull(appCreated);
    Assertions.assertEquals(appName, appCreated.getName());
    Assertions.assertEquals(dns, appCreated.getDns());
    Assertions.assertEquals(healthCheckEndpoint, appCreated.getHealthEndpoint());
  }

  private String createApplication(String appName, String dns, String healthCheckEndpoint) {
    ApplicationCreate applicationCreate = new ApplicationCreate();
    applicationCreate.setName(appName);
    applicationCreate.setDns(dns);
    applicationCreate.setHealthEndpoint(healthCheckEndpoint);

    ResponseEntity<Void> response = this.restTemplate
        .postForEntity("/application", applicationCreate, Void.class);

    Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());

    String appLocation = Objects.requireNonNull(response.getHeaders().get("Location")).get(0);

    return extractApplicationIdFromLocationUrl(appLocation);
  }

  private String extractApplicationIdFromLocationUrl(String locationUrl) {
    String pattern = ".*/application/(.*)";

    Pattern r = Pattern.compile(pattern);

    Matcher m = r.matcher(locationUrl);

    Assertions.assertTrue(m.find());

    return m.group(1);
  }

}
