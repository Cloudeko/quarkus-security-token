package io.quarkiverse.quarkus.security.token.runtime;

public interface DatabaseCapability {
    String POSTGRESQL_CLIENT = "postgresql";
    String MSSQL_CLIENT = "mssql";
    String MYSQL_CLIENT = "mysql";
    String DB2_CLIENT = "db2";
    String ORACLE_CLIENT = "oracle";

    static String reactiveClientToDatabaseClient(String reactiveClient) {
        return switch (reactiveClient) {
            case "io.quarkus.reactive-pg-client" -> POSTGRESQL_CLIENT;
            case "io.quarkus.reactive-mssql-client" -> MSSQL_CLIENT;
            case "io.quarkus.reactive-mysql-client" -> MYSQL_CLIENT;
            case "io.quarkus.reactive-db2-client" -> DB2_CLIENT;
            case "io.quarkus.reactive-oracle-client" -> ORACLE_CLIENT;
            default -> throw new IllegalArgumentException("Unknown reactive SQL client " + reactiveClient);
        };
    }
}
