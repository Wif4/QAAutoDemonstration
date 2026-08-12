package api.bugred.steps;

import api.bugred.model.*;
import core.api.ApiClientManager;
import io.restassured.path.json.JsonPath;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;

public class UserSteps {
    public static Optional <FullUserResponse> registerAndWait(RegisterUserRequest registerUserRequest) {
        ApiClientManager
                .getRegisterClient()
                .doRegister(registerUserRequest);
        return searchUserByEmail(registerUserRequest.getEmail());
    }

    public static Optional <FullUserResponse> searchUserByEmail(String email) {

        if (email == null) {throw new IllegalArgumentException("email cannot be null in searchUserByEmail step");}
        JsonPath jsonPath = new JsonPath(searchResponseByEmail(email));
        return jsonPath
                .getList("results", FullUserResponse.class)
                .stream()
                .findFirst();
    }

    public static String searchResponseByEmail(String email) {

        return await()
                .pollDelay(1,TimeUnit.SECONDS)
                .atMost(10, TimeUnit.SECONDS)
                .pollInterval(500, TimeUnit.MILLISECONDS)
                .ignoreExceptions()
                .until(
                        () -> ApiClientManager.getSearchClient().searchByEmailRaw(email),
                        response -> (response.getStatusCode() == 231 ||
                                response.getStatusCode() == 230 ||
                                response.getStatusCode() == 232 )
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
