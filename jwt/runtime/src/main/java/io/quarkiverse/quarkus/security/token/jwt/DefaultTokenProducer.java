package io.quarkiverse.quarkus.security.token.jwt;

import jakarta.annotation.Priority;
import jakarta.enterprise.inject.Default;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;

import io.quarkiverse.quarkus.security.token.access.AccessTokenManager;
import io.quarkiverse.quarkus.security.token.refresh.RefreshTokenUserProvider;
import io.smallrye.jwt.auth.principal.JWTParser;

public class DefaultTokenProducer {

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

    @Default
    @Produces
    @Priority(1)
    public RefreshTokenUserProvider refreshTokenUserProvider() {
        return new DefaultRefreshTokenUserProvider();
    }
}
