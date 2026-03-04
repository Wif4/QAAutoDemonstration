package api.bugred.client;

import api.bugred.model.FullUserResponse;
import api.bugred.model.RegisterUser;
import core.config.ConfigManager;
import core.specs.ApiSpec;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class RegisterClient {

    @Step("Register user with email: {registerUser.email}")
    public FullUserResponse doRegister(RegisterUser registerUser)
    {
       return doRegisterRaw(registerUser)
                .then()
               .statusCode(200)
               .extract().
               as(FullUserResponse.class);
    }

    public Response doRegisterRaw(RegisterUser registerUser)
    {
        return given()
                .spec(ApiSpec.requestSpec(ConfigManager.getBugredUrl()))
                .body(registerUser)
                .when()
                .post("/tasks/rest/doregister");
    }
}
