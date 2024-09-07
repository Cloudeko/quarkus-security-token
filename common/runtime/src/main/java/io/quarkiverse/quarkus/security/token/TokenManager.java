package io.quarkiverse.quarkus.security.token;

import io.smallrye.mutiny.Uni;
import io.vertx.ext.auth.User;

public interface TokenManager {

    Uni<Token> createToken(User user);

    default Token createTokenBlocking(User user) {
        return createToken(user).await().indefinitely();
    }

    Uni<Boolean> verifyAccessToken(String token);

    default Boolean verifyAccessTokenBlocking(String token) {
        return verifyAccessToken(token).await().indefinitely();
    }

    default Uni<Boolean> verifyRefreshToken(String token) {
        throw new UnsupportedOperationException("Refresh token verification is not supported");
    }

    default Boolean verifyRefreshTokenBlocking(String token) {
        return verifyRefreshToken(token).await().indefinitely();
    }

    default Uni<Void> revokeAccessToken(String token) {
        throw new UnsupportedOperationException("Revoke access token is not supported");
    }

    default void revokeAccessTokenBlocking(String token) {
        revokeAccessToken(token).await().indefinitely();
    }

    default Uni<Void> revokeRefreshToken(String token) {
        throw new UnsupportedOperationException("Revoke refresh token is not supported");
    }

    default void revokeRefreshTokenBlocking(String token) {
        revokeRefreshToken(token).await().indefinitely();
    }

    default Uni<Token> refreshToken(String token) {
        throw new UnsupportedOperationException("Refreshing token is not supported");
    }

    default Token refreshTokenBlocking(String token) {
        return refreshToken(token).await().indefinitely();
    }
}
