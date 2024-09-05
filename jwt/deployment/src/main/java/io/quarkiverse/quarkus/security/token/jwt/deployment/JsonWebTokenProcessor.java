package io.quarkiverse.quarkus.security.token.jwt.deployment;

import io.quarkiverse.quarkus.security.token.jwt.DefaultTokenProducer;
import io.quarkus.arc.deployment.AdditionalBeanBuildItem;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.FeatureBuildItem;

public class JsonWebTokenProcessor {

    private static final String FEATURE = "security-token-common";

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }

    @BuildStep
    AdditionalBeanBuildItem createProducerBean() {
        return AdditionalBeanBuildItem.unremovableOf(DefaultTokenProducer.class);
    }
}
