package io.quarkiverse.quarkus.security.token.runtime;

import io.quarkiverse.quarkus.security.token.TokenUserProvider;
import io.quarkiverse.quarkus.security.token.refresh.RefreshTokenCredential;
import io.smallrye.mutiny.Uni;
import io.vertx.ext.auth.User;
import io.vertx.sqlclient.Row;

public class DatabaseRefreshTokenCredential implements RefreshTokenCredential {

    private final String subject;
    private final String refreshToken;
    private final boolean revoked;
    private final long expirationTime;
    private final long issuedAt;

    private TokenUserProvider userProvider;

    public DatabaseRefreshTokenCredential(Row row) {
        this.subject = row.getString("subject");
        this.refreshToken = row.getString("refresh_token");
        this.revoked = row.getBoolean("revoked");
        this.expirationTime = row.getLong("expiration_time");
        this.issuedAt = row.getLong("issued_at");
    }

    public DatabaseRefreshTokenCredential(String subject, String refreshToken, long expirationTime) {
        this.subject = subject;
        this.refreshToken = refreshToken;
        this.revoked = false;
        this.expirationTime = expirationTime;
        this.issuedAt = System.currentTimeMillis();
    }

    @Override
    public String getSubject() {
        return subject;
    }

    @Override
    public String getRefreshToken() {
        return refreshToken;
    }

    @Override
    public boolean isRevoked() {
        return revoked;
    }

    @Override
    public long getExpirationTime() {
        return expirationTime;
    }

    @Override
    public long getIssuedAt() {
        return issuedAt;
    }

    public Uni<User> getUser() {
        return userProvider.getUser(this);
    }

    public TokenUserProvider getUserProvider() {
        return userProvider;
    }

    public void setUserProvider(TokenUserProvider userProvider) {
        this.userProvider = userProvider;
    }
}
