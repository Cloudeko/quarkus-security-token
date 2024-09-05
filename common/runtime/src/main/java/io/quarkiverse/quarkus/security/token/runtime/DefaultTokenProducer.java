package io.quarkiverse.quarkus.security.token.runtime;

import io.quarkiverse.quarkus.security.token.TokenManager;
import io.quarkiverse.quarkus.security.token.access.AccessTokenManager;
import io.quarkiverse.quarkus.security.token.refresh.RefreshTokenUserProvider;
import io.quarkus.arc.DefaultBean;
import jakarta.annotation.Priority;
import jakarta.enterprise.inject.Default;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Singleton;

public class DefaultTokenProducer {

    @Produces
    @Singleton
    @DefaultBean
    public TokenManager tokenManager(AccessTokenManager accessTokenManager) {
        return new DefaultTokenManager(accessTokenManager);
    }

    @Default
    @Produces
    @Priority(1)
    public RefreshTokenUserProvider refreshTokenUserProvider() {
        return new DefaultRefreshTokenUserProvider();
    }
}
