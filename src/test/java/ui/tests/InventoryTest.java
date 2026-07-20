/*
package ui.tests;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import ui.core.BaseUiTest;
import ui.pages.InventoryPage;
import ui.pages.LoginPage;

import static org.assertj.core.api.Assertions.assertThat;

public class InventoryTest extends BaseUiTest {

    @Test
    public void addBackPackButton_shouldAddItemToCart()
    {
        LoginPage loginPage = new LoginPage(driver, wait);
        loginPage.open();
        loginPage.enterUsername("standard_user");
        loginPage.enterPassword("secret_sauce");
        loginPage.clickLogin();
        InventoryPage inventoryPage = new InventoryPage(driver, wait);
        inventoryPage.addBackPack();
        inventoryPage.toCart();
        WebElement itemName = driver.findElement(By.className("inventory_item_name"));

        assertThat(itemName.getText()).contains("Sauce Labs Backpack");
    }
}
*/
