package io.quarkiverse.quarkus.security.token.runtime;

import io.quarkiverse.quarkus.security.token.refresh.RefreshToken;
import io.quarkiverse.quarkus.security.token.refresh.RefreshTokenUserProvider;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.ext.auth.User;
import io.vertx.sqlclient.Row;

public class DatabaseRefreshToken implements RefreshToken {

    private final String subject;
    private final String refreshToken;
    private final boolean revoked;
    private final long expirationTime;
    private final long issuedAt;

    private RefreshTokenUserProvider userProvider;

    public DatabaseRefreshToken(Row row) {
        this.subject = row.getString("subject");
        this.refreshToken = row.getString("refresh_token");
        this.revoked = row.getBoolean("revoked");
        this.expirationTime = row.getLong("expiration_time");
        this.issuedAt = row.getLong("issued_at");
    }

    public DatabaseRefreshToken(String subject, String refreshToken, long expirationTime) {
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

    public RefreshTokenUserProvider getUserProvider() {
        return userProvider;
    }

    public void setUserProvider(RefreshTokenUserProvider userProvider) {
        this.userProvider = userProvider;
    }
}
