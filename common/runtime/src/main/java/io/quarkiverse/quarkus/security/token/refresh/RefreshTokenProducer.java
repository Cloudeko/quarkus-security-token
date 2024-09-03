package io.quarkiverse.quarkus.security.token.refresh;

import io.vertx.ext.auth.User;

public interface RefreshTokenProducer {
    RefreshTokenCredential createRefreshToken(User user);
}
