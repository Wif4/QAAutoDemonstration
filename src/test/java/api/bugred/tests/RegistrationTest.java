package api.bugred.tests;

import api.bugred.model.FullUserRequestResponse;
import api.bugred.model.RegisterUserRequest;
import api.bugred.model.UpdateUserRequest;
import api.bugred.testdata.UserTestDataFactory;
import core.BaseTest;
import core.api.ApiClientManager;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@Tag("api")
@Tag("bugred")
public class RegistrationTest extends BaseTest {

   @Test
    void doRegister_shouldReturnSuccessAndBody()
    {
        RegisterUserRequest expectedUser = UserTestDataFactory.getUniqueUser();

       FullUserRequestResponse userResponse = ApiClientManager
               .getRegisterClient().
               doRegister(expectedUser);

       assertThat(userResponse).isNotNull();
       assertThat(userResponse)
               .usingRecursiveComparison()
               .comparingOnlyFields("email", "name")
               .isEqualTo(expectedUser);
        assertThat(userResponse).extracting("password", "avatar").doesNotContainNull();
    }

    @Test
    void doRegister_shouldReturnSuccessErrorTypeAndEmailMessage() {
        RegisterUserRequest registerUserRequest = UserTestDataFactory.getUniqueUser();

        ApiClientManager
               .getRegisterClient().
                doRegisterRaw(registerUserRequest); //preparation

        RegisterUserRequest duplicateMailUser = UserTestDataFactory.getUserWithEmail(registerUserRequest.getEmail());

        String response = ApiClientManager
               .getRegisterClient().doRegisterRaw(duplicateMailUser)
                .then()
                .statusCode(200)
                .extract()
                .response()
                .getBody()
                .asString();

        JsonPath jsonPath = new JsonPath(response);
        String type = jsonPath.getString("type");
        String message = jsonPath.getString("message");

        assertThat(type).isEqualTo("error");
        assertThat(message).contains(registerUserRequest.getEmail());
    }

    @Test
    void doRegister_shouldReturnSuccessErrorTypeAndNameMessage() {
        RegisterUserRequest registerUserRequest = UserTestDataFactory.getUniqueUser();

        ApiClientManager
               .getRegisterClient()
                .doRegisterRaw(registerUserRequest); //preparation

        RegisterUserRequest duplicateNameUser = UserTestDataFactory.getUserWithName(registerUserRequest.getName());

        String response = ApiClientManager
               .getRegisterClient().doRegisterRaw(duplicateNameUser)
                .then()
                .statusCode(200)
                .extract()
                .response()
                .getBody()
                .asString();

        JsonPath jsonPath = new JsonPath(response);
        String type = jsonPath.getString("type");
        String message = jsonPath.getString("message");

        assertThat(type).isEqualTo("error");
        assertThat(message).contains(duplicateNameUser.getName());
    }

    @Test
    void registerUser_shouldBeFoundBySearch() {

        RegisterUserRequest expectedUser = UserTestDataFactory.getUniqueUser();

        FullUserRequestResponse userResponseCreated = ApiClientManager
               .getRegisterClient()
                .doRegister(expectedUser);


        String responseSearched = await()
                .atMost(5, TimeUnit.SECONDS)
                .pollInterval(500, TimeUnit.MILLISECONDS)
                .ignoreExceptions()
                .until(
                        () -> ApiClientManager.getSearchClient()
                                .searchByEmailRaw(expectedUser.getEmail()),
                        res -> res.getStatusCode() == 231
                ).asString();
        JsonPath jsonPath = new JsonPath(responseSearched);

        List<FullUserRequestResponse> users = jsonPath.getList("results", FullUserRequestResponse.class);

        FullUserRequestResponse userResponseSearched = users.getFirst();

        assertThat(userResponseSearched)
                .usingRecursiveComparison()
                .comparingOnlyFields("name", "email")
                .isEqualTo(userResponseCreated);
    }

    @Test
    void userOneField_shouldReturnSuccessMessage(){
        RegisterUserRequest expectedUser = UserTestDataFactory.getUniqueUser();
        UpdateUserRequest updatedUser = UserTestDataFactory.getUpdateUserWithNameChange(expectedUser.getEmail());

        FullUserRequestResponse userResponseCreated = ApiClientManager
                .getRegisterClient()
                .doRegister(expectedUser);

        String responseUpdated = await()
                .atMost(5, TimeUnit.SECONDS)
                .pollInterval(1, TimeUnit.SECONDS)
                .ignoreExceptions()
                .until(
                        () -> ApiClientManager.getUpdateClient().updateClientRaw(updatedUser),
                        response -> response.getStatusCode() == 200
                ).asString();
        assertThat(responseUpdated).contains(updatedUser.getField());
        assertThat(responseUpdated).contains(updatedUser.getValue());
        assertThat(responseUpdated).contains(expectedUser.getEmail());
    }
}
