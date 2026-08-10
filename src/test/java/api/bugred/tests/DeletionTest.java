package api.bugred.tests;

import api.bugred.model.DeleteUserResponse;
import api.bugred.model.RegisterUser;
import api.bugred.steps.UserSteps;
import api.bugred.testdata.UserTestDataFactory;
import io.restassured.path.json.JsonPath;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

public class DeletionTest {


    @Test
    void doDeleteUser_shouldDeleteUser()
    {
        RegisterUser registerUser = UserTestDataFactory.getUniqueUser();

        UserSteps.registerAndWait(registerUser);

        DeleteUserResponse deletionResponse = UserSteps.deleteUserByEmail(registerUser.getEmail());

            SoftAssertions softly = new SoftAssertions();

            softly.assertThat(deletionResponse.getMessage())
                    .contains("успешно удален");

        softly.assertThat(deletionResponse.getMessage())
                .contains(registerUser.getEmail());

            softly.assertThat(deletionResponse.getType())
                    .contains("error"); //bugred intentionally returns error by contract

            JsonPath searchJson = new JsonPath(UserSteps.searchResponseByEmail(registerUser.getEmail()));

            softly.assertThat(searchJson
                            .getInt("foundCount"))
                    .isEqualTo(0);
            softly.assertAll();
    }
}
