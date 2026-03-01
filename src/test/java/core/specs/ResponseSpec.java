package core.specs;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.ResponseSpecification;
import static org.hamcrest.Matchers.lessThan;

public class ResponseSpec {

    public static ResponseSpecification successResponse() {
        return new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectResponseTime(lessThan(5000L))
                .build();
    }

    public static ResponseSpecification notFoundResponse() {
        return new ResponseSpecBuilder()
                .expectStatusCode(404)
                .expectResponseTime(lessThan(5000L))
                .build();
    }

    public static ResponseSpecification badRequestResponse() {
        return new ResponseSpecBuilder()
                .expectStatusCode(400)
                .expectResponseTime(lessThan(5000L))
                .build();
    }
}