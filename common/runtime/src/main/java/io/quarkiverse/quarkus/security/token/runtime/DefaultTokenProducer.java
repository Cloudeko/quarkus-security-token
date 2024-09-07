package io.quarkiverse.quarkus.security.token.runtime;

import jakarta.annotation.Priority;
import jakarta.enterprise.inject.Default;
import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import io.quarkiverse.quarkus.security.token.TokenManager;
import io.quarkiverse.quarkus.security.token.TokenUserProvider;
import io.quarkiverse.quarkus.security.token.access.AccessTokenManager;
import io.quarkiverse.quarkus.security.token.access.AccessTokenStorageProvider;
import io.quarkiverse.quarkus.security.token.refresh.RefreshTokenManager;
import io.quarkiverse.quarkus.security.token.refresh.RefreshTokenStorageProvider;
import io.quarkus.arc.DefaultBean;

public class DefaultTokenProducer {

    @Inject
    Instance<AccessTokenManager> accessTokenManager;

    @Inject
    Instance<AccessTokenStorageProvider> accessTokenStorageProvider;

    @Inject
    Instance<RefreshTokenManager> refreshTokenManager;

    @Inject
    Instance<RefreshTokenStorageProvider> refreshTokenStorageProvider;

    @Inject
    Instance<TokenUserProvider> refreshTokenUserProvider;

    @Produces
    @Singleton
    @DefaultBean
    public TokenManager tokenManager() {
        AccessTokenManager accessTokenManager = this.accessTokenManager.isResolvable() ? this.accessTokenManager.get() : null;
        AccessTokenStorageProvider accessTokenStorageProvider = this.accessTokenStorageProvider.isResolvable()
                ? this.accessTokenStorageProvider.get()
                : null;

        RefreshTokenManager refreshTokenManager = this.refreshTokenManager.isResolvable() ? this.refreshTokenManager.get()
                : null;
        RefreshTokenStorageProvider refreshTokenStorageProvider = this.refreshTokenStorageProvider.isResolvable()
                ? this.refreshTokenStorageProvider.get()
                : null;
        TokenUserProvider tokenUserProvider = this.refreshTokenUserProvider.isResolvable()
                ? this.refreshTokenUserProvider.get()
                : null;

        return new DefaultTokenManager(accessTokenManager, accessTokenStorageProvider, refreshTokenManager,
                refreshTokenStorageProvider, tokenUserProvider);
    }

    @Default
    @Produces
    @Priority(1)
    public TokenUserProvider refreshTokenUserProvider() {
        return new DefaultRefreshTokenUserProvider();
    }
}
