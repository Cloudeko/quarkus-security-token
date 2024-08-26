package io.quarkiverse.quarkus.security.token.it;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class SecurityTokenResourceTest {

    @Test
    public void testHelloEndpoint() {
        given()
                .when().get("/security-token")
                .then()
                .statusCode(200)
                .body(is("Hello security-token"));
    }
}
