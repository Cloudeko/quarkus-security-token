package io.quarkiverse.quarkus.security.token.refresh;

import io.smallrye.mutiny.Uni;

public interface RefreshTokenStorageProvider {
    Uni<Void> storeRefreshToken(RefreshTokenCredential refreshToken);

    Uni<Void> removeRefreshToken(String refreshToken);

    Uni<RefreshTokenCredential> getRefreshToken(String refreshToken);

    Uni<Void> revokeRefreshToken(String refreshToken);

    Uni<Void> removeAllTokensForSubject(String subject);

    Uni<Void> removeAllInvalidTokens();

    Uni<Void> removeAllTokens();
}
