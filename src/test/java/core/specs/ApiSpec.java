package core.specs;

import core.config.ConfigManager;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;

import static io.restassured.filter.log.LogDetail.ALL;

public class ApiSpec {

    public static RequestSpecification requestSpec() {
        return new RequestSpecBuilder()
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + ConfigManager.getToken())
                .log(ALL)
                .build();
    }
}
