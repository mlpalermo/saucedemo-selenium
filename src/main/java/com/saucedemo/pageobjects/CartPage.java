package com.saucedemo.pageobjects;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.saucedemo.basepage.BasePage;

/**
 * Page Object class for the Cart page.
 * This class contains methods to interact with elements on the Cart page.
 */
public class CartPage extends BasePage {

    private static final Logger logger = LoggerFactory.getLogger(CartPage.class);
    
    /**
	 * Constructor for CartPage.
	 *
	 * @param driver WebDriver instance.
	 */
    public CartPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    // Locators for cart page elements
    @FindBy(id = "continue-shopping")
    private WebElement continueShoppingButton;

    @FindBy(id = "checkout")
    private WebElement checkoutButton;

    private final By cartItemBy = By.cssSelector(".cart_item");
    private final By cartItemNameBy = By.cssSelector(".inventory_item_name");
    private final By cartItemDescriptionBy = By.cssSelector(".inventory_item_desc");
    private final By cartItemPriceBy = By.cssSelector(".inventory_item_price");
    private final By cartItemRemoveButtonBy = By.cssSelector(".cart_button");

    /**
     * Fetches all cart items.
     *
     * @return List of WebElements representing cart items.
     */
    private List<WebElement> getCartItems() {
        try {
            waitForElementToAppear(cartItemBy);
            List<WebElement> cartItems = driver.findElements(cartItemBy);
            logger.info("Fetched the list of all products: {}", cartItems);
            return cartItems;
        } catch (Exception e) {
            logger.error("Error while fetching cart items.", e);
            throw e;
        }
    }

    /**
     * Retrieves a cart item by its name.
     *
     * @param productName The name of the product.
     * @return WebElement representing the cart item, or null if not found.
     */
    private WebElement getCartItemByName(String productName) {
        try {
            logger.info("Searching for product '{}' in the cart.", productName);
            return getCartItems().stream()
                    .filter(item -> item.findElement(cartItemNameBy).getText().equals(productName))
                    .findFirst()
                    .orElse(null);
        } catch (Exception e) {
            logger.error("Error while searching for product '{}' in the cart.", productName, e);
            throw e;
        }
    }

