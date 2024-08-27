package io.quarkiverse.quarkus.security.token.runtime;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import io.quarkiverse.quarkus.security.token.Token;
import io.quarkiverse.quarkus.security.token.TokenManager;
import io.quarkiverse.quarkus.security.token.access.AccessToken;
import io.quarkiverse.quarkus.security.token.access.AccessTokenManager;
import io.smallrye.mutiny.Uni;
import io.vertx.ext.auth.User;

@ApplicationScoped
public class DefaultTokenManager implements TokenManager {

    private final AccessTokenManager accessTokenManager;

    @Inject
    public DefaultTokenManager(AccessTokenManager accessTokenManager) {
        this.accessTokenManager = accessTokenManager;
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
}
