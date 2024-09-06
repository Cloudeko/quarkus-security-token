package io.quarkiverse.quarkus.security.token.runtime;

import io.quarkus.runtime.annotations.Recorder;

import java.util.List;
import java.util.function.Supplier;

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
