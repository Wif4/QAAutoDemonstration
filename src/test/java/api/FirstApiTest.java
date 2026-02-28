package api;

import core.BaseTest;
import core.specs.ApiSpec;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class FirstApiTest extends BaseTest {

    @Test
    void get_shouldReturn200() {
        given()
                .spec(ApiSpec.requestSpec())
                .when()
                .get()
                .then()
                .statusCode(200);
    }
}