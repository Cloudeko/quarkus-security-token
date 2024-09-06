package io.quarkiverse.quarkus.security.token.it;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

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
}
