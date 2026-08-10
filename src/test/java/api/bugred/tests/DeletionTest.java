package api.bugred.tests;

import api.bugred.model.DeleteUserResponse;
import api.bugred.model.RegisterUserRequest;
import api.bugred.steps.UserSteps;
import api.bugred.testdata.UserTestDataFactory;
import io.restassured.path.json.JsonPath;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

public class DeletionTest {


    @Test
    void doDeleteUser_shouldDeleteUser()
    {
        RegisterUserRequest registerUserRequest = UserTestDataFactory.getUniqueUser();

        UserSteps.registerAndWait(registerUserRequest);

        DeleteUserResponse deletionResponse = UserSteps.deleteUserByEmail(registerUserRequest.getEmail());

            SoftAssertions softly = new SoftAssertions();

            softly.assertThat(deletionResponse.getMessage())
                    .contains("успешно удален");

        softly.assertThat(deletionResponse.getMessage())
                .contains(registerUserRequest.getEmail());

            softly.assertThat(deletionResponse.getType())
                    .contains("error"); //bugred intentionally returns error by contract

            JsonPath searchJson = new JsonPath(UserSteps.searchResponseByEmail(registerUserRequest.getEmail()));

            softly.assertThat(searchJson
                            .getInt("foundCount"))
                    .isEqualTo(0);
            softly.assertAll();
    }
}
