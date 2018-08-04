package com.apps.dashboard.external.requests.impl;

import static org.glassfish.jersey.client.ClientProperties.CONNECT_TIMEOUT;
import static org.glassfish.jersey.client.ClientProperties.READ_TIMEOUT;

import com.apps.dashboard.external.requests.RequestHelper;
import javax.annotation.Nonnull;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DefaultRequestHelper implements RequestHelper {

  private static Logger LOGGER = LoggerFactory.getLogger(DefaultRequestHelper.class);

  private Client client;

  @Autowired
  public DefaultRequestHelper(Client client) {
    this.client = client;
  }

  @Override
  public Response performGetRequest(@Nonnull String endpoint) {

    LOGGER.debug("Perform request to url {}", endpoint);

    Invocation.Builder builder = client.target(endpoint)
        .request()
        .property(CONNECT_TIMEOUT, 500)
        .property(READ_TIMEOUT, 500);

    return builder.get();
  }
}
