package io.quarkiverse.quarkus.security.token;

import io.quarkiverse.quarkus.security.token.refresh.RefreshTokenCredential;
import io.quarkus.security.credential.Credential;
import io.quarkus.security.credential.TokenCredential;

public interface Token extends Credential {
    TokenCredential getAccessToken();

    RefreshTokenCredential getRefreshToken();

    default String getRawAccessToken() {
        if (getAccessToken() == null) {
            return null;
        }

        return getAccessToken().getToken();
    }

    default String getRawRefreshToken() {
        if (getRefreshToken() == null) {
            return null;
        }

        return getRefreshToken().getRefreshToken();
    }

    default String getType() {
        return getAccessToken() != null ? getAccessToken().getType() : null;
    }

    default boolean isRefreshable() {
        return getRefreshToken() != null && getRefreshToken().isValid();
    }
}
