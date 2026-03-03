package api.bugred.tests;

import api.bugred.client.RegisterClient;
import api.bugred.model.FullUserResponse;
import api.bugred.model.RegisterUser;
import api.bugred.testdata.UserTestDataFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.BaseTest;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

public class RegistrationTest extends BaseTest {

    private final RegisterClient registerClient = new RegisterClient();
    private final ObjectMapper objectMapper = new ObjectMapper();



    @ParameterizedTest
    @ValueSource(strings = {
            """
    {
      "email": "Sincere@apriql.biz",
      "name": "Leanne Grahamq",
      "password": "123"
    }
    """})
    void doRegister_shouldReturnSuccessAndBody(String json) throws Exception
    {
        RegisterUser expectedUser = objectMapper.readValue(json, RegisterUser.class);

       FullUserResponse userResponse = registerClient.doRegister(json);

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
        String json = objectMapper.writeValueAsString(registerUser);

        registerClient.doRegisterRaw(json); //preparation

        String response = registerClient.doRegisterRaw(json)
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
        String prepJson = objectMapper.writeValueAsString(registerUser);

        registerClient.doRegisterRaw(prepJson); //preparation

        RegisterUser duplicateNameUser = UserTestDataFactory.getUserWithName(registerUser.getName());

        String json = objectMapper.writeValueAsString(duplicateNameUser);

        String response = registerClient.doRegisterRaw(json)
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
