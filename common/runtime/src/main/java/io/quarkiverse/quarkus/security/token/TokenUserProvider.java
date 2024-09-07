package io.quarkiverse.quarkus.security.token;

import io.quarkus.security.credential.Credential;
import io.smallrye.mutiny.Uni;
import io.vertx.ext.auth.User;

public interface TokenUserProvider<T extends Credential> {
    Uni<User> getUser(T token);
}
