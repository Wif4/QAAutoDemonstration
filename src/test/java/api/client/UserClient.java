package api.client;

import api.models.User;
import core.specs.ApiSpec;
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.Response;

import java.util.List;

import static io.restassured.RestAssured.given;

public class UserClient {

    public Response getUsersRaw() {
        return given()
                .spec(ApiSpec.requestSpec())
                .when()
                .get("/users");
    }

    public List<User> getUsers() {
        return getUsersRaw()
                .then()
                .extract()
                .as(new TypeRef<List<User>>() {});
    }
}