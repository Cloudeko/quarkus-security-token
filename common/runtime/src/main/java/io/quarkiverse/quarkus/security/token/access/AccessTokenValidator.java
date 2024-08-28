package io.quarkiverse.quarkus.security.token.access;

import io.smallrye.mutiny.Uni;

public interface AccessTokenValidator {

    Uni<Boolean> verifyToken(String token);

    default boolean verifyTokenBlocking(String token) {
        return verifyToken(token).await().indefinitely();
    }
}
