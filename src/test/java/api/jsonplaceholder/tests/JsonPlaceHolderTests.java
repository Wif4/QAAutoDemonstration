package api.jsonplaceholder.tests;

import api.jsonplaceholder.client.UserClient;
import api.jsonplaceholder.model.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.BaseTest;
import core.specs.ResponseSpec;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

public class JsonPlaceHolderTests extends BaseTest {

    /*@Test
    void get_shouldReturn200() {
        given()
                .spec(ApiSpec.requestSpec())
                .when()
                .get()
                .then()
                .spec(ResponseSpec.successResponse());
    }*/

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
    void getUsersById_shouldReturnUserId(Integer userId){
        var user = userClient.getUserById(userId);

        assertNotNull(user, "User should not be null");
        assertEquals(userId, user.getId(), "Users id should be equal to" + userId);
        assertNotNull(user.getEmail(), "Email should not be null");
    }

    @ParameterizedTest
    @ValueSource (ints = {-1, 999, 1000})
    void getUsersById_shouldReturnNotFound(Integer userId){
        Response response = userClient.getUserByIdRaw(userId)
                .then()
                .statusCode(404)
                .extract()
                .response();

        String body = response.getBody().asString();

        assertTrue(body.isEmpty() || "{}".equals(body.trim()));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            """
    {
      "id": 12,
      "name": "Leanne Graham",
      "username": "Bret",
      "email": "Sincere@april.biz"
    }
    """,
            """
    {
      "id": 13,
      "name": "Anatoly Graham",
      "username": "Bret",
      "email": "Anatoly@april.biz"
    }
    """
    })
    void postUsers_shouldReturnUser(String json) {
        var user = userClient.createUser(json);
        assertNotNull(user, "User should not be null");

        ObjectMapper mapper = new ObjectMapper();
        JsonNode node;
        try {
            node = mapper.readTree(json);
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        assertNotNull(user.getId());
        assertEquals(node.get("name").asText(), user.getName());
        assertEquals(node.get("username").asText(), user.getUsername());
        assertEquals(node.get("email").asText(), user.getEmail());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            """
    {
      "id": null,
      "name": "",
      "username": "",
      "email": ""
    }
    """
    })
    void postUsers_shouldHandleNullFields(String json) {
        var user = userClient.createUserRaw(json)
                .then()
                .statusCode(201)
                .extract()
                .as(User.class);
        assertNotNull(user, "User should not be null");
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node;
        try {
            node = mapper.readTree(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        assertNotNull(user.getId());
        assertEquals(node.get("name").asText(), user.getName());
        assertEquals(node.get("username").asText(), user.getUsername());
        assertEquals(node.get("email").asText(), user.getEmail());
    }
}