package core.specs;

import core.config.ConfigManager;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;

import static io.restassured.filter.log.LogDetail.ALL;

public class ApiSpec {

    public static RequestSpecification requestSpec(String baseUrl) {
        RequestSpecBuilder builder = new RequestSpecBuilder()
                .setBaseUri(baseUrl)
                .addHeader("Content-Type", "application/json")
                .log(ALL);

        String token = ConfigManager.getToken();

        if (token != null && !token.isBlank()) {
            builder.addHeader("Authorization", "Bearer " + token);
        }

        return builder.build();
    }
}
