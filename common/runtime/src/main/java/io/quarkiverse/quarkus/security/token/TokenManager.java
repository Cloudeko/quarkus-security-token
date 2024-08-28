package io.quarkiverse.quarkus.security.token;

import io.quarkiverse.quarkus.security.token.access.AccessToken;
import io.quarkiverse.quarkus.security.token.refresh.RefreshToken;
import io.smallrye.mutiny.Uni;
import io.vertx.ext.auth.User;

public interface TokenManager {

    Uni<Token> createToken(User user);

    Uni<Boolean> verifyAccessToken(String token);

    default Uni<Boolean> verifyRefreshToken(String token) {
        throw new UnsupportedOperationException("Refresh token verification is not supported");
    }

    default Uni<Void> revokeAccessToken(AccessToken token) {
        throw new UnsupportedOperationException("Revoke access token is not supported");
    }

    default Uni<Void> revokeRefreshToken(RefreshToken token) {
        throw new UnsupportedOperationException("Revoke refresh token is not supported");
    }

    default Uni<Token> refreshToken(RefreshToken token) {
        throw new UnsupportedOperationException("Refreshing token is not supported");
    }
}
