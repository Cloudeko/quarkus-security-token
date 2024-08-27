package io.quarkiverse.quarkus.security.token.refresh;

import io.smallrye.mutiny.Uni;

public interface RefreshTokenStorageProvider {
    Uni<Void> storeRefreshToken(RefreshToken refreshToken);

    Uni<Void> removeRefreshToken(String refreshToken);

    Uni<RefreshToken> getRefreshToken(String refreshToken);

    Uni<Void> revokeRefreshToken(String refreshToken);

    Uni<Void> removeAllTokensForSubject(String subject);

    Uni<Void> removeAllInvalidTokens();

    Uni<Void> removeAllTokens();
}
