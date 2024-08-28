package io.quarkiverse.quarkus.security.token.runtime;

import io.quarkiverse.quarkus.security.token.Token;
import io.quarkiverse.quarkus.security.token.TokenManager;
import io.quarkiverse.quarkus.security.token.access.AccessToken;
import io.quarkiverse.quarkus.security.token.access.AccessTokenManager;
import io.quarkiverse.quarkus.security.token.refresh.RefreshToken;
import io.quarkiverse.quarkus.security.token.refresh.RefreshTokenManager;
import io.quarkiverse.quarkus.security.token.refresh.RefreshTokenStorageProvider;
import io.quarkiverse.quarkus.security.token.refresh.RefreshTokenUserProvider;
import io.smallrye.mutiny.Uni;
import io.vertx.ext.auth.User;

public class DatabaseTokenManager implements TokenManager {

    private final AccessTokenManager accessTokenManager;
    private final RefreshTokenManager refreshTokenManager;
    private final RefreshTokenStorageProvider refreshTokenStorageProvider;
    private final RefreshTokenUserProvider refreshTokenUserProvider;

    public DatabaseTokenManager(AccessTokenManager accessTokenManager,
            RefreshTokenManager refreshTokenManager,
            RefreshTokenStorageProvider refreshTokenStorageProvider,
            RefreshTokenUserProvider refreshTokenUserProvider) {
        this.accessTokenManager = accessTokenManager;
        this.refreshTokenManager = refreshTokenManager;
        this.refreshTokenStorageProvider = refreshTokenStorageProvider;
        this.refreshTokenUserProvider = refreshTokenUserProvider;
    }

    @Override
    public Uni<Token> createToken(User user) {
        AccessToken accessToken = accessTokenManager.createAccessToken(user);
        RefreshToken refreshToken = refreshTokenManager.createRefreshToken(user);

        return refreshTokenStorageProvider.storeRefreshToken(refreshToken)
                .replaceWith(new BasicToken(accessToken, refreshToken));
    }

    @Override
    public Uni<Boolean> verifyAccessToken(String token) {
        return accessTokenManager.verifyToken(token);
    }

    @Override
    public Uni<Boolean> verifyRefreshToken(String token) {
        return refreshTokenManager.verifyToken(token);
    }

    @Override
    public Uni<Void> revokeRefreshToken(RefreshToken token) {
        return refreshTokenStorageProvider.revokeRefreshToken(token.getRefreshToken());
    }

    @Override
    public Uni<Token> refreshToken(RefreshToken token) {
        return Uni.combine().all().unis(getUser(token), refreshTokenManager.swapRefreshToken(token.getRefreshToken())).asTuple()
                .onItem().transform(tuple -> {
                    User user = tuple.getItem1();
                    RefreshToken newToken = tuple.getItem2();

                    AccessToken accessToken = accessTokenManager.createAccessToken(user);

                    return new BasicToken(accessToken, newToken);
                });
    }

    private Uni<User> getUser(RefreshToken token) {
        return refreshTokenUserProvider.getUser(token);
    }
}
