package io.quarkiverse.quarkus.security.token.runtime;

import io.quarkiverse.quarkus.security.token.AccessToken;
import io.quarkiverse.quarkus.security.token.AccessTokenProducer;
import io.vertx.ext.auth.User;

public class JsonWebAccessTokenProducer implements AccessTokenProducer {
    @Override
    public AccessToken createAccessToken(User user) {
        return null;
    }
}
