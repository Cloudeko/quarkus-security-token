package io.quarkiverse.quarkus.security.token.runtime;

import io.quarkiverse.quarkus.security.token.Token;
import io.quarkiverse.quarkus.security.token.TokenManager;
import io.quarkiverse.quarkus.security.token.access.AccessTokenManager;
import io.smallrye.mutiny.Uni;
import io.vertx.ext.auth.User;

public class DefaultTokenManager implements TokenManager {

    private final AccessTokenManager accessTokenManager;

    public DefaultTokenManager(AccessTokenManager accessTokenManager) {
        this.accessTokenManager = accessTokenManager;
    }

    @Override
    public Uni<Token> createToken(User user) {
        return Uni.createFrom().item(new BasicToken(accessTokenManager.createAccessToken(user)));
    }

    @Override
    public Uni<Boolean> verifyAccessToken(String token) {
        return accessTokenManager.verifyToken(token);
    }
}
