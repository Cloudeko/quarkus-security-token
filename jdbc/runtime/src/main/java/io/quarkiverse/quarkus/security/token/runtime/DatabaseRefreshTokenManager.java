package io.quarkiverse.quarkus.security.token.runtime;

import io.quarkiverse.quarkus.security.token.access.AccessToken;
import io.quarkiverse.quarkus.security.token.refresh.RefreshToken;
import io.quarkiverse.quarkus.security.token.refresh.RefreshTokenManager;
import io.smallrye.mutiny.Uni;
import io.vertx.ext.auth.User;

public class DatabaseRefreshTokenManager implements RefreshTokenManager {

    @Override
    public AccessToken createAccessToken(User user) {
        return null;
    }

    @Override
    public Uni<Boolean> verifyToken(RefreshToken token) {
        return null;
    }

    @Override
    public Uni<RefreshToken> swapRefreshToken(String refreshToken) {
        return null;
    }
}
