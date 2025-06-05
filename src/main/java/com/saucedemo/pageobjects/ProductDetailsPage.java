package com.saucedemo.pageobjects;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.saucedemo.basepage.BasePage;

/**
 * Page Object Model class for the Product Details page of the application.
 * This class contains methods to interact with the product details page elements.
 */
public class ProductDetailsPage extends BasePage {

    private static final Logger logger = LoggerFactory.getLogger(ProductDetailsPage.class);
    
    /**
	 * Constructor for ProductDetailsPage.
	 *
	 * @param driver The WebDriver instance.
	 */
    public ProductDetailsPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    // Locators for product details page elements
    @FindBy(css = ".inventory_details_name")
    private WebElement productName;

    @FindBy(css = ".inventory_details_desc")
    private WebElement productDescription;

    @FindBy(css = ".inventory_details_price")
    private WebElement productPrice;

    @FindBy(id = "add-to-cart")
    private WebElement addToCartButton;

    @FindBy(id = "remove")
    private WebElement removeButton;

    @FindBy(css = ".inventory_details_back_button")
    private WebElement backToProductsButton;

    /**
     * Retrieves the product name.
     *
     * @return The product name as a String.
     */
    public String getProductName() {
        try {
            waitForWebElementToAppear(productName);
            String name = productName.getText();
            logger.info("Retrieved product name: {}", name);
            return name;
        } catch (Exception e) {
            logger.error("Error while retrieving product name.", e);
            throw e;
        }
    }

    /**
     * Retrieves the product description.
     *
     * @return The product description as a String.
     */
    public String getProductDescription() {
        try {
            waitForWebElementToAppear(productDescription);
            String description = productDescription.getText();
            logger.info("Retrieved product description: {}", description);
            return description;
        } catch (Exception e) {
            logger.error("Error while retrieving product description.", e);
            throw e;
        }
    }

    /**
     * Retrieves the product price.
     *
     * @return The product price as a String.
     */
    public String getProductPrice() {
        try {
            waitForWebElementToAppear(productPrice);
            String price = productPrice.getText();
            logger.info("Retrieved product price: {}", price);
            return price;
        } catch (Exception e) {
            logger.error("Error while retrieving product price.", e);
            throw e;
        }
    }

    /**
     * Clicks the "Add to Cart" button.
     */
    public void clickAddToCartButton() {
        try {
            if (addToCartButton.isDisplayed()) {
                addToCartButton.click();
                logger.info("Clicked 'Add to Cart' button.");
            } else {
                logger.warn("'Add to Cart' button is not displayed.");
            }
        } catch (Exception e) {
            logger.error("Error while clicking 'Add to Cart' button.", e);
            throw e;
        }
    }

    /**
     * Clicks the "Remove" button.
     */
    public void clickRemoveButton() {
        try {
            if (removeButton.isDisplayed()) {
                removeButton.click();
                logger.info("Clicked 'Remove' button.");
            } else {
                logger.warn("'Remove' button is not displayed.");
            }
        } catch (Exception e) {
            logger.error("Error while clicking 'Remove' button.", e);
            throw e;
        }
    }

    /**
     * Checks if the "Add to Cart" button is displayed.
     *
     * @return True if the button is displayed, false otherwise.
     */
    public boolean isAddToCartButtonDisplayed() {
        try {
            boolean isDisplayed = addToCartButton.isDisplayed();
            logger.info("'Add to Cart' button displayed: {}", isDisplayed);
            return isDisplayed;
        } catch (Exception e) {
            logger.error("Error while checking 'Add to Cart' button visibility.", e);
            return false;
        }
    }

    /**
     * Checks if the "Remove" button is displayed.
     *
     * @return True if the button is displayed, false otherwise.
     */
    public boolean isRemoveButtonDisplayed() {
        try {
            boolean isDisplayed = removeButton.isDisplayed();
            logger.info("'Remove' button displayed: {}", isDisplayed);
            return isDisplayed;
        } catch (Exception e) {
            logger.error("Error while checking 'Remove' button visibility.", e);
            return false;
        }
    }

    /**
     * Clicks the "Back to Products" button and navigates to the Product Catalog page.
     *
     * @return A new instance of ProductCatalogPage.
     */
    public ProductCatalogPage clickBackToProducts() {
        try {
            logger.info("Clicking the 'Back to Products' button.");
            waitForWebElementToAppear(backToProductsButton);
            backToProductsButton.click();
            logger.info("'Back to Products' button clicked successfully.");
            return new ProductCatalogPage(driver);
        } catch (Exception e) {
            logger.error("Error while clicking 'Back to Products' button.", e);
            throw e;
        }
    }

    /**
     * Navigates back to the Checkout: Overview page using the browser's back button.
     *
     * @return A new instance of CheckoutOverviewPage.
     */
    public CheckoutOverviewPage navigateBackToCheckoutOverview() {
        try {
            logger.info("Navigating back to the Checkout: Overview page using the browser's Back button.");
            driver.navigate().back();
            return new CheckoutOverviewPage(driver);
        } catch (Exception e) {
            logger.error("Error while navigating back to Checkout: Overview page.", e);
            throw e;
        }
    }

    /**
     * Checks if the user is on the Product Details page.
     *
     * @return True if the user is on the Product Details page, false otherwise.
     */
    public boolean isOnProductDetailsPage() {
        try {
        	logger.info("Checking if on Product Details Page.");
            boolean isOnPage = backToProductsButton.isDisplayed();
            logger.info("Is on Product Details Page: {}", isOnPage);
            return isOnPage;
        } catch (NoSuchElementException e) {
            logger.warn("Back to Products button not found, not on Product Details Page.");
            return false;
        } catch (Exception e) {
            logger.error("Unexpected error while checking if on Product Details Page.", e);
            return false;
        }
    }
}
