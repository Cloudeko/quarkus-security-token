package io.quarkiverse.quarkus.security.token.runtime;

import io.quarkiverse.quarkus.security.token.Token;
import io.quarkiverse.quarkus.security.token.refresh.RefreshTokenCredential;
import io.quarkus.security.credential.TokenCredential;

public class BasicToken implements Token {

    private final TokenCredential accessToken;
    private final RefreshTokenCredential refreshToken;

    public BasicToken(TokenCredential accessToken) {
        this(accessToken, null);
    }

    public BasicToken(TokenCredential accessToken, RefreshTokenCredential refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    @Override
    public TokenCredential getAccessToken() {
        return accessToken;
    }

    @Override
    public RefreshTokenCredential getRefreshToken() {
        return refreshToken;
    }

    @Override
    public boolean isRefreshable() {
        return refreshToken != null;
    }
}
