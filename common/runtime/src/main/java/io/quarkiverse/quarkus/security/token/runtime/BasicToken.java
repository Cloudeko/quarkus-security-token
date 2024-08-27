package io.quarkiverse.quarkus.security.token.runtime;

import io.quarkiverse.quarkus.security.token.Token;
import io.quarkiverse.quarkus.security.token.access.AccessToken;
import io.quarkiverse.quarkus.security.token.refresh.RefreshToken;

public class BasicToken implements Token {

    private final AccessToken accessToken;
    private final RefreshToken refreshToken;

    public BasicToken(AccessToken accessToken) {
        this(accessToken, null);
    }

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
