package api.bugred.steps;

import api.bugred.model.*;
import core.api.ApiClientManager;
import io.restassured.path.json.JsonPath;

import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;

public class UserSteps {
    public static FullUserRequestResponse registerAndWait(RegisterUserRequest registerUserRequest) {
        ApiClientManager
                .getRegisterClient()
                .doRegister(registerUserRequest);
        return searchUserByEmail(registerUserRequest.getEmail());
    }

    public static FullUserRequestResponse searchUserByEmail(String email) {

        JsonPath jsonPath = new JsonPath(searchResponseByEmail(email));

        return jsonPath
                .getList("results", FullUserRequestResponse.class)
                .getFirst();
    }

    public static String searchResponseByEmail(String email) {

        return await()
                .pollDelay(1,TimeUnit.SECONDS)
                .atMost(10, TimeUnit.SECONDS)
                .pollInterval(500, TimeUnit.MILLISECONDS)
                .ignoreExceptions()
                .until(
                        () -> ApiClientManager.getSearchClient().searchByEmailRaw(email),
                        response -> (response.getStatusCode() == 231 || response.getStatusCode() == 230)
                ).asString();
    }

    public static UpdateUserResponse updateUserOneField (UpdateUserRequest updateUser)
    {
        return ApiClientManager
                .getUpdateClient()
                .updateClient(updateUser);
    }

    public static DeleteUserResponse deleteUserByEmail (String email){

        return ApiClientManager
                .getDeleteClient()
                .doDelete(email);
    }
}
