package io.quarkiverse.quarkus.security.token.jwt;

import java.time.Duration;
import java.util.Optional;
import java.util.Set;

import io.quarkus.runtime.annotations.ConfigPhase;
import io.quarkus.runtime.annotations.ConfigRoot;
import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithName;

@ConfigRoot(phase = ConfigPhase.RUN_TIME)
@ConfigMapping(prefix = "quarkus.security.token.access.jwt")
public interface JsonWebTokenConfig {

    /**
     * The issuer of the token.
     */
    Optional<String> issuer();

    /**
     * The audience of the token.
     */
    Optional<String> audience();

    /**
     * The access token lifespan.
     */
    Optional<Duration> accessTokenLifespan();

    /**
     * The groups of the token.
     */
    Optional<Set<String>> groups();

    /**
     * The scopes of the token.
     */
    Optional<Set<String>> scopes();

    /**
     * The private key used to sign the token.
     */
    @WithName("key.private")
    Optional<String> privateKey();

    /**
     * The public key used to sign the token.
     */
    @WithName("key.private.location")
    Optional<String> privateKeyLocation();
}
