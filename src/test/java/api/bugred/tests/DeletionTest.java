package api.bugred.tests;

import api.bugred.model.FullUserResponse;
import api.bugred.model.RegisterUser;
import api.bugred.testdata.UserTestDataFactory;
import core.api.ApiClientManager;
import groovy.json.StringEscapeUtils;
import io.restassured.path.json.JsonPath;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.awaitility.Awaitility.await;

public class DeletionTest {


    @Test
    void doDeleteUser_shouldDeleteUser()
    {
        RegisterUser registerUser = UserTestDataFactory.getUniqueUser();

        ApiClientManager
                .getRegisterClient().
                doRegister(registerUser);


        String responseSearched = await()
                .atMost(10, TimeUnit.SECONDS)
                .pollInterval(500, TimeUnit.MILLISECONDS)
                .ignoreExceptions()
                .until(
                        () -> ApiClientManager.getSearchClient()
                                .searchByEmailRaw(registerUser.getEmail()),
                        res -> res.getStatusCode() == 231
                ).asString();
        JsonPath jsonPath = new JsonPath(responseSearched);

        FullUserResponse userResponseSearched = jsonPath
                .getList("results", FullUserResponse.class)
                .getFirst();

       assertThat(userResponseSearched.getName()).isEqualTo(registerUser.getName());

            String deletionResponse = ApiClientManager
                    .getDeleteClient()
                    .doDelete(registerUser.getEmail());

            SoftAssertions softly = new SoftAssertions();

        String decodedResponse = StringEscapeUtils.unescapeJava(deletionResponse);

            softly.assertThat(decodedResponse).contains
                    ("успешно удален");

            softly.assertThat(decodedResponse).contains(registerUser.getEmail());


            String responseSearchAfterDeletion = await()
                    .atMost(10, TimeUnit.SECONDS)
                    .pollInterval(500, TimeUnit.MILLISECONDS)
                    .ignoreExceptions()
                    .until(
                            () -> ApiClientManager.getSearchClient()
                                    .searchByEmailRaw(registerUser.getEmail()),
                            res -> res.getStatusCode() == 230
                    ).asString();

            JsonPath searchJson = new JsonPath(responseSearchAfterDeletion);

            softly.assertThat(searchJson
                            .getInt("foundCount"))
                    .isEqualTo(0);
            softly.assertAll();


    }
}
