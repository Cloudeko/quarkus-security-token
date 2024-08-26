package io.quarkiverse.quarkus.security.token;

public interface AccessToken {

    String getAccessToken();

    String getType();
}
