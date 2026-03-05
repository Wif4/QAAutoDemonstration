package ui.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage extends BasePage{

    private final By usernameInput = By.id("user-name");
    private final By passwordInput = By.id("password");
    private final By loginButton = By.id("login-button");
    private final By errorMessage = By.cssSelector("[data-test='error']");

    public LoginPage(WebDriver driver, WebDriverWait wait) {
        super (driver, wait);
    }

    @Step("Open login page")
    public void open() {
        driver.get("https://www.saucedemo.com/");
    }

    @Step("Enter username: {username}")
    public void enterUsername(String username) {
        wait
                .until(ExpectedConditions.visibilityOfElementLocated(usernameInput))
                .sendKeys(username);
    }

    @Step("Enter password")
    public void enterPassword(String password) {
        wait
                .until(ExpectedConditions.visibilityOfElementLocated(passwordInput))
                .sendKeys(password);
    }
    @Step("Click login button")
    public void clickLogin() {
        click(loginButton);
    }

    public String getErrorMessage() {
        return wait
                .until(ExpectedConditions.visibilityOfElementLocated(errorMessage))
                .getText();
    }
}