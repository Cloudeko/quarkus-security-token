package io.quarkiverse.quarkus.security.token.runtime;

import java.util.Map;
import java.util.function.Supplier;

import io.quarkus.runtime.annotations.Recorder;

@Recorder
public class DatabaseTokenRecorder {

    public Supplier<DatabaseCapabilities> createDatabaseClient(String clientType) {
        return () -> DatabaseCapabilities.of(clientType);
    }

    public Supplier<DatabaseTokenInitializer.InitializerProperties> createUserAccountInitializerProps(
            Map<String, Boolean> tableSchemas) {
        return () -> new DatabaseTokenInitializer.InitializerProperties(tableSchemas);
    }
}
