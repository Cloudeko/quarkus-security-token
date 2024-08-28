package io.quarkiverse.quarkus.security.token.refresh;

import io.smallrye.mutiny.Uni;
import io.vertx.ext.auth.User;

public interface RefreshTokenUserProvider {
    Uni<User> getUser(RefreshToken refreshToken);
}
