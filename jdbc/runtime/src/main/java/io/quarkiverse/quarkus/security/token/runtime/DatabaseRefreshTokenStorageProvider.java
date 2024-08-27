package io.quarkiverse.quarkus.security.token.runtime;

import io.quarkiverse.quarkus.security.token.refresh.RefreshToken;
import io.quarkiverse.quarkus.security.token.refresh.RefreshTokenStorageProvider;
import io.smallrye.mutiny.Uni;
import io.vertx.sqlclient.Pool;
import io.vertx.sqlclient.Tuple;
import org.jboss.logging.Logger;

public class DatabaseRefreshTokenStorageProvider implements RefreshTokenStorageProvider {

    private static final Logger log = Logger.getLogger(DatabaseRefreshTokenStorageProvider.class);

    private static final String TOKEN_STATE_INSERT_FAILED = "Failed to insert refresh token state into database";
    private static final String FAILED_TO_ACQUIRE_TOKEN = "Failed to acquire refresh token";
    private static final String FAILED_TO_REMOVE_TOKEN = "Failed to remove refresh token";
    private static final String TOKEN_STATE_REVOKE_FAILED = "Failed to revoke refresh token";

    private final String clientType;
    private final Pool pool;

    public DatabaseRefreshTokenStorageProvider(String clientType, Pool pool) {
        this.clientType = clientType;
        this.pool = pool;
    }

    @Override
    public Uni<Void> storeRefreshToken(RefreshToken refreshToken) {
        return Uni.createFrom()
                .completionStage(pool
                        .withTransaction(client -> client
                                .preparedQuery(DatabaseQuery.STORE_REFRESH_TOKEN.getQuery(clientType))
                                .execute(Tuple.of(refreshToken.getSubject(), refreshToken.getRefreshToken(), refreshToken.getExpirationTime())))
                        .toCompletionStage())
                .onItem().transformToUni(result -> result.rowCount() == 1 ? Uni.createFrom().voidItem() : Uni.createFrom().failure(new RuntimeException(TOKEN_STATE_INSERT_FAILED)))
                .onFailure().invoke(throwable -> log.error("Failed to store refresh token", throwable));
    }

    @Override
    public Uni<Void> removeRefreshToken(String refreshToken) {
        return Uni.createFrom()
                .completionStage(pool
                        .withTransaction(client -> client
                                .preparedQuery(DatabaseQuery.REMOVE_REFRESH_TOKEN.getQuery(clientType))
                                .execute(Tuple.of(refreshToken)))
                        .toCompletionStage())
                .onItem().transformToUni(result -> result.rowCount() == 1 ? Uni.createFrom().voidItem() : Uni.createFrom().failure(new RuntimeException(FAILED_TO_REMOVE_TOKEN)))
                .onFailure().invoke(throwable -> log.error("Failed to remove refresh token", throwable));
    }

    @Override
    public Uni<RefreshToken> getRefreshToken(String refreshToken) {
        return Uni.createFrom()
                .completionStage(pool
                        .withTransaction(client -> client
                                .preparedQuery(DatabaseQuery.GET_REFRESH_TOKEN.getQuery(clientType))
                                .execute(Tuple.of(refreshToken)))
                        .toCompletionStage())
                .onItem().transformToUni(DatabaseUtil::processNullableRow)
                .onItem().transform(DatabaseRefreshToken::new)
                .onFailure().invoke(throwable -> log.error("Failed to get refresh token", throwable));
    }

    @Override
    public Uni<Void> revokeRefreshToken(String refreshToken) {
        return Uni.createFrom()
                .completionStage(pool
                        .withTransaction(client -> client
                                .preparedQuery(DatabaseQuery.REVOKE_REFRESH_TOKEN.getQuery(clientType))
                                .execute(Tuple.of(refreshToken)))
                        .toCompletionStage())
                .onItem().transformToUni(result -> result.rowCount() == 1 ? Uni.createFrom().voidItem() : Uni.createFrom().failure(new RuntimeException("Failed to revoke user")))
                .onFailure().invoke(throwable -> log.error("Failed to revoke refresh token", throwable));
    }

    @Override
    public Uni<Void> removeAllTokensForSubject(String subject) {
        return Uni.createFrom()
                .completionStage(pool
                        .withTransaction(client -> client
                                .preparedQuery(DatabaseQuery.REMOVE_ALL_TOKENS_FOR_SUBJECT.getQuery(clientType))
                                .execute(Tuple.of(subject)))
                        .toCompletionStage())
                .onItem().transformToUni(result -> Uni.createFrom().voidItem())
                .onFailure().transform(throwable -> new RuntimeException("Failed to remove all tokens for subject", throwable));
    }

    @Override
    public Uni<Void> removeAllInvalidTokens() {
        return Uni.createFrom()
                .completionStage(pool
                        .withTransaction(client -> client
                                .preparedQuery(DatabaseQuery.REMOVE_ALL_INVALID_TOKENS.getQuery(clientType))
                                .execute())
                        .toCompletionStage())
                .onItem().transformToUni(result -> Uni.createFrom().voidItem())
                .onFailure().transform(throwable -> new RuntimeException("Failed to remove invalid tokens", throwable));
    }

    @Override
    public Uni<Void> removeAllTokens() {
        return Uni.createFrom()
                .completionStage(pool
                        .withTransaction(client -> client
                                .preparedQuery(DatabaseQuery.REMOVE_ALL_TOKENS.getQuery(clientType))
                                .execute())
                        .toCompletionStage())
                .onItem().transformToUni(result -> Uni.createFrom().voidItem())
                .onFailure().transform(throwable -> new RuntimeException("Failed to remove all tokens", throwable));
    }
}
