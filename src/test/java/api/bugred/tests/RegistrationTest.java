package api.bugred.tests;

import api.bugred.client.RegisterClient;
import api.bugred.model.FullUserResponse;
import api.bugred.model.RegisterUser;
import api.bugred.testdata.UserTestDataFactory;
import core.BaseTest;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class RegistrationTest extends BaseTest {

    private final RegisterClient registerClient = new RegisterClient();

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
}
