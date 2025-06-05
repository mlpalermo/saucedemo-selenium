package com.saucedemo.pageobjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.saucedemo.basepage.BasePage;

/**
 * Page Object class for the Checkout: Complete page.
 * This class contains methods to interact with elements on the Checkout: Complete page.
 */
public class CheckoutCompletePage extends BasePage {

    private static final Logger logger = LoggerFactory.getLogger(CheckoutCompletePage.class);
    
    /**
	 * Constructor for CheckoutCompletePage.
	 *
	 * @param driver WebDriver instance.
	 */
    public CheckoutCompletePage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    // Locators for checkout complete page elements
    @FindBy(className = "complete-header")
    private WebElement completeHeader;

    @FindBy(className = "complete-text")
    private WebElement completeText;

    @FindBy(id = "back-to-products")
    private WebElement backToProductsButton;

    /**
     * Retrieves the header text displayed on the Checkout Complete page.
     *
     * @return The header text as a String.
     */
    public String getCompleteHeaderText() {
        try {
            logger.info("Fetching the complete header text.");
            waitForWebElementToAppear(completeHeader);
            String headerText = completeHeader.getText();
            logger.info("Complete header text retrieved: {}", headerText);
            return headerText;
        } catch (Exception e) {
            logger.error("Error while fetching the complete header text.", e);
            throw e;
        }
    }

    /**
     * Retrieves the message text displayed on the Checkout Complete page.
     *
     * @return The message text as a String.
     */
    public String getCompleteMessageText() {
        try {
            logger.info("Fetching the complete message text.");
            waitForWebElementToAppear(completeText);
            String messageText = completeText.getText();
            logger.info("Complete message text retrieved: {}", messageText);
            return messageText;
        } catch (Exception e) {
            logger.error("Error while fetching the complete message text.", e);
            throw e;
        }
    }

    /**
     * Clicks the "Back Home" button and navigates to the Product Catalog page.
     *
     * @return A new instance of the ProductCatalogPage.
     */
    public ProductCatalogPage clickBackHomeButton() {
        try {
            logger.info("Clicking the 'Back Home' button.");
            waitForWebElementToAppear(backToProductsButton);
            backToProductsButton.click();
            logger.info("'Back Home' button clicked successfully.");
            return new ProductCatalogPage(driver);
        } catch (Exception e) {
            logger.error("Error while clicking the 'Back Home' button.", e);
            throw e;
        }
    }
}
