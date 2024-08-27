package io.quarkiverse.quarkus.security.token.access;

public interface AccessToken {

    String getAccessToken();

    String getType();
}
