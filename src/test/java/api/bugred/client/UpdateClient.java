package api.bugred.client;

import api.bugred.model.UpdateUser;
import core.config.ConfigManager;
import core.specs.ApiSpec;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class UpdateClient {
    public Response updateClientRaw(UpdateUser updateUser) {
        return given()
                .spec(ApiSpec.requestSpec(ConfigManager.getBugredUrl()))
                .body(updateUser)
                .when()
                .post("/tasks/rest/useronefield");
    }

    public UpdateUser updateClient(UpdateUser updateUser) {
        return updateClientRaw(updateUser)
                .then()
                .statusCode(200)
                .extract()
                .response()
                .as(UpdateUser.class);
    }
}