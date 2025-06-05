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
 * Page Object class for the Checkout: Overview page.
 * This class contains methods to interact with elements on the Checkout: Overview page.
 */
public class CheckoutOverviewPage extends BasePage {

    private static final Logger logger = LoggerFactory.getLogger(CheckoutOverviewPage.class);
    
    /**
	 * Constructor for CheckoutOverviewPage.
	 *
	 * @param driver WebDriver instance.
	 */
    public CheckoutOverviewPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    // Locators for cart items
    private final By cartItemBy = By.cssSelector(".cart_item");
    private final By cartItemNameBy = By.cssSelector(".inventory_item_name");
    private final By cartItemDescriptionBy = By.cssSelector(".inventory_item_desc");
    private final By cartItemPriceBy = By.cssSelector(".inventory_item_price");

    // Locators for summary information
    @FindBy(css = ".summary_subtotal_label")
    private WebElement itemTotal;

    @FindBy(css = ".summary_tax_label")
    private WebElement tax;

    @FindBy(css = ".summary_total_label")
    private WebElement total;

    @FindBy(css = ".summary_info div:nth-child(2)")
    private WebElement paymentInformation;

    @FindBy(css = ".summary_info div:nth-child(4)")
    private WebElement shippingInformation;

    @FindBy(id = "finish")
    private WebElement finishButton;

    @FindBy(id = "cancel")
    private WebElement cancelButton;

    /**
     * Fetches all cart items displayed on the Checkout: Overview page.
     *
     * @return List of WebElements representing cart items.
     */
    private List<WebElement> getOverviewCartItems() {
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
    public WebElement getCartItemByName(String productName) {
        try {
            logger.info("Searching for the cart item with name: {}", productName);
            return getOverviewCartItems().stream()
                    .filter(item -> item.findElement(cartItemNameBy).getText().equals(productName))
                    .findFirst()
                    .orElse(null);
        } catch (Exception e) {
            logger.error("Error while searching for the cart item with name: {}", productName, e);
            throw e;
        }
    }

    /**
     * Checks if a product is in the cart.
     *
     * @param productName The name of the product.
     * @return True if the product is in the cart, false otherwise.
     */
    public boolean isProductInOverviewCart(String productName) {
        if (getCartItemByName(productName) != null) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Retrieves details of all products in the cart.
     *
     * @return List of HashMaps containing product details.
     */
    public List<HashMap<String, String>> getAllProductDetails() {
        try {
            logger.info("Fetching all product details from the Checkout: Overview page.");
            return getOverviewCartItems().stream().map(item -> {
                HashMap<String, String> productDetails = new HashMap<>();
                productDetails.put("name", item.findElement(cartItemNameBy).getText());
                productDetails.put("description", item.findElement(cartItemDescriptionBy).getText());
                productDetails.put("price", item.findElement(cartItemPriceBy).getText());
                return productDetails;
            }).collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error while fetching product details.", e);
            throw e;
        }
    }

    /**
     * Retrieves payment information displayed on the page.
     *
     * @return Payment information as a String.
     */
    public String getPaymentInformation() {
        try {
            logger.info("Fetching payment information.");
            waitForWebElementToAppear(paymentInformation);
            return paymentInformation.getText();
        } catch (Exception e) {
            logger.error("Error while fetching payment information.", e);
            throw e;
        }
    }

    /**
     * Retrieves shipping information displayed on the page.
     *
     * @return Shipping information as a String.
     */
    public String getShippingInformation() {
        try {
            logger.info("Fetching shipping information.");
            waitForWebElementToAppear(shippingInformation);
            return shippingInformation.getText();
        } catch (Exception e) {
            logger.error("Error while fetching shipping information.", e);
            throw e;
        }
    }

    /**
     * Verifies the total computation on the page.
     *
     * @param taxRate The tax rate to use for computation.
     * @return True if the computed total matches the displayed total, false otherwise.
     */
    public boolean verifyTotalComputation(double taxRate) {
        try {
            logger.info("Verifying the total computation on the Checkout: Overview page.");

            double itemTotalValue = Double.parseDouble(itemTotal.getText().replace("Item total: $", ""));
            double taxValue = Double.parseDouble(tax.getText().replace("Tax: $", ""));
            double totalValue = Double.parseDouble(total.getText().replace("Total: $", ""));

            double computedTax = Math.round(itemTotalValue * taxRate * 100.0) / 100.0;
            double computedTotal = Math.round((itemTotalValue + computedTax) * 100.0) / 100.0;

            logger.info("Item Total: {}, Computed Tax: {}, Displayed Tax: {}, Computed Total: {}, Displayed Total: {}",
                    itemTotalValue, computedTax, taxValue, computedTotal, totalValue);

            return computedTax == taxValue && computedTotal == totalValue;
        } catch (Exception e) {
            logger.error("Error while verifying total computation.", e);
            return false;
        }
    }

    /**
     * Clicks the "Finish" button to complete the checkout process.
     *
     * @return A new instance of CheckoutCompletePage.
     */
    public CheckoutCompletePage clickFinishButton() {
        try {
            logger.info("Clicking the 'Finish' button.");
            waitForWebElementToAppear(finishButton);
            finishButton.click();
            return new CheckoutCompletePage(driver);
        } catch (Exception e) {
            logger.error("Error while clicking the 'Finish' button.", e);
            throw e;
        }
    }

    /**
     * Clicks the "Cancel" button to cancel the checkout process.
     *
     * @return A new instance of ProductCatalogPage.
     */
    public ProductCatalogPage clickCancelButton() {
        try {
            logger.info("Clicking the 'Cancel' button.");
            waitForWebElementToAppear(cancelButton);
            cancelButton.click();
            return new ProductCatalogPage(driver);
        } catch (Exception e) {
            logger.error("Error while clicking the 'Cancel' button.", e);
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
     * Checks if the user is on the Checkout: Overview page.
     *
     * @return True if the user is on the page, false otherwise.
     */
    public boolean isOnCheckoutOverviewPage() {
        try {
        	logger.info("Checking if on Checkout: Overview page.");
            boolean isOnPage = finishButton.isDisplayed();
            logger.info("Is on Checkout: Overview Page: {}", isOnPage);
            return isOnPage;
        } catch (NoSuchElementException e) {
            logger.warn("Finish button not found, not on Checkout: Overview Page.");
            return false;
        } catch (Exception e) {
            logger.error("Unexpected error while checking if on Checkout: Overview Page.", e);
            return false;
        }
    }
}
