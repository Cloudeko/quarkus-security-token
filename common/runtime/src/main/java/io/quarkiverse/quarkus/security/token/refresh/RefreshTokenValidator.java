package io.quarkiverse.quarkus.security.token.refresh;

import io.smallrye.mutiny.Uni;

public interface RefreshTokenValidator {

    Uni<Boolean> verifyToken(String token);

    default boolean verifyTokenBlocking(String token) {
        return verifyToken(token).await().indefinitely();
    }
}
