package io.quarkiverse.quarkus.security.token.runtime;

import io.quarkiverse.quarkus.security.token.AccessToken;

public class BasicAccessToken implements AccessToken {

    private final String accessToken;
    private final String type;

    public BasicAccessToken(String accessToken) {
        this(accessToken, "basic");
    }

    public BasicAccessToken(String accessToken, String type) {
        this.accessToken = accessToken;
        this.type = type;
    }

    @Override
    public String getAccessToken() {
        return accessToken;
    }

    @Override
    public String getType() {
        return type;
    }
}
