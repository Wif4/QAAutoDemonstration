package api.jsonplaceholder.client;

import api.jsonplaceholder.model.User;
import core.config.ConfigManager;
import core.specs.ApiSpec;
import core.specs.ResponseSpec;
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.Response;

import java.util.List;

import static io.restassured.RestAssured.given;

public class UserClient {

    public Response getUsersRaw() {
        return given()
                .spec(ApiSpec.requestSpec(ConfigManager.getJsonPlaceholderUrl()))
                .when()
                .get("/users");
    }

    public List<User> getUsers() {
        return getUsersRaw()
                .then()
                .extract()
                .as(new TypeRef<>() {});
    }

    public User getUserById(Integer userId) {

        return  getUserByIdRaw(userId)
                .then()
                .spec(ResponseSpec.successResponse())
                .extract()
                .as(User.class);
    }
    public Response getUserByIdRaw(Integer userId) {
        return given()
                .spec(ApiSpec.requestSpec(ConfigManager.getJsonPlaceholderUrl()))
                .pathParam("id", userId)
                .when()
                .get("/users/{id}");
    }

    public Response createUserRaw(String json) {
        return given()
                .spec(ApiSpec.requestSpec(ConfigManager.getJsonPlaceholderUrl()))
                .body(json)
                .when()
                .post("/users");
    }

    public User createUser(String json) {
        return createUserRaw(json)
                .then()
                .statusCode(201)
                .extract()
                .as(User.class);
    }

}