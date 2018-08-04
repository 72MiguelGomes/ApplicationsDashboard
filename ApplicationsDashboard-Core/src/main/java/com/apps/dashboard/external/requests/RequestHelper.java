package com.apps.dashboard.external.requests;

import javax.annotation.Nonnull;
import javax.ws.rs.core.Response;

public interface RequestHelper {

  Response performGetRequest(@Nonnull String endpoint);

}
