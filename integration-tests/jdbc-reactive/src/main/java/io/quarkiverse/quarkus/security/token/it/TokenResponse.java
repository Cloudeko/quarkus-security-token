package io.quarkiverse.quarkus.security.token.it;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.quarkiverse.quarkus.security.token.Token;

public class TokenResponse {

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("token_type")
    private String tokenType;

    @JsonProperty("expires_in")
    private int expiresIn;

    @JsonProperty("refresh_token")
    private String refreshToken;

    public TokenResponse() {
    }

    public TokenResponse(Token sessionToken) {
        this.accessToken = sessionToken.getRawAccessToken();
        this.tokenType = sessionToken.getType();
        this.expiresIn = 3600;
        this.refreshToken = sessionToken.getRawRefreshToken();
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public String getRefreshToken() {
        return refreshToken;
    }
}
