package io.quarkiverse.quarkus.security.token.refresh;

public interface RefreshToken {
    String getSubject();

    String getRefreshToken();

    default boolean isExpired() {
        return isExpired(System.currentTimeMillis());
    }

    default boolean isExpired(long currentTime) {
        return false;
    }
}
