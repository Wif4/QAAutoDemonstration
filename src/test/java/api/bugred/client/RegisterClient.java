package api.bugred.client;

import api.bugred.model.FullUserResponse;
import core.specs.ApiSpec;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class RegisterClient {
    public FullUserResponse doRegister(String json)
    {
       return doRegisterRaw(json)
                .then()
               .statusCode(200)
               .extract().
               as(FullUserResponse.class);
    }

    public Response doRegisterRaw(String json)
    {
        return given()
                .spec(ApiSpec.requestSpec())
                .body(json)
                .when()
                .post("/tasks/rest/doregister");
    }
}
