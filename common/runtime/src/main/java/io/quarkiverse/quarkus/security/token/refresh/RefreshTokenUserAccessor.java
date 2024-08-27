package io.quarkiverse.quarkus.security.token.refresh;

import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.ext.auth.User;

public interface RefreshTokenUserAccessor {
    Uni<User> getUser(RefreshToken refreshToken);
}
