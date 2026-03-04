package ui.core;

import io.qameta.allure.Attachment;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class ScreenshotOnFailureExtension implements AfterTestExecutionCallback {

    @Override
    public void afterTestExecution(ExtensionContext context) {

        boolean testFailed = context.getExecutionException().isPresent();

        if (testFailed) {

            Object testInstance = context.getRequiredTestInstance();

            if (testInstance instanceof BaseUiTest baseUiTest) {

                WebDriver driver = baseUiTest.getDriver();

                if (driver != null) {
                    saveScreenshot(driver);
                }
            }
        }
    }

    @Attachment(value = "Failure Screenshot", type = "image/png")
    private byte[] saveScreenshot(WebDriver driver) {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }
}