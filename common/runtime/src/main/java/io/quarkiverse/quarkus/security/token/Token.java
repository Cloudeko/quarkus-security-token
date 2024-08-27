package io.quarkiverse.quarkus.security.token;

import io.quarkiverse.quarkus.security.token.access.AccessToken;
import io.quarkiverse.quarkus.security.token.refresh.RefreshToken;

public interface Token {
    AccessToken getAccessToken();

    RefreshToken getRefreshToken();

    default String getRawAccessToken() {
        if (getAccessToken() == null) {
            return null;
        }

        return getAccessToken().getAccessToken();
    }

    default String getRawRefreshToken() {
        if (getRefreshToken() == null) {
            return null;
        }

        return getRefreshToken().getRefreshToken();
    }

    default boolean isRefreshable() {
        return true;
    }
}
