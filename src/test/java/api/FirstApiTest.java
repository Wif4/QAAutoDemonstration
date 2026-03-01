package api;

import api.client.UserClient;
import core.BaseTest;
import core.specs.ApiSpec;
import core.specs.ResponseSpec;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

public class FirstApiTest extends BaseTest {

    @Test
    void get_shouldReturn200() {
        given()
                .spec(ApiSpec.requestSpec())
                .when()
                .get()
                .then()
                .spec(ResponseSpec.successResponse());
    }

    private final UserClient userClient = new UserClient();

    @Test
    void getUsers_shouldReturnUsersList() {
        userClient.getUsersRaw()
                .then()
                .spec(ResponseSpec.successResponse())
                .body("[0].id", notNullValue());
    }

    @Test
    void getUsers_shouldReturnTypedUsers() {
        var users = userClient.getUsers();

        assertNotNull(users, "Users list should not be null");
        assertFalse(users.isEmpty(), "Users list should not be empty");
        assertNotNull(users.getFirst().getId(), "User id should not be null");
    }

    @ParameterizedTest
    @ValueSource (ints = {1,2,3,4})
    void getUsersId_shouldReturnUserId(Integer userId){
        var user = userClient.getUserById(userId);

        assertNotNull(user, "User should not be null");
        assertEquals(userId, user.getId(), "Users id should be equal to" + userId);
        assertNotNull(user.getEmail(), "Email should not be null");
    }
}