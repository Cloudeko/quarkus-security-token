package io.quarkiverse.quarkus.security.token.access;

import io.smallrye.mutiny.Uni;

public interface AccessTokenValidator {

    Uni<Boolean> verifyToken(AccessToken token);

    default boolean verifyTokenBlocking(AccessToken token) {
        return verifyToken(token).await().indefinitely();
    }
}
