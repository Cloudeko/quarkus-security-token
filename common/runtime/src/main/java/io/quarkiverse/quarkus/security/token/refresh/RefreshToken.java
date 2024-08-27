package io.quarkiverse.quarkus.security.token.refresh;

public interface RefreshToken {
    String getSubject();

    String getRefreshToken();

    boolean isRevoked();

    long getExpirationTime();

    long getIssuedAt();

    default boolean isExpired() {
        return isExpired(System.currentTimeMillis());
    }

    default boolean isExpired(long currentTime) {
        return getExpirationTime() < currentTime;
    }

    default boolean isValid() {
        return !isRevoked() && !isExpired();
    }
}
