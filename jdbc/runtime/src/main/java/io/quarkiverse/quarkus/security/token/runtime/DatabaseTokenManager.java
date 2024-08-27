package io.quarkiverse.quarkus.security.token.runtime;

import jakarta.inject.Inject;

import io.quarkiverse.quarkus.security.token.Token;
import io.quarkiverse.quarkus.security.token.TokenManager;
import io.quarkiverse.quarkus.security.token.access.AccessToken;
import io.quarkiverse.quarkus.security.token.access.AccessTokenManager;
import io.quarkiverse.quarkus.security.token.refresh.RefreshToken;
import io.quarkiverse.quarkus.security.token.refresh.RefreshTokenManager;
import io.smallrye.mutiny.Uni;
import io.vertx.ext.auth.User;

public class DatabaseTokenManager implements TokenManager {

    private final AccessTokenManager accessTokenManager;
    private final RefreshTokenManager refreshTokenManager;

    @Inject
    public DatabaseTokenManager(AccessTokenManager accessTokenManager, RefreshTokenManager refreshTokenManager) {
        this.accessTokenManager = accessTokenManager;
        this.refreshTokenManager = refreshTokenManager;
    }

    @Override
    public Uni<Token> createToken(User user) {
        AccessToken accessToken = accessTokenManager.createAccessToken(user);
        return Uni.createFrom().item(new BasicToken(accessToken));
    }

    @Override
    public Uni<Token> createToken(Token token) {
        return Uni.createFrom().item(token);
    }

    @Override
    public Uni<Boolean> verifyAccessToken(AccessToken token) {
        return accessTokenManager.verifyToken(token);
    }

    @Override
    public Uni<Boolean> verifyRefreshToken(RefreshToken token) {
        return TokenManager.super.verifyRefreshToken(token);
    }

    @Override
    public Uni<Boolean> revokeAccessToken(AccessToken token) {
        return TokenManager.super.revokeAccessToken(token);
    }

    @Override
    public Uni<Boolean> revokeRefreshToken(RefreshToken token) {
        return TokenManager.super.revokeRefreshToken(token);
    }

    @Override
    public Uni<Token> refreshToken(RefreshToken token) {
        return TokenManager.super.refreshToken(token);
    }
}
