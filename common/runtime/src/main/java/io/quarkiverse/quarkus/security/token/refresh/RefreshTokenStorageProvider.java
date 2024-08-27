package io.quarkiverse.quarkus.security.token.refresh;

import io.smallrye.mutiny.Uni;

public interface RefreshTokenStorageProvider {
    Uni<Void> storeRefreshToken(RefreshToken refreshToken);

    Uni<RefreshToken> getRefreshToken(String refreshToken);
}
