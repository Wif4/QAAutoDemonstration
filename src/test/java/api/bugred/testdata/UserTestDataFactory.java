package api.bugred.testdata;

import api.bugred.model.RegisterUserRequest;
import api.bugred.model.UpdateUserRequest;

import java.util.UUID;

public class UserTestDataFactory {
    public static RegisterUserRequest getUniqueUser()
    {
        RegisterUserRequest registerUserRequest = new RegisterUserRequest();
        registerUserRequest.setName(UUID.randomUUID().toString());
        registerUserRequest.setEmail(UUID.randomUUID() + "@mail.ru");
        registerUserRequest.setPassword(UUID.randomUUID().toString());
        return registerUserRequest;
    }
    public static RegisterUserRequest getUserWithName(String name)
    {
        RegisterUserRequest registerUserRequest = new RegisterUserRequest();
        registerUserRequest.setName(name);
        registerUserRequest.setEmail(UUID.randomUUID() + "@mail.ru");
        registerUserRequest.setPassword(UUID.randomUUID().toString());
        return registerUserRequest;
    }
    public static RegisterUserRequest getUserWithEmail(String email)
    {
        RegisterUserRequest registerUserRequest = new RegisterUserRequest();
        registerUserRequest.setName(UUID.randomUUID().toString());
        registerUserRequest.setEmail(email);
        registerUserRequest.setPassword(UUID.randomUUID().toString());
        return registerUserRequest;
    }

    public static UpdateUserRequest getUpdateUserWithNameChange(String email)
    {
        UpdateUserRequest updateUser = new UpdateUserRequest();
        updateUser.setField("name");
        updateUser.setValue(UUID.randomUUID().toString());
        updateUser.setEmail(email);

        return updateUser;
    }
}
