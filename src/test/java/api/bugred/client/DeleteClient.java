package api.bugred.client;

import core.config.ConfigManager;
import core.specs.ApiSpec;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class DeleteClient {

    public Response doDeleteRaw (String email)
    {
        return  given()
                .spec(ApiSpec.requestSpec(ConfigManager.getBugredUrl()))
                .queryParam("email", email)
                .when()
                .post("/tasks/rest/deleteuser");
    }

    public String doDelete (String email)
    {
        return  doDeleteRaw(email)
                .then()
                .statusCode(200)
                .extract()
                .asString();
    }
}
