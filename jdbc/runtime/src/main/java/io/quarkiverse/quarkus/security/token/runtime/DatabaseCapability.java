package io.quarkiverse.quarkus.security.token.runtime;

public interface DatabaseCapability {
    String POSTGRESQL_CLIENT = "postgresql";
    String MSSQL_CLIENT = "mssql";
    String MYSQL_CLIENT = "mysql";
    String DB2_CLIENT = "db2";
    String ORACLE_CLIENT = "oracle";
}
