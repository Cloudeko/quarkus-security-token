package io.quarkiverse.quarkus.security.token.runtime;

import java.util.List;
import java.util.function.Supplier;

import io.quarkus.runtime.annotations.Recorder;

@Recorder
public class DatabaseTokenRecorder {

    public Supplier<DatabaseCapabilities> createDatabaseClient(String clientType) {
        return () -> DatabaseCapabilities.of(clientType);
    }

    public Supplier<DatabaseTokenInitializer.InitializerProperties> createUserAccountInitializerProps(
            List<DatabaseTokenInitializer.InitializerProperties.TableSchema> tableSchemas) {
        return () -> new DatabaseTokenInitializer.InitializerProperties(tableSchemas);
    }
}
