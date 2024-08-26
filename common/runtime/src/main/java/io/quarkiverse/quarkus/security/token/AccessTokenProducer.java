package io.quarkiverse.quarkus.security.token;

import io.vertx.ext.auth.User;

public interface AccessTokenProducer {
    AccessToken createAccessToken(User user);
}
