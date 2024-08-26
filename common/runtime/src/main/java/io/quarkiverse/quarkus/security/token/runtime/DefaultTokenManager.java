package io.quarkiverse.quarkus.security.token.runtime;

import io.quarkiverse.quarkus.security.token.*;
import io.smallrye.mutiny.Uni;
import io.vertx.ext.auth.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class DefaultTokenManager implements TokenManager {

    private final AccessTokenProducer accessTokenProducer;
    private final RefreshTokenProducer refreshTokenProducer;
    private final AccessTokenValidator accessTokenValidator;
    private final RefreshTokenValidator refreshTokenValidator;

    @Inject
    public DefaultTokenManager(AccessTokenProducer accessTokenProducer,
            RefreshTokenProducer refreshTokenProducer,
            AccessTokenValidator accessTokenValidator,
            RefreshTokenValidator refreshTokenValidator) {
        this.accessTokenProducer = accessTokenProducer;
        this.refreshTokenProducer = refreshTokenProducer;
        this.accessTokenValidator = accessTokenValidator;
        this.refreshTokenValidator = refreshTokenValidator;
    }

    @Override
    public Uni<Token> createToken(User user) {
        return null;
    }

    @Override
    public Uni<Token> createToken(Token token) {
        return null;
    }

    @Override
    public Uni<Boolean> verifyAccessToken(AccessToken token) {
        return null;
    }

    @Override
    public Uni<Boolean> verifyRefreshToken(RefreshToken token) {
        return null;
    }
}
