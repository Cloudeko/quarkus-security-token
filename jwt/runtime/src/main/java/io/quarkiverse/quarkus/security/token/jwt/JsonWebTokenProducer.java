package io.quarkiverse.quarkus.security.token.jwt;

import io.quarkiverse.quarkus.security.token.access.AccessTokenManager;
import io.smallrye.jwt.auth.principal.JWTParser;
import jakarta.annotation.Priority;
import jakarta.enterprise.inject.Default;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;

public class JsonWebTokenProducer {

    @Inject
    JWTParser jwtParser;

    @Inject
    JsonWebTokenConfig jsonWebTokenConfig;

    @Default
    @Produces
    @Priority(1)
    public AccessTokenManager accessTokenManager() {
        return new JsonWebAccessTokenManager(jwtParser, jsonWebTokenConfig);
    }
}
