package api.bugred.tests;

import api.bugred.model.FullUserResponse;
import api.bugred.model.RegisterUserRequest;
import api.bugred.model.UpdateUserRequest;
import api.bugred.model.UpdateUserResponse;
import api.bugred.steps.UserSteps;
import api.bugred.testdata.UserTestDataFactory;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class UpdateTest {

    @Test
    public void UpdateOneField_shouldUpdateNameField()
    {
        SoftAssertions softly = new SoftAssertions();
        RegisterUserRequest registerUserRequest = UserTestDataFactory.getUniqueUser();
        UpdateUserRequest updateUser = UserTestDataFactory.getUpdateUserWithNameChange(registerUserRequest.getEmail());
        UserSteps.registerAndWait(registerUserRequest);

        UpdateUserResponse updateResult = UserSteps.updateUserOneField(updateUser);

        softly.assertThat(updateResult.getMessage()).
                contains("Поле name успешно изменено на");
        softly.assertThat(updateResult.getMessage())
                .contains(registerUserRequest.getEmail());

        FullUserResponse resultUser = UserSteps.searchUserByEmail(registerUserRequest.getEmail());

        softly.assertThat(resultUser.getName())
                .isEqualTo(updateUser.getValue());

        softly.assertAll();
    }

    @Test
    public void UpdateOneField_shouldReturnNotFound()
    {
        RegisterUserRequest registerUserRequest = UserTestDataFactory.getUniqueUser();
        UpdateUserRequest updateUserRequest = UserTestDataFactory
                .getUpdateUserWithNameChange(registerUserRequest.getEmail());

        UpdateUserResponse userResponse = UserSteps.updateUserOneField(updateUserRequest);

        assertThat(userResponse.getMessage()).contains("Пользователь с таким email не найден!");
        assertThat(userResponse.getType()).contains("error");

    }
}
