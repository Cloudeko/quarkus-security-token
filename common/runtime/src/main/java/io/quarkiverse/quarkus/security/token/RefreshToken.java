package io.quarkiverse.quarkus.security.token;

public interface RefreshToken {
    String getRefreshToken();

    default boolean isExpired() {
        return isExpired(System.currentTimeMillis());
    }

    default boolean isExpired(long currentTime) {
        return false;
    }
}
