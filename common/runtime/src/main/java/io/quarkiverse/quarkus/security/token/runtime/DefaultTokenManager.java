package io.quarkiverse.quarkus.security.token.runtime;

import java.util.ArrayList;
import java.util.List;

import io.quarkiverse.quarkus.security.token.Token;
import io.quarkiverse.quarkus.security.token.TokenManager;
import io.quarkiverse.quarkus.security.token.TokenUserProvider;
import io.quarkiverse.quarkus.security.token.access.AccessTokenManager;
import io.quarkiverse.quarkus.security.token.access.AccessTokenStorageProvider;
import io.quarkiverse.quarkus.security.token.refresh.RefreshTokenCredential;
import io.quarkiverse.quarkus.security.token.refresh.RefreshTokenManager;
import io.quarkiverse.quarkus.security.token.refresh.RefreshTokenStorageProvider;
import io.quarkus.security.credential.TokenCredential;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.tuples.Tuple2;
import io.vertx.ext.auth.User;

public class DefaultTokenManager implements TokenManager {

    private final AccessTokenManager accessTokenManager;
    private final AccessTokenStorageProvider accessTokenStorageProvider;

    private final RefreshTokenManager refreshTokenManager;
    private final RefreshTokenStorageProvider refreshTokenStorageProvider;
    private final TokenUserProvider<RefreshTokenCredential> tokenUserProvider;

    public DefaultTokenManager(AccessTokenManager accessTokenManager,
            AccessTokenStorageProvider accessTokenStorageProvider,
            RefreshTokenManager refreshTokenManager,
            RefreshTokenStorageProvider refreshTokenStorageProvider,
            TokenUserProvider<RefreshTokenCredential> tokenUserProvider) {
        this.accessTokenManager = accessTokenManager;
        this.accessTokenStorageProvider = accessTokenStorageProvider;
        this.refreshTokenManager = refreshTokenManager;
        this.refreshTokenStorageProvider = refreshTokenStorageProvider;
        this.tokenUserProvider = tokenUserProvider;
    }

    @Override
    public Uni<Token> createToken(User user) {
        List<Uni<?>> operations = new ArrayList<>();
        TokenCredential accessToken = createAccessToken(user);
        RefreshTokenCredential refreshToken = createRefreshToken(user);

        if (accessToken != null && accessTokenStorageProvider != null) {
            operations.add(accessTokenStorageProvider.storeAccessToken(accessToken));
        }

        if (refreshToken != null && refreshTokenStorageProvider != null) {
            operations.add(refreshTokenStorageProvider.storeRefreshToken(refreshToken));
        }

        if (operations.isEmpty()) {
            return Uni.createFrom().item(new BasicToken(accessToken, refreshToken));
        }

        return Uni.combine().all().unis(operations).with((ignored) -> new BasicToken(accessToken, refreshToken));
    }

    private TokenCredential createAccessToken(User user) {
        if (accessTokenManager == null) {
            return null;
        }

        return accessTokenManager.createAccessToken(user);
    }

    private RefreshTokenCredential createRefreshToken(User user) {
        if (refreshTokenManager == null) {
            return null;
        }

        return refreshTokenManager.createRefreshToken(user);
    }

    @Override
    public Uni<Boolean> verifyAccessToken(String token) {
        if (accessTokenManager == null) {
            return Uni.createFrom().failure(new IllegalStateException("Access token manager is not available"));
        }

        return accessTokenManager.verifyToken(token);
    }

    @Override
    public Uni<Boolean> verifyRefreshToken(String token) {
        if (refreshTokenManager == null) {
            return Uni.createFrom().failure(new IllegalStateException("Refresh token manager is not available"));
        }

        return refreshTokenManager.verifyToken(token);
    }

    @Override
    public Uni<Void> revokeRefreshToken(String token) {
        if (refreshTokenStorageProvider == null) {
            return Uni.createFrom().failure(new IllegalStateException("Refresh token storage provider is not available"));
        }

        return refreshTokenStorageProvider.revokeRefreshToken(token);
    }

    @Override
    public Uni<Token> refreshToken(String token) {
        if (accessTokenManager == null || refreshTokenManager == null) {
            return Uni.createFrom().failure(new IllegalStateException("Token managers are not available"));
        }

        Uni<User> userUni = getUser(token);
        Uni<RefreshTokenCredential> refreshTokenUni = refreshTokenManager.swapRefreshToken(token);

        return Uni.combine().all().unis(userUni, refreshTokenUni).asTuple().onItem().transform(this::createToken);
    }

    private Uni<User> getUser(String token) {
        if (tokenUserProvider == null) {
            return Uni.createFrom().failure(new IllegalStateException("Token user provider is not available"));
        }

        if (refreshTokenManager == null) {
            return Uni.createFrom().failure(new IllegalStateException("Refresh token manager is not available"));
        }

        Uni<RefreshTokenCredential> refreshTokenUni = refreshTokenManager.findRefreshToken(token);

        return refreshTokenUni.onItem().transformToUni(tokenUserProvider::getUser);
    }

    private Token createToken(Tuple2<User, RefreshTokenCredential> tuple) {
        User user = tuple.getItem1();
        RefreshTokenCredential refreshToken = tuple.getItem2();

        TokenCredential accessToken = createAccessToken(user);

        return new BasicToken(accessToken, refreshToken);
    }
}
