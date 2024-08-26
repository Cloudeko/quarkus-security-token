package io.quarkiverse.quarkus.security.token.runtime;

import io.quarkiverse.quarkus.security.token.AccessToken;
import io.quarkiverse.quarkus.security.token.RefreshToken;
import io.quarkiverse.quarkus.security.token.Token;

public class BasicToken implements Token {

    private final AccessToken accessToken;
    private final RefreshToken refreshToken;

    public BasicToken(AccessToken accessToken, RefreshToken refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    @Override
    public AccessToken getAccessToken() {
        return accessToken;
    }

    @Override
    public RefreshToken getRefreshToken() {
        return refreshToken;
    }

    @Override
    public boolean isRefreshable() {
        return refreshToken != null;
    }
}
