package io.quarkiverse.quarkus.security.token.runtime;

import io.quarkiverse.quarkus.security.token.RefreshToken;

public class BasicRefreshToken implements RefreshToken {

    private final String refreshToken;

    public BasicRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    @Override
    public String getRefreshToken() {
        return refreshToken;
    }
}
