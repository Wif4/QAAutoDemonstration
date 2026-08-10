package api.bugred.client;

import api.bugred.model.DeleteUserResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    public DeleteUserResponse doDelete (String email)
    {
        return  parseDeleteResponse(
                doDeleteRaw(email)
                        .then()
                        .statusCode(200)
                        .extract()
                        .asString()
        );
    }

    private DeleteUserResponse parseDeleteResponse(String response) {

        if ((response == null) || !response.contains("type"))
        {
            throw new RuntimeException("Invalid Bugred response: JSON object not found");
        }

        int index = response.indexOf("type");
        String formattedString = response.substring(index);
        String resultString = "{\"" + formattedString;
        DeleteUserResponse deleteUserResponse;

        ObjectMapper mapper = new ObjectMapper();
        try {
            deleteUserResponse = mapper.readValue(resultString,DeleteUserResponse.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to parse Bugred delete response", e);
        }

        return deleteUserResponse;
    }
}
