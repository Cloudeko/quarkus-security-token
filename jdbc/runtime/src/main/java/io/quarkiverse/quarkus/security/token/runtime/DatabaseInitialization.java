package io.quarkiverse.quarkus.security.token.runtime;

public enum DatabaseInitialization {
    POSTGRESQL(DatabaseCapability.POSTGRESQL_CLIENT,
            "CREATE TABLE IF NOT EXISTS access_tokens (token VARCHAR(255) PRIMARY KEY, subject VARCHAR(255), expiration_time BIGINT, issued_at BIGINT, revoked BOOLEAN);",
            "CREATE TABLE IF NOT EXISTS refresh_tokens (token VARCHAR(255) PRIMARY KEY, subject VARCHAR(255), expiration_time BIGINT, issued_at BIGINT, revoked BOOLEAN);",
            true),
    MSSQL(DatabaseCapability.MSSQL_CLIENT,
            "CREATE TABLE access_tokens (token VARCHAR(255) PRIMARY KEY, subject VARCHAR(255), expiration_time BIGINT, issued_at BIGINT, revoked BIT);",
            "CREATE TABLE refresh_tokens (token VARCHAR(255) PRIMARY KEY, subject VARCHAR(255), expiration_time BIGINT, issued_at BIGINT, revoked BIT);",
            false),
    MYSQL(DatabaseCapability.MYSQL_CLIENT,
            "CREATE TABLE IF NOT EXISTS access_tokens (token VARCHAR(255) PRIMARY KEY, subject VARCHAR(255), expiration_time BIGINT, issued_at BIGINT, revoked BOOLEAN);",
            "CREATE TABLE IF NOT EXISTS refresh_tokens (token VARCHAR(255) PRIMARY KEY, subject VARCHAR(255), expiration_time BIGINT, issued_at BIGINT, revoked BOOLEAN);",
            true),
    DB2(DatabaseCapability.DB2_CLIENT,
            "CREATE TABLE access_tokens (token VARCHAR(255) PRIMARY KEY, subject VARCHAR(255), expiration_time BIGINT, issued_at BIGINT, revoked BOOLEAN);",
            "CREATE TABLE refresh_tokens (token VARCHAR(255) PRIMARY KEY, subject VARCHAR(255), expiration_time BIGINT, issued_at BIGINT, revoked BOOLEAN);",
            false),
    ORACLE(DatabaseCapability.ORACLE_CLIENT,
            "CREATE TABLE IF NOT EXISTS access_tokens (token VARCHAR(255) PRIMARY KEY, subject VARCHAR(255), expiration_time NUMBER, issued_at NUMBER, revoked BOOLEAN);",
            "CREATE TABLE IF NOT EXISTS refresh_tokens (token VARCHAR(255) PRIMARY KEY, subject VARCHAR(255), expiration_time NUMBER, issued_at NUMBER, revoked BOOLEAN);",
            true);

    private final String clientType;
    private final String accessTokensTableQuery;
    private final String refreshTokensTableQuery;
    private final boolean supportsIfNotExists;

    DatabaseInitialization(String clientType, String accessTokensTableQuery, String refreshTokensTableQuery,
            boolean supportsIfNotExists) {
        this.clientType = clientType;
        this.accessTokensTableQuery = accessTokensTableQuery;
        this.refreshTokensTableQuery = refreshTokensTableQuery;
        this.supportsIfNotExists = supportsIfNotExists;
    }

    public static DatabaseInitialization of(String clientType) {
        return switch (clientType) {
            case DatabaseCapability.POSTGRESQL_CLIENT -> POSTGRESQL;
            case DatabaseCapability.MSSQL_CLIENT -> MSSQL;
            case DatabaseCapability.MYSQL_CLIENT -> MYSQL;
            case DatabaseCapability.DB2_CLIENT -> DB2;
            case DatabaseCapability.ORACLE_CLIENT -> ORACLE;
            default -> throw new IllegalArgumentException("Unknown sql client " + clientType);
        };
    }

    public String getClientType() {
        return clientType;
    }

    public String getAccessTokensTableQuery() {
        return accessTokensTableQuery;
    }

    public String getRefreshTokensTableQuery() {
        return refreshTokensTableQuery;
    }

    public boolean supportsIfNotExists() {
        return supportsIfNotExists;
    }
}
