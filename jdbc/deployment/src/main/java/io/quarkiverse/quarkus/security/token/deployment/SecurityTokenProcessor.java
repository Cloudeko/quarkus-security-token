package io.quarkiverse.quarkus.security.token.deployment;

import static io.quarkus.deployment.Capability.*;
import static io.quarkus.deployment.annotations.ExecutionTime.STATIC_INIT;

import java.util.Arrays;

import jakarta.enterprise.context.Dependent;

import io.quarkiverse.quarkus.security.token.runtime.DatabaseCababilities;
import io.quarkiverse.quarkus.security.token.runtime.DatabaseTokenRecorder;
import io.quarkus.arc.deployment.SyntheticBeanBuildItem;
import io.quarkus.builder.item.SimpleBuildItem;
import io.quarkus.deployment.Capabilities;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.annotations.Record;
import io.quarkus.deployment.builditem.FeatureBuildItem;

class SecurityTokenProcessor {

    private static final String FEATURE = "security-token-jdbc";

    private static final String[] SUPPORTED_REACTIVE_CLIENTS = new String[] { REACTIVE_PG_CLIENT, REACTIVE_MYSQL_CLIENT,
            REACTIVE_MSSQL_CLIENT, REACTIVE_DB2_CLIENT, REACTIVE_ORACLE_CLIENT };

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }

    @BuildStep
    ReactiveClientBuildItem findReactiveClient(Capabilities capabilities) {
        final String reactiveClient = capabilities.getCapabilities().stream()
                .filter(c -> Arrays.asList(SUPPORTED_REACTIVE_CLIENTS).contains(c))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No supported reactive SQL client found"));

        return new ReactiveClientBuildItem(reactiveClient);
    }

    @BuildStep
    @Record(STATIC_INIT)
    SyntheticBeanBuildItem createBasicUserInitializerConfig(DatabaseTokenRecorder recorder,
            ReactiveClientBuildItem reactiveClient) {
        return SyntheticBeanBuildItem
                .configure(DatabaseCababilities.class)
                .supplier(recorder.createDatabaseClient(reactiveClient.getReactiveClient()))
                .scope(Dependent.class)
                .unremovable()
                .done();
    }

    private static final class ReactiveClientBuildItem extends SimpleBuildItem {

        private final String reactiveClient;

        private ReactiveClientBuildItem(String reactiveClient) {
            this.reactiveClient = reactiveClient;
        }

        public String getReactiveClient() {
            return reactiveClient;
        }
    }
}
