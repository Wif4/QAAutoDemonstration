package api.bugred.tests;

import api.bugred.model.FullUserResponse;
import api.bugred.model.RegisterUser;
import api.bugred.model.UpdateUser;
import api.bugred.model.UpdateUserResponse;
import api.bugred.steps.UserSteps;
import api.bugred.testdata.UserTestDataFactory;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

public class UpdateTest {

    @Test
    public void UpdateOneField_shouldUpdateNameField()
    {
        SoftAssertions softly = new SoftAssertions();
        RegisterUser registerUser = UserTestDataFactory.getUniqueUser();
        UpdateUser updateUser = UserTestDataFactory.getUpdateUserWithNameChange(registerUser.getEmail());

        UserSteps.registerAndWait(registerUser);

        UpdateUserResponse updateResult = UserSteps.updateUserOneField(updateUser);

        softly.assertThat(updateResult.getMessage()).
                contains("Поле name успешно изменено на");
        softly.assertThat(updateResult.getMessage())
                .contains(registerUser.getEmail());

        FullUserResponse resultUser = UserSteps.searchUserByEmail(registerUser.getEmail());

        softly.assertThat(resultUser.getName())
                .isEqualTo(updateUser.getValue());

        softly.assertAll();
    }
}
