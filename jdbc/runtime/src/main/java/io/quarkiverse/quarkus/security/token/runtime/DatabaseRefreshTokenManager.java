package io.quarkiverse.quarkus.security.token.runtime;

import java.util.Base64;
import java.util.UUID;

import io.quarkiverse.quarkus.security.token.refresh.RefreshTokenCredential;
import io.quarkiverse.quarkus.security.token.refresh.RefreshTokenManager;
import io.quarkiverse.quarkus.security.token.refresh.RefreshTokenStorageProvider;
import io.quarkiverse.quarkus.security.token.refresh.RefreshTokenUserProvider;
import io.smallrye.mutiny.Uni;
import io.vertx.ext.auth.User;

public class DatabaseRefreshTokenManager implements RefreshTokenManager {

    private final DatabaseRefreshTokenConfig config;
    private final RefreshTokenStorageProvider refreshTokenStorageProvider;
    private final RefreshTokenUserProvider refreshTokenUserProvider;

    public DatabaseRefreshTokenManager(DatabaseRefreshTokenConfig config,
            RefreshTokenStorageProvider refreshTokenStorageProvider,
            RefreshTokenUserProvider refreshTokenUserProvider) {
        this.config = config;
        this.refreshTokenStorageProvider = refreshTokenStorageProvider;
        this.refreshTokenUserProvider = refreshTokenUserProvider;
    }

    @Override
    public RefreshTokenCredential createRefreshToken(User user) {
        StringBuilder refreshToken = new StringBuilder();

        if (config.prefix().isPresent()) {
            refreshToken.append(config.prefix().get()).append("_");
        }

        String token = user.subject() + "_" + System.currentTimeMillis() + UUID.randomUUID().toString();
        String base64Token = Base64.getEncoder().encodeToString(token.getBytes());

        refreshToken.append(base64Token);

        return new DatabaseRefreshTokenCredential(user.subject(), refreshToken.toString(),
                System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 30);
    }

    @Override
    public Uni<Boolean> verifyToken(String token) {
        return refreshTokenStorageProvider.getRefreshToken(token)
                .map(refreshToken -> refreshToken != null && refreshToken.isValid());
    }

    @Override
    public Uni<RefreshTokenCredential> swapRefreshToken(String refreshToken) {
        return refreshTokenStorageProvider.getRefreshToken(refreshToken).flatMap(token -> {
            if (token == null || !token.isValid()) {
                return null;
            }

            return refreshTokenUserProvider.getUser(token).map(user -> {
                if (user == null) {
                    return null;
                }

                return createRefreshToken(user);
            });
        });
    }
}
