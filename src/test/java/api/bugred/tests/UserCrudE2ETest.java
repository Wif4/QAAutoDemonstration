package api.bugred.tests;

import api.bugred.model.*;
import api.bugred.steps.UserSteps;
import api.bugred.testdata.UserTestDataFactory;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

import java.util.Optional;

public class UserCrudE2ETest {

    @Test
    void UserCrud_shouldProcessUserLifecycle()
    {
        RegisterUserRequest registerUserRequest = UserTestDataFactory.getUniqueUser();
        UpdateUserRequest updateUserRequest = UserTestDataFactory
                .getUpdateUserWithNameChange(registerUserRequest.getEmail());
        SoftAssertions softAssertions = new SoftAssertions();

        UserSteps.registerAndWait(registerUserRequest);

        UserSteps.updateUserOneField(updateUserRequest);

        Optional<FullUserResponse> updatedUserSearchResponse = UserSteps.searchUserByEmail(registerUserRequest.getEmail());

        softAssertions.assertThat(updateUserRequest.getValue())
                .isEqualTo(updatedUserSearchResponse.orElseThrow().getName());

        UserSteps.deleteUserByEmail(registerUserRequest.getEmail());

        Optional<FullUserResponse> deletionUserSearchResponse = UserSteps.searchUserByEmail(registerUserRequest.getEmail());

        softAssertions.assertThat(deletionUserSearchResponse).isEmpty();

        softAssertions.assertAll();
    }
}
