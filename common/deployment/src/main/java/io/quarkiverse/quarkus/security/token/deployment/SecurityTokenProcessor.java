package io.quarkiverse.quarkus.security.token.deployment;

import io.quarkiverse.quarkus.security.token.runtime.DefaultTokenManager;
import io.quarkiverse.quarkus.security.token.runtime.JsonWebAccessTokenManager;
import io.quarkus.arc.deployment.AdditionalBeanBuildItem;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.FeatureBuildItem;

class SecurityTokenProcessor {

    private static final String FEATURE = "security-token";

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }

    @BuildStep
    AdditionalBeanBuildItem setup() {
        AdditionalBeanBuildItem.Builder builder = AdditionalBeanBuildItem.builder();
        builder.addBeanClass(DefaultTokenManager.class);
        builder.addBeanClass(JsonWebAccessTokenManager.class);

        return builder.build();
    }
}
