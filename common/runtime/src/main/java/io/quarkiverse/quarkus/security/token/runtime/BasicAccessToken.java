package io.quarkiverse.quarkus.security.token.runtime;

import io.quarkiverse.quarkus.security.token.AccessToken;
import io.quarkus.smallrye.jwt.runtime.auth.JwtPrincipalProducer;
import io.quarkus.vertx.http.runtime.security.QuarkusHttpUser;

public class BasicAccessToken implements AccessToken {

    private final String accessToken;
    private final String type;

    public BasicAccessToken(String accessToken) {
        this(accessToken, "jwt");
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
