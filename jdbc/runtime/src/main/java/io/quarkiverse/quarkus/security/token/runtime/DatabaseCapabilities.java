package io.quarkiverse.quarkus.security.token.runtime;

public record DatabaseCapabilities(String clientType) {
    public DatabaseCapabilities {
        if (clientType == null) {
            throw new IllegalArgumentException("clientType must not be null");
        }
    }

    public static DatabaseCapabilities of(String clientType) {
        String capability = switch (clientType) {
            case "reactive-pg-client" -> DatabaseCapability.POSTGRESQL_CLIENT;
            case "reactive-mssql-client" -> DatabaseCapability.MSSQL_CLIENT;
            case "reactive-db2-client" -> DatabaseCapability.DB2_CLIENT;
            case "reactive-oracle-client" -> DatabaseCapability.ORACLE_CLIENT;
            case "reactive-mysql-client" -> DatabaseCapability.MYSQL_CLIENT;
            default -> throw new IllegalArgumentException("Unknown reactive SQL client " + clientType);
        };

        return new DatabaseCapabilities(capability);
    }
}
