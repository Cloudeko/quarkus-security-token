package io.quarkiverse.quarkus.security.token;

import io.vertx.ext.auth.User;

public interface RefreshTokenProducer {
    AccessToken createAccessToken(User user);
}
