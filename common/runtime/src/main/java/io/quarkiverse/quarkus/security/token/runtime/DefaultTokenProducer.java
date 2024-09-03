package io.quarkiverse.quarkus.security.token.runtime;

import jakarta.annotation.Priority;
import jakarta.enterprise.inject.Default;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import io.quarkiverse.quarkus.security.token.TokenManager;
import io.quarkiverse.quarkus.security.token.access.AccessTokenManager;
import io.quarkiverse.quarkus.security.token.refresh.RefreshTokenUserProvider;
import io.quarkus.arc.DefaultBean;
import io.smallrye.jwt.auth.principal.JWTParser;

public class DefaultTokenProducer {

    @Inject
    JWTParser jwtParser;

    @Inject
    CommonAccessTokenConfig commonAccessTokenConfig;

    @Produces
    @Singleton
    @DefaultBean
    public TokenManager tokenManager(AccessTokenManager accessTokenManager) {
        return new DefaultTokenManager(accessTokenManager);
    }

    @Default
    @Produces
    @Priority(1)
    public AccessTokenManager accessTokenManager() {
        return new JsonWebAccessTokenManager(jwtParser, commonAccessTokenConfig);
    }

    @Default
    @Produces
    @Priority(1)
    public RefreshTokenUserProvider refreshTokenUserProvider() {
        return new DefaultRefreshTokenUserProvider();
    }
}
