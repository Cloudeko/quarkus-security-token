package io.quarkiverse.quarkus.security.token.refresh;

import io.quarkiverse.quarkus.security.token.access.AccessToken;
import io.vertx.ext.auth.User;

public interface RefreshTokenProducer {
    AccessToken createAccessToken(User user);
}
