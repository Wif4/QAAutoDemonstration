package ui.tests;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import ui.core.BaseUiTest;
import ui.pages.LoginPage;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("ui")
@Tag("sauceDemo")
public class LoginTest extends BaseUiTest {

    @Tag("smoke")
    @Test
    void login_shouldSucceed() {

        LoginPage loginPage = new LoginPage(driver, wait);

        loginPage.open();
        loginPage.enterUsername("standard_user");
        loginPage.enterPassword("secret_sauce");
        loginPage.clickLogin();

        assertThat(driver.getCurrentUrl()).contains("inventory");
    }
    @Tag("smoke")
    @Test
    void login_shouldFailWithWrongPassword() {

        LoginPage loginPage = new LoginPage(driver, wait);

        loginPage.open();
        loginPage.enterUsername("standard_user");
        loginPage.enterPassword("wrong_password");
        loginPage.clickLogin();

        String error = loginPage.getErrorMessage();

        assertThat(error).contains("Username and password do not match");
    }
}