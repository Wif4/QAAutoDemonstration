package api.client;

import core.specs.ApiSpec;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class UserClient {

    public Response getUsers() {
        return given()
                .spec(ApiSpec.requestSpec())
                .when()
                .get("/users");
    }
}