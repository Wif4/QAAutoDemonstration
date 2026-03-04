package api.bugred.tests;

import api.bugred.client.RegisterClient;
import api.bugred.client.SearchClient;
import api.bugred.model.FullUserResponse;
import api.bugred.model.RegisterUser;
import api.bugred.testdata.UserTestDataFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import core.BaseTest;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

public class RegistrationTest extends BaseTest {

    private final RegisterClient registerClient = new RegisterClient();
    private final SearchClient searchClient = new SearchClient();

   @Test
    void doRegister_shouldReturnSuccessAndBody() throws Exception
    {
        RegisterUser expectedUser = UserTestDataFactory.getUniqueUser();

       FullUserResponse userResponse = registerClient.doRegister(expectedUser);

       assertThat(userResponse).isNotNull();
       assertThat(userResponse)
               .usingRecursiveComparison()
               .comparingOnlyFields("email", "name")
               .isEqualTo(expectedUser);
        assertThat(userResponse).extracting("password", "avatar").doesNotContainNull();
    }

    @Test
    void doRegister_shouldReturnSuccessErrorTypeAndEmailMessage() throws Exception {
        RegisterUser registerUser = UserTestDataFactory.getUniqueUser();

        registerClient.doRegisterRaw(registerUser); //preparation

        RegisterUser duplicateMailUser = UserTestDataFactory.getUserWithEmail(registerUser.getEmail());

        String response = registerClient.doRegisterRaw(duplicateMailUser)
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
    void doRegister_shouldReturnSuccessErrorTypeAndNameMessage() throws Exception {
        RegisterUser registerUser = UserTestDataFactory.getUniqueUser();

        registerClient.doRegisterRaw(registerUser); //preparation

        RegisterUser duplicateNameUser = UserTestDataFactory.getUserWithName(registerUser.getName());

        String response = registerClient.doRegisterRaw(duplicateNameUser)
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
    void registerUser_shouldBeFoundBySearch() throws JsonProcessingException {

        RegisterUser expectedUser = UserTestDataFactory.getUniqueUser();

        FullUserResponse userResponseCreated = registerClient.doRegister(expectedUser);


        String responseSearched = await()
                .atMost(5, TimeUnit.SECONDS)
                .pollInterval(500, TimeUnit.MILLISECONDS)
                .ignoreExceptions()
                .until(
                        () -> searchClient.searchByEmailRaw(expectedUser.getEmail()),
                        res -> res.getStatusCode() == 231
                ).asString();
        JsonPath jsonPath = new JsonPath(responseSearched);

        List<FullUserResponse> users = jsonPath.getList("results", FullUserResponse.class);

        FullUserResponse userResponseSearched = users.get(0);


        assertThat(userResponseSearched)
                .usingRecursiveComparison()
                .comparingOnlyFields("name", "email")
                .isEqualTo(userResponseCreated);

    }
}
