package io.quarkiverse.quarkus.security.token.runtime;

import jakarta.annotation.Priority;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;

import io.quarkiverse.quarkus.security.token.TokenManager;
import io.quarkiverse.quarkus.security.token.access.AccessTokenManager;
import io.quarkiverse.quarkus.security.token.refresh.RefreshTokenManager;
import io.quarkiverse.quarkus.security.token.refresh.RefreshTokenStorageProvider;
import io.quarkiverse.quarkus.security.token.refresh.RefreshTokenUserProvider;
import io.vertx.sqlclient.Pool;

public class DatabaseTokenProducer {

    @Inject
    DatabaseRefreshTokenConfig databaseRefreshTokenConfig;

    @Produces
    @Priority(Byte.MAX_VALUE)
    public TokenManager tokenManager(AccessTokenManager accessTokenManager,
            RefreshTokenManager refreshTokenManager,
            RefreshTokenStorageProvider refreshTokenStorageProvider,
            RefreshTokenUserProvider refreshTokenUserProvider) {
        return new DatabaseTokenManager(accessTokenManager, refreshTokenManager, refreshTokenStorageProvider,
                refreshTokenUserProvider);
    }

    @Produces
    @Priority(Byte.MAX_VALUE)
    public RefreshTokenManager refreshTokenManager(RefreshTokenStorageProvider refreshTokenStorageProvider,
            RefreshTokenUserProvider refreshTokenUserProvider) {
        return new DatabaseRefreshTokenManager(databaseRefreshTokenConfig, refreshTokenStorageProvider,
                refreshTokenUserProvider);
    }

    @Produces
    @Priority(Byte.MAX_VALUE)
    public RefreshTokenStorageProvider refreshTokenStorageProvider(DatabaseCapabilities client, Pool pool) {
        return new DatabaseRefreshTokenStorageProvider(client.clientType(), pool);
    }
}
