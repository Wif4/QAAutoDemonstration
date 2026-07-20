package core.api;

import api.bugred.client.DeleteClient;
import api.bugred.client.RegisterClient;
import api.bugred.client.SearchClient;
import api.bugred.client.UpdateClient;
import api.jsonplaceholder.client.UserClient;

public class ApiClientManager {
    private static final RegisterClient registerClient = new RegisterClient();
    private static final SearchClient searchClient = new SearchClient();
    private static final UserClient userClient = new UserClient();
    private static final UpdateClient updateClient = new UpdateClient();
    private static final DeleteClient deleteClient = new DeleteClient();

    public static RegisterClient getRegisterClient() {
        return registerClient;
    }

    public static SearchClient getSearchClient ()
    {
        return  searchClient;
    }

    public  static UserClient getUserClient()
    {
        return userClient;
    }

    public static UpdateClient getUpdateClient() {
        return updateClient;
    }

    public static DeleteClient getDeleteClient() {
        return deleteClient;
    }
}
