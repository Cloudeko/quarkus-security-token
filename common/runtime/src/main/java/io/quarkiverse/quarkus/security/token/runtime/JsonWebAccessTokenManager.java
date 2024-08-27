package io.quarkiverse.quarkus.security.token.runtime;

import java.util.Objects;

import jakarta.inject.Inject;

import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.logging.Logger;

import io.quarkiverse.quarkus.security.token.access.AccessToken;
import io.quarkiverse.quarkus.security.token.access.AccessTokenManager;
import io.quarkus.security.identity.SecurityIdentity;
import io.quarkus.vertx.http.runtime.security.QuarkusHttpUser;
import io.smallrye.jwt.auth.principal.JWTParser;
import io.smallrye.jwt.build.Jwt;
import io.smallrye.jwt.build.JwtClaimsBuilder;
import io.smallrye.mutiny.Uni;
import io.vertx.ext.auth.User;

public class JsonWebAccessTokenManager implements AccessTokenManager {

    private static final Logger log = Logger.getLogger(JsonWebAccessTokenManager.class);

    private final JWTParser parser;
    private final CommonAccessTokenConfig config;

    @Inject
    public JsonWebAccessTokenManager(JWTParser parser, CommonAccessTokenConfig config) {
        this.parser = parser;
        this.config = config;
    }

    @Override
    public AccessToken createAccessToken(User user) {
        if (!(user instanceof QuarkusHttpUser quarkusUser)) {
            throw new IllegalArgumentException("User must be an instance of QuarkusHttpUser");
        }

        Objects.requireNonNull(quarkusUser.getSecurityIdentity(), "SecurityIdentity is null");

        JwtClaimsBuilder claims = Jwt.claims();
        SecurityIdentity securityIdentity = quarkusUser.getSecurityIdentity();

        claims.subject(securityIdentity.getPrincipal().getName());

        config.issuer().ifPresent(claims::issuer);
        config.audience().ifPresent(claims::audience);

        config.accessTokenLifespan().ifPresent(claims::expiresIn);

        config.groups().ifPresent(claims::groups);
        config.scopes().ifPresent(claims::scope);

        log.debugf("Creating access token for %s", securityIdentity.getPrincipal().getName());

        if (config.privateKey().isPresent()) {
            throw new UnsupportedOperationException("Private key content signing is not supported yet");
        }

        if (config.privateKeyLocation().isPresent()) {
            return new BasicAccessToken(claims.sign(config.privateKeyLocation().get()));
        }

        return new BasicAccessToken(claims.sign());
    }

    @Override
    public Uni<Boolean> verifyToken(AccessToken token) {
        try {
            JsonWebToken jwt = parser.parse(token.getAccessToken());
            return Uni.createFrom().item(true);
        } catch (Exception e) {
            log.debug("Failed to verify token", e);
            return Uni.createFrom().item(false);
        }
    }
}
