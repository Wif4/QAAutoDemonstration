package core;

import core.config.ConfigManager;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;

public abstract class BaseTest {
    @BeforeAll
    static void setUp() {
        RestAssured.baseURI = ConfigManager.getBaseUrl();
    }
}
