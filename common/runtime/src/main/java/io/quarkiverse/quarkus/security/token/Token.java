package io.quarkiverse.quarkus.security.token;

public interface Token {
    AccessToken getAccessToken();

    RefreshToken getRefreshToken();

    default String getAccessTokenString() {
        return getAccessToken().getAccessToken();
    }

    default String getRefreshTokenString() {
        return getRefreshToken().getRefreshToken();
    }

    default boolean isRefreshable() {
        return true;
    }
}
