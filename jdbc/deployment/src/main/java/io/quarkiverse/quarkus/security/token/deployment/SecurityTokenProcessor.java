package io.quarkiverse.quarkus.security.token.deployment;

import io.quarkiverse.quarkus.security.token.runtime.*;
import io.quarkus.arc.deployment.AdditionalBeanBuildItem;
import io.quarkus.arc.deployment.SyntheticBeanBuildItem;
import io.quarkus.builder.item.SimpleBuildItem;
import io.quarkus.deployment.Capabilities;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.annotations.Record;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import jakarta.enterprise.context.Dependent;

import java.util.Arrays;
import java.util.List;

import static io.quarkus.deployment.Capability.*;
import static io.quarkus.deployment.annotations.ExecutionTime.STATIC_INIT;

class SecurityTokenProcessor {

    private static final String FEATURE = "security-token-jdbc";

    private static final String[] SUPPORTED_REACTIVE_CLIENTS = new String[] { REACTIVE_PG_CLIENT, REACTIVE_MYSQL_CLIENT,
            REACTIVE_MSSQL_CLIENT, REACTIVE_DB2_CLIENT, REACTIVE_ORACLE_CLIENT };

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }

    @BuildStep
    AdditionalBeanBuildItem createAdditionalBeans() {
        return AdditionalBeanBuildItem.builder()
                .addBeanClass(DatabaseTokenProducer.class)
                .addBeanClass(DatabaseTokenInitializer.class)
                .setUnremovable()
                .build();
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
    DatabaseCapabilitiesBuildItem createDatabaseCapabilities(ReactiveClientBuildItem reactiveClient) {
        return new DatabaseCapabilitiesBuildItem(
                DatabaseCapability.reactiveClientToDatabaseClient(reactiveClient.getReactiveClient()));
    }

    @BuildStep
    @Record(STATIC_INIT)
    SyntheticBeanBuildItem createDatabaseCapabilities(DatabaseTokenRecorder recorder,
            DatabaseCapabilitiesBuildItem database) {
        return SyntheticBeanBuildItem
                .configure(DatabaseCapabilities.class)
                .supplier(recorder.createDatabaseClient(database.getClientType()))
                .scope(Dependent.class)
                .unremovable()
                .done();
    }

    @BuildStep
    @Record(STATIC_INIT)
    SyntheticBeanBuildItem createDatabaseInitializer(DatabaseTokenRecorder recorder,
            DatabaseCapabilitiesBuildItem database) {
        final DatabaseInitialization initialization = DatabaseInitialization.of(database.getClientType());
        final List<DatabaseTokenInitializer.InitializerProperties.TableSchema> tableSchemas = List.of(
                new DatabaseTokenInitializer.InitializerProperties.TableSchema(initialization.getAccessTokensTable(),
                        initialization.getAccessTokensTableQuery(), initialization.supportsIfNotExists()),
                new DatabaseTokenInitializer.InitializerProperties.TableSchema(initialization.getRefreshTokensTable(),
                        initialization.getRefreshTokensTableQuery(), initialization.supportsIfNotExists()));

        return SyntheticBeanBuildItem
                .configure(DatabaseTokenInitializer.InitializerProperties.class)
                .supplier(recorder.createUserAccountInitializerProps(tableSchemas))
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

    private static final class DatabaseCapabilitiesBuildItem extends SimpleBuildItem {

        private final String clientType;

        private DatabaseCapabilitiesBuildItem(String clientType) {
            this.clientType = clientType;
        }

        public String getClientType() {
            return clientType;
        }
    }
}
