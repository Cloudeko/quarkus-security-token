package io.quarkiverse.quarkus.security.token.runtime;

public enum DatabaseQuery {
    STORE_REFRESH_TOKEN(
            "INSERT INTO refresh_tokens (token, subject, revoked, expiration_time, issued_at) VALUES (%s, %s, false, %s, CURRENT_TIMESTAMP)",
            3),
    REMOVE_REFRESH_TOKEN("DELETE FROM refresh_tokens WHERE token = %s", 1),
    GET_REFRESH_TOKEN("SELECT token, subject, revoked, expiration_time, issued_at FROM refresh_tokens WHERE token = %s", 1),
    REVOKE_REFRESH_TOKEN("UPDATE refresh_tokens SET revoked = true WHERE token = %s", 1),
    REMOVE_ALL_TOKENS_FOR_SUBJECT("DELETE FROM refresh_tokens WHERE subject = %s", 1),
    REMOVE_ALL_INVALID_TOKENS("DELETE FROM refresh_tokens WHERE expiration_time < CURRENT_TIMESTAMP OR revoked = true", 0),
    REMOVE_ALL_TOKENS("DELETE FROM refresh_tokens", 0);

    private final String query;
    private final int parameters;

    private DatabaseQuery(String query, int parameters) {
        this.query = query;
        this.parameters = parameters;
    }

    public String getQuery(String clientType) {
        final String[] placeholders = new String[parameters];
        for (int i = 0; i < parameters; i++) {
            placeholders[i] = switch (clientType) {
                case DatabaseCapability.POSTGRESQL_CLIENT -> "$" + (i + 1);
                case DatabaseCapability.MSSQL_CLIENT -> "@p" + (i + 1);
                case DatabaseCapability.DB2_CLIENT, DatabaseCapability.ORACLE_CLIENT, DatabaseCapability.MYSQL_CLIENT -> "?";
                default -> throw new IllegalArgumentException("Unknown sql client " + clientType);
            };
        }

        return String.format(query, (Object[]) placeholders);
    }
}
