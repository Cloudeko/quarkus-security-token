package io.quarkiverse.quarkus.security.token.access;

import io.smallrye.mutiny.Uni;

public interface AccessTokenStorageProvider {

    Uni<Void> storeAccessToken(AccessToken token);

    Uni<AccessToken> getAccessToken(String token);

    Uni<Void> deleteAccessToken(String token);

    Uni<Void> revokeAccessTokens(String subject);
}
