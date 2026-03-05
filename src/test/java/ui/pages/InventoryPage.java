package ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class InventoryPage extends BasePage{

    private final By toCartBackpackButton= By.id("add-to-cart-sauce-labs-backpack");
    private final By toCartBikeButton= By.id("add-to-cart-sauce-labs-bike-light");
    private final By toCartShirtButton = By.id("add-to-cart-sauce-labs-bolt-t-shirt");
    private final By toCartFleeceButton= By.id("add-to-cart-sauce-labs-fleece-jacket");
    private final By toCartOnesieButton= By.id("add-to-cart-test.allthethings()-t-shirt-(red)");
    private final By toCartRedShirtButton = By.id("add-to-cart-sauce-labs-onesie");
    private final By shoppingCartButton= By.className("shopping_cart_link");

    public InventoryPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public void addAllToCart()
    {
        addBackPack();
        addBikeLight();
        addShirt();
        click(toCartFleeceButton);
        click(toCartOnesieButton);
        click(toCartRedShirtButton);
        toCart();
    }

    public void addBackPack(){
        click(toCartBackpackButton);
    }

    public void addShirt(){
        click(toCartShirtButton);
    }

    public void addBikeLight (){
        click(toCartBikeButton);
    }

    public void toCart(){
        click(shoppingCartButton);
    }
}
