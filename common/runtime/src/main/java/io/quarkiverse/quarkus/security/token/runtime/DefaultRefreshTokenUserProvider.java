package io.quarkiverse.quarkus.security.token.runtime;

import io.quarkiverse.quarkus.security.token.TokenUserProvider;
import io.quarkiverse.quarkus.security.token.refresh.RefreshTokenCredential;
import io.quarkus.security.runtime.QuarkusPrincipal;
import io.quarkus.security.runtime.QuarkusSecurityIdentity;
import io.quarkus.vertx.http.runtime.security.QuarkusHttpUser;
import io.smallrye.mutiny.Uni;
import io.vertx.ext.auth.User;

public class DefaultRefreshTokenUserProvider implements TokenUserProvider<RefreshTokenCredential> {

    @Override
    public Uni<User> getUser(RefreshTokenCredential refreshToken) {
        QuarkusPrincipal principal = new QuarkusPrincipal(refreshToken.getSubject());
        QuarkusSecurityIdentity identity = QuarkusSecurityIdentity.builder().setPrincipal(principal).build();
        QuarkusHttpUser user = new QuarkusHttpUser(identity);
        return Uni.createFrom().item(user);
    }
}
