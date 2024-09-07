/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.quarkiverse.quarkus.security.token.it;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

import io.quarkiverse.quarkus.security.token.Token;
import io.quarkiverse.quarkus.security.token.TokenManager;
import io.quarkus.security.runtime.QuarkusPrincipal;
import io.quarkus.security.runtime.QuarkusSecurityIdentity;
import io.quarkus.smallrye.jwt.runtime.auth.BearerTokenAuthentication;
import io.quarkus.vertx.http.runtime.security.QuarkusHttpUser;

@Path("/security-token")
@ApplicationScoped
public class SecurityTokenResource {

    @Inject
    TokenManager tokenManager;

    @POST
    public Response createToken(@QueryParam("subject") String subject) {
        QuarkusPrincipal principal = new QuarkusPrincipal(subject);
        QuarkusSecurityIdentity identity = QuarkusSecurityIdentity.builder().setPrincipal(principal).build();
        QuarkusHttpUser user = new QuarkusHttpUser(identity);
        Token token = tokenManager.createTokenBlocking(user);

        return Response.ok(new TokenResponse(token)).build();
    }

    @POST
    @Path("/swap")
    public Response swapToken(@QueryParam("token") String token) {
        Token newToken = tokenManager.refreshTokenBlocking(token);
        return Response.ok(new TokenResponse(newToken)).build();
    }

    @GET
    @BearerTokenAuthentication
    public String hello(@Context SecurityContext securityContext) {
        return "Hello " + securityContext.getUserPrincipal().getName();
    }
}
