package api.bugred.steps;

import api.bugred.model.FullUserResponse;
import api.bugred.model.RegisterUser;
import core.api.ApiClientManager;
import io.restassured.path.json.JsonPath;

import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;

public class UserSteps {
    public FullUserResponse registerAndWait(RegisterUser registerUser) {
        ApiClientManager
                .getRegisterClient()
                .doRegister(registerUser);
        return searchUserByEmail(registerUser.getEmail());
    }

    public FullUserResponse searchUserByEmail(String email) {
        String responseSearched = await()
                .atMost(10, TimeUnit.SECONDS)
                .pollInterval(500, TimeUnit.MILLISECONDS)
                .ignoreExceptions()
                .until(
                        () -> ApiClientManager.getSearchClient().searchByEmailRaw(email),
                        response -> response.getStatusCode() == 231
                ).asString();
        JsonPath jsonPath = new JsonPath(responseSearched);
        return jsonPath
                .getList("results", FullUserResponse.class)
                .getFirst();
    }
}
