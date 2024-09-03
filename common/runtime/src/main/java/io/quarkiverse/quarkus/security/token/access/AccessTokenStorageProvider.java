package io.quarkiverse.quarkus.security.token.access;

import io.quarkus.security.credential.TokenCredential;
import io.smallrye.mutiny.Uni;

public interface AccessTokenStorageProvider {

    Uni<Void> storeAccessToken(TokenCredential token);

    Uni<TokenCredential> getAccessToken(String token);

    Uni<Void> deleteAccessToken(String token);

    Uni<Void> revokeAccessTokens(String subject);
}
