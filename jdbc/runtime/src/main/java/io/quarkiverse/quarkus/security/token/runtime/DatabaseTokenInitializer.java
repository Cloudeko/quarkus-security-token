package io.quarkiverse.quarkus.security.token.runtime;

import io.quarkus.runtime.StartupEvent;
import io.smallrye.mutiny.Uni;
import io.vertx.core.Vertx;
import io.vertx.sqlclient.Pool;
import jakarta.enterprise.event.Observes;
import org.jboss.logging.Logger;

import java.util.List;

public class DatabaseTokenInitializer {

    private static final Logger log = Logger.getLogger(DatabaseTokenInitializer.class);
    private static final String FAILED_TO_CREATE_DB_TABLE = "unknown reason, please report the issue and create table manually";

    void initialize(@Observes StartupEvent event, Vertx vertx, Pool pool, InitializerProperties properties) {
        log.debug("Initializing token database tables");

        List<InitializerProperties.TableSchema> tableSchemas = properties.tables();
        if (tableSchemas == null || tableSchemas.isEmpty()) {
            log.warn("No table schemas provided, skipping database table creation");
            return;
        }

        tableSchemas.forEach(tableSchema -> {
            createDatabaseTable(pool, tableSchema.table(), tableSchema.ddl(), tableSchema.supportsIfNotExists());
        });
    }

    private static void createDatabaseTable(Pool pool, String table, String createTableDdl, boolean supportsIfTableNotExists) {
        log.debugf("Creating database table with query: %s", createTableDdl);

        Uni<String> tableCreationResult = Uni.createFrom()
                .completionStage(pool.query(createTableDdl).execute().toCompletionStage())
                .onItemOrFailure()
                .transformToUni((rows, throwable) -> {
                    if (throwable != null) {
                        return supportsIfTableNotExists
                                ? Uni.createFrom().item(throwable.getMessage())
                                : Uni.createFrom().nullItem();
                    }

                    return verifyTableExists(pool, table);
                });

        String errMsg = tableCreationResult.await().indefinitely();
        if (errMsg != null) {
            throw new RuntimeException("Failed to create database table: " + errMsg);
        }
    }

    private static Uni<String> verifyTableExists(Pool pool, String table) {
        return Uni.createFrom()
                .completionStage(pool.query("SELECT MAX(token) FROM " + table).execute().toCompletionStage())
                .map(rows -> {
                    if (rows != null && rows.columnsNames().size() == 1) {
                        return null; // Table exists
                    }
                    return FAILED_TO_CREATE_DB_TABLE;
                })
                .onFailure().recoverWithItem(throwable -> {
                    log.error("Create database query failed with: ", throwable);
                    return FAILED_TO_CREATE_DB_TABLE;
                });
    }

    public record InitializerProperties(List<TableSchema> tables) {

        public record TableSchema(String table, String ddl, boolean supportsIfNotExists) {
        }
    }
}
