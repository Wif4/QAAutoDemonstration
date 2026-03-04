package api.bugred.tests;

import api.bugred.model.FullUserResponse;
import api.bugred.model.RegisterUser;
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
        RegisterUser expectedUser = UserTestDataFactory.getUniqueUser();

       FullUserResponse userResponse = ApiClientManager
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
        RegisterUser registerUser = UserTestDataFactory.getUniqueUser();

        ApiClientManager
               .getRegisterClient().
                doRegisterRaw(registerUser); //preparation

        RegisterUser duplicateMailUser = UserTestDataFactory.getUserWithEmail(registerUser.getEmail());

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
        assertThat(message).contains(registerUser.getEmail());
    }

    @Test
    void doRegister_shouldReturnSuccessErrorTypeAndNameMessage() {
        RegisterUser registerUser = UserTestDataFactory.getUniqueUser();

        ApiClientManager
               .getRegisterClient()
                .doRegisterRaw(registerUser); //preparation

        RegisterUser duplicateNameUser = UserTestDataFactory.getUserWithName(registerUser.getName());

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

        RegisterUser expectedUser = UserTestDataFactory.getUniqueUser();

        FullUserResponse userResponseCreated = ApiClientManager
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

        List<FullUserResponse> users = jsonPath.getList("results", FullUserResponse.class);

        FullUserResponse userResponseSearched = users.getFirst();

        assertThat(userResponseSearched)
                .usingRecursiveComparison()
                .comparingOnlyFields("name", "email")
                .isEqualTo(userResponseCreated);

    }
}
