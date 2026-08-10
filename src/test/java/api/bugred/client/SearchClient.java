package api.bugred.client;

import api.bugred.model.FullUserResponse;
import core.config.ConfigManager;
import core.specs.ApiSpec;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class SearchClient {
    public Response searchByEmailRaw(String email)
    {
        return given()
                .spec(ApiSpec.requestSpec(ConfigManager.getBugredUrl()))
                .queryParam("query", email)
                .when()
                .get("/tasks/rest/magicsearch");
    }

    public FullUserResponse searchByEmail(String email)
    {
        return searchByEmailRaw(email)
                .then()
                .statusCode(200)
                .extract()
                .as(FullUserResponse.class);
    }
}
