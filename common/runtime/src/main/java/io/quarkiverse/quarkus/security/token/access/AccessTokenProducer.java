package io.quarkiverse.quarkus.security.token.access;

import io.quarkus.security.credential.TokenCredential;
import io.vertx.ext.auth.User;

public interface AccessTokenProducer {
    TokenCredential createAccessToken(User user);
}
