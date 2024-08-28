package io.quarkiverse.quarkus.security.token.runtime;

import java.util.function.Supplier;

import io.quarkus.runtime.annotations.Recorder;

@Recorder
public class DatabaseTokenRecorder {

    public Supplier<DatabaseCababilities> createDatabaseClient(String clientType) {
        return () -> DatabaseCababilities.of(clientType);
    }
}
