package io.quarkiverse.quarkus.security.token.runtime;

import io.quarkiverse.quarkus.security.token.refresh.RefreshToken;
import io.quarkiverse.quarkus.security.token.refresh.RefreshTokenUserProvider;
import io.quarkus.security.runtime.QuarkusPrincipal;
import io.quarkus.security.runtime.QuarkusSecurityIdentity;
import io.quarkus.vertx.http.runtime.security.QuarkusHttpUser;
import io.smallrye.mutiny.Uni;
import io.vertx.ext.auth.User;

public class DefaultRefreshTokenUserProvider implements RefreshTokenUserProvider {
    @Override
    public Uni<User> getUser(RefreshToken refreshToken) {
        QuarkusPrincipal principal = new QuarkusPrincipal(refreshToken.getSubject());
        QuarkusSecurityIdentity identity = QuarkusSecurityIdentity.builder().setPrincipal(principal).build();
        QuarkusHttpUser user = new QuarkusHttpUser(identity);
        return Uni.createFrom().item(user);
    }
}
