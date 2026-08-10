package api.bugred.client;

import api.bugred.model.FullUserResponse;
import api.bugred.model.RegisterUserRequest;
import core.config.ConfigManager;
import core.specs.ApiSpec;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class RegisterClient {

    @Step("Register user with email: {registerUser.email}")
    public FullUserResponse doRegister(RegisterUserRequest registerUserRequest)
    {
       return doRegisterRaw(registerUserRequest)
                .then()
               .statusCode(200)
               .extract().
               as(FullUserResponse.class);
    }

    public Response doRegisterRaw(RegisterUserRequest registerUserRequest)
    {
        return given()
                .spec(ApiSpec.requestSpec(ConfigManager.getBugredUrl()))
                .body(registerUserRequest)
                .when()
                .post("/tasks/rest/doregister");
    }
}
