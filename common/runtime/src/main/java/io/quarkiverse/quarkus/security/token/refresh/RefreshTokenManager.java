package io.quarkiverse.quarkus.security.token.refresh;

import io.smallrye.mutiny.Uni;

public interface RefreshTokenManager extends RefreshTokenProducer, RefreshTokenValidator {
    Uni<RefreshToken> swapRefreshToken(String refreshToken);
}
