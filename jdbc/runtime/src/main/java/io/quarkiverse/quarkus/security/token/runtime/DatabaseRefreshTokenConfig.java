package io.quarkiverse.quarkus.security.token.runtime;

import io.quarkus.runtime.annotations.ConfigPhase;
import io.quarkus.runtime.annotations.ConfigRoot;
import io.smallrye.config.ConfigMapping;

import java.util.Optional;

@ConfigRoot(phase = ConfigPhase.RUN_TIME)
@ConfigMapping(prefix = "quarkus.security.token.refresh")
public interface DatabaseRefreshTokenConfig {

    /**
     * The prefix to use for the refresh token.
     */
    Optional<String> prefix();
}