    /**
     * Checks if a product is in the cart.
     *
     * @param productName The name of the product.
     * @return True if the product is in the cart, false otherwise.
     */
    public boolean isProductInCart(String productName) {
        if (getCartItemByName(productName) != null) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Checks if the cart is empty.
     *
     * @return True if the cart is empty, false otherwise.
     */
    public boolean isCartEmpty() {
        try {
            logger.info("Checking if the cart is empty.");
            List<WebElement> cartItems = driver.findElements(cartItemBy);
    	    return cartItems.isEmpty();
        } catch (Exception e) {
            logger.error("Error while checking if the cart is empty.", e);
            return false;
        }
    }

    /**
     * Retrieves the description of a product in the cart.
     *
     * @param productName The name of the product.
     * @return The product description.
     * @throws NoSuchElementException if the product or description is not found.
     */
    public String getProductDescription(String productName) {
        WebElement product = getCartItemByName(productName);
        if (product == null) {
            throw new NoSuchElementException("Product '" + productName + "' not found in the cart.");
        }
        return product.findElement(cartItemDescriptionBy).getText();
    }

    /**
     * Retrieves the price of a product in the cart.
     *
     * @param productName The name of the product.
     * @return The product price.
     * @throws NoSuchElementException if the product or price is not found.
     */
    public String getProductPrice(String productName) {
        WebElement product = getCartItemByName(productName);
        if (product == null) {
            throw new NoSuchElementException("Product '" + productName + "' not found in the cart.");
        }
        return product.findElement(cartItemPriceBy).getText();
    }

    /**
     * Removes a product from the cart.
     *
     * @param productName The name of the product to remove.
     */
    public void removeProductFromCart(String productName) {
        try {
            WebElement product = getCartItemByName(productName);
            if (product != null) {
                product.findElement(cartItemRemoveButtonBy).click();
                logger.info("Product '{}' removed from the cart.", productName);
            } else {
                logger.warn("Product '{}' not found in the cart.", productName);
            }
        } catch (Exception e) {
            logger.error("Error while removing product '{}' from the cart.", productName, e);
            throw e;
        }
    }

    /**
     * Retrieves details of all products in the cart.
     *
     * @return List of HashMaps containing product details.
     */
    public List<HashMap<String, String>> getAllProductDetails() {
        try {
            logger.info("Fetching all product details from the cart.");
            return getCartItems().stream().map(item -> {
                HashMap<String, String> productDetails = new HashMap<>();
                productDetails.put("name", item.findElement(cartItemNameBy).getText());
                productDetails.put("description", item.findElement(cartItemDescriptionBy).getText());
                productDetails.put("price", item.findElement(cartItemPriceBy).getText());
                return productDetails;
            }).collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error while fetching product details from the cart.", e);
            throw e;
        }
    }

    /**
     * Clicks the "Continue Shopping" button.
     *
     * @return ProductCatalogPage object.
     */
    public ProductCatalogPage clickContinueShoppingButton() {
        try {
            logger.info("Clicking 'Continue Shopping' button.");
            continueShoppingButton.click();
            return new ProductCatalogPage(driver);
        } catch (Exception e) {
            logger.error("Error while clicking 'Continue Shopping' button.", e);
            throw e;
        }
    }

    /**
     * Navigates to the product details page for a specific product.
     *
     * @param productName The name of the product.
     * @return A new instance of ProductDetailsPage if navigation succeeds.
     * @throws NoSuchElementException if the product is not found.
     */
    public ProductDetailsPage goToProductDetailsPage(String productName) {
        try {
            logger.info("Navigating to the product details page for '{}'.", productName);
            WebElement product = getCartItemByName(productName);
            if (product == null) {
            	logger.warn("Product '{}' not found in the cart for navigation.", productName);
            	throw new NoSuchElementException("Product '" + productName + "' not found for navigation.");
            }
            product.findElement(cartItemNameBy).click();
            logger.info("Navigated to the product details page for '{}'.", productName);
            return new ProductDetailsPage(driver);
        } catch (Exception e) {
            logger.error("Error while navigating to product details page for '{}'.", productName, e);
            throw e;
        }
    }

    /**
     * Checks if the "Remove" button is displayed for any cart item.
     *
     * @return True if the "Remove" button is displayed, false otherwise.
     */
    public boolean isRemoveButtonDisplayed() {
        try {
            logger.info("Checking if 'Remove' button is displayed.");
            return getCartItems().stream()
                    .anyMatch(item -> item.findElement(cartItemRemoveButtonBy).isDisplayed());
        } catch (Exception e) {
            logger.error("Error while checking if 'Remove' button is displayed.", e);
            return false;
        }
    }

    /**
     * Clicks the "Checkout" button.
     *
     * @return CheckoutYourInformationPage object.
     */
    public CheckoutYourInformationPage clickCheckoutButton() {
        try {
            logger.info("Clicking 'Checkout' button.");
            checkoutButton.click();
            return new CheckoutYourInformationPage(driver);
        } catch (Exception e) {
            logger.error("Error while clicking 'Checkout' button.", e);
            throw e;
        }
    }

    /**
     * Checks if the user is on the cart page.
     *
     * @return True if on the cart page, false otherwise.
     */
    public boolean isOnCartPage() {
        try {
        	logger.info("Checking if on Cart Page.");
            boolean isOnPage = checkoutButton.isDisplayed();
            logger.info("Is on Cart Page: {}", isOnPage);
            return isOnPage;
        } catch (NoSuchElementException e) {
            logger.warn("Checkout button not found, not on Cart Page.");
            return false;
        } catch (Exception e) {
            logger.error("Unexpected error while checking if on Cart Page.", e);
            return false;
        }
    }
}
