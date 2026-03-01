package api;

import api.client.UserClient;
import core.BaseTest;
import core.specs.ApiSpec;
import core.specs.ResponseSpec;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

public class FirstApiTest extends BaseTest {

    @Test
    void get_shouldReturn200() {
        given()
                .spec(ApiSpec.requestSpec())
                .when()
                .get()
                .then()
                .spec(ResponseSpec.successResponse());
    }

    private final UserClient userClient = new UserClient();

    @Test
    void getUsers_shouldReturnUsersList() {
        userClient.getUsers()
                .then()
                .spec(ResponseSpec.successResponse())
                .body("[0].id", notNullValue());
    }
}