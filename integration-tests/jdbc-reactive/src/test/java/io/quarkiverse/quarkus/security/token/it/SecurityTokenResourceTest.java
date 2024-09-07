package io.quarkiverse.quarkus.security.token.it;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class SecurityTokenResourceTest {

    @Test
    public void testHelloEndpoint() {
        TokenResponse token = given()
                .queryParam("subject", "test")
                .when().post("/security-token")
                .then()
                .statusCode(200)
                .body(
                        "access_token", notNullValue(),
                        "token_type", is("bearer"),
                        "expires_in", notNullValue(),
                        "refresh_token", notNullValue())
                .extract().as(TokenResponse.class);

        given()
                .header("Authorization", "Bearer " + token.getAccessToken())
                .when().get("/security-token")
                .then()
                .statusCode(200)
                .body(is("Hello test"));
    }

    @Test
    public void testSwapToken() {
        TokenResponse token = given()
                .queryParam("subject", "test")
                .when().post("/security-token")
                .then()
                .statusCode(200)
                .body(
                        "access_token", notNullValue(),
                        "token_type", is("bearer"),
                        "expires_in", notNullValue(),
                        "refresh_token", notNullValue())
                .extract().as(TokenResponse.class);

        TokenResponse newToken = given()
                .queryParam("refresh_token", token.getRefreshToken())
                .when().post("/security-token/swap")
                .then()
                .statusCode(200)
                .body(
                        "access_token", notNullValue(),
                        "token_type", is("bearer"),
                        "expires_in", notNullValue(),
                        "refresh_token", notNullValue())
                .extract().as(TokenResponse.class);

        given()
                .header("Authorization", "Bearer " + newToken.getAccessToken())
                .when().get("/security-token")
                .then()
                .statusCode(200)
                .body(is("Hello test"));
    }
}
