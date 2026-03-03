package api.bugred.testdata;

import api.bugred.model.RegisterUser;

import java.util.UUID;

public class UserTestDataFactory {
    public static RegisterUser getUniqueUser()
    {
        RegisterUser registerUser = new RegisterUser();
        registerUser.setName(UUID.randomUUID().toString());
        registerUser.setEmail(UUID.randomUUID() + "@mail.ru");
        registerUser.setPassword(UUID.randomUUID().toString());
        return registerUser;
    }
    public static RegisterUser getUserWithName(String name)
    {
        RegisterUser registerUser = new RegisterUser();
        registerUser.setName(name);
        registerUser.setEmail(UUID.randomUUID() + "@mail.ru");
        registerUser.setPassword(UUID.randomUUID().toString());
        return registerUser;
    }
    public static RegisterUser getUserWithEmail(String email)
    {
        RegisterUser registerUser = new RegisterUser();
        registerUser.setName(UUID.randomUUID().toString());
        registerUser.setEmail(email);
        registerUser.setPassword(UUID.randomUUID().toString());
        return registerUser;
    }
}
