package api.client;

import api.models.User;
import core.specs.ApiSpec;
import core.specs.ResponseSpec;
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

    public User getUserById(Integer userId) {

        return  given()
                .spec(ApiSpec.requestSpec())
                .pathParam("id", userId)
                .when()
                .get("/users/{id}")
                .then()
                .spec(ResponseSpec.successResponse())
                .extract()
                .as(User.class);
    }

    public User getUserByNotFoundId(Integer userId) {

        return  given()
                .spec(ApiSpec.requestSpec())
                .pathParam("id", userId)
                .when()
                .get("/users/{id}")
                .then()
                .spec(ResponseSpec.notFoundResponse())
                .extract()
                .as(User.class);
    }

    public User getUserByInvalidId(char userId) {

        User user = given()
                .spec(ApiSpec.requestSpec())
                .pathParam("id", userId)
                .when()
                .get("/users/{id}")
                .then()
                .spec(ResponseSpec.badRequestResponse())
                .extract()
                .as(User.class);
        return user;
    }
}