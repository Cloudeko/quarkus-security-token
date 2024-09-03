package io.quarkiverse.quarkus.security.token.runtime;

public record DatabaseCapabilities(String clientType) {
    public DatabaseCapabilities {
        if (clientType == null) {
            throw new IllegalArgumentException("clientType must not be null");
        }
    }

    public static DatabaseCapabilities of(String clientType) {
        return new DatabaseCapabilities(clientType);
    }
}
