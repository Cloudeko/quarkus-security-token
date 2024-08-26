package io.quarkiverse.quarkus.security.token;

import io.smallrye.mutiny.Uni;

public interface RefreshTokenValidator {

    Uni<Boolean> verifyToken(RefreshToken token);

    default boolean verifyTokenBlocking(RefreshToken token) {
        return verifyToken(token).await().indefinitely();
    }
}
