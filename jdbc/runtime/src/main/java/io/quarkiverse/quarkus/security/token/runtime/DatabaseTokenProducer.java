package io.quarkiverse.quarkus.security.token.runtime;

import jakarta.annotation.Priority;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;

import io.quarkiverse.quarkus.security.token.TokenUserProvider;
import io.quarkiverse.quarkus.security.token.refresh.RefreshTokenManager;
import io.quarkiverse.quarkus.security.token.refresh.RefreshTokenStorageProvider;
import io.vertx.sqlclient.Pool;

public class DatabaseTokenProducer {

    @Inject
    DatabaseRefreshTokenConfig databaseRefreshTokenConfig;

    @Produces
    @Priority(Byte.MAX_VALUE)
    public RefreshTokenManager refreshTokenManager(RefreshTokenStorageProvider refreshTokenStorageProvider,
            TokenUserProvider tokenUserProvider) {
        return new DatabaseRefreshTokenManager(databaseRefreshTokenConfig, refreshTokenStorageProvider,
                tokenUserProvider);
    }

    @Produces
    @Priority(Byte.MAX_VALUE)
    public RefreshTokenStorageProvider refreshTokenStorageProvider(DatabaseCapabilities client, Pool pool) {
        return new DatabaseRefreshTokenStorageProvider(client.clientType(), pool);
    }
}
