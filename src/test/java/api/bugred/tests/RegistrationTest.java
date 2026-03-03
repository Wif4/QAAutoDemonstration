package api.bugred.tests;

import api.bugred.client.RegisterClient;
import api.bugred.model.FullUserResponse;
import api.bugred.model.RegisterUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.BaseTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class RegistrationTest extends BaseTest {

    private final RegisterClient registerClient = new RegisterClient();

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
        ObjectMapper mapper = new ObjectMapper();
        RegisterUser expectedUser = mapper.readValue(json, RegisterUser.class);

       FullUserResponse userResponse = registerClient.doRegister(json);

       assertThat(userResponse).isNotNull();
       assertThat(userResponse)
               .usingRecursiveComparison()
               .comparingOnlyFields("email", "name")
               .isEqualTo(expectedUser);
        assertThat(userResponse).extracting("password", "avatar").doesNotContainNull();

    }

}
