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
 * Page Object class for the Checkout: Your Information page.
 * This class contains methods to interact with elements on the Checkout: Your Information page.
 */
public class CheckoutYourInformationPage extends BasePage {

    private static final Logger logger = LoggerFactory.getLogger(CheckoutYourInformationPage.class);
    
     /**
	 * Constructor for CheckoutYourInformationPage.
	 *
	 * @param driver The WebDriver instance used to interact with the browser.
	 */ 
    public CheckoutYourInformationPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    // Locators for checkout information elements
    @FindBy(id = "first-name")
    private WebElement firstNameInput;

    @FindBy(id = "last-name")
    private WebElement lastNameInput;

    @FindBy(id = "postal-code")
    private WebElement postalCodeInput;

    @FindBy(id = "continue")
    private WebElement continueButton;

    @FindBy(id = "cancel")
    private WebElement cancelButton;

    @FindBy(xpath = "//h3[@data-test='error']")
    private WebElement errorMessage;

    /**
     * Enters the checkout information (first name, last name, and postal code).
     *
     * @param firstName  The first name of the user.
     * @param lastName   The last name of the user.
     * @param postalCode The postal code of the user.
     */
    public void enterCheckoutInformation(String firstName, String lastName, String postalCode) {
        try {
            logger.info("Entering checkout information.");
            waitForWebElementToAppear(firstNameInput);
            firstNameInput.sendKeys(firstName);
            lastNameInput.sendKeys(lastName);
            postalCodeInput.sendKeys(postalCode);
            logger.info("Checkout information entered successfully.");
        } catch (Exception e) {
            logger.error("Error while entering checkout information.", e);
            throw e;
        }
    }

    /**
     * Clicks the "Continue" button to proceed to the Checkout: Overview page.
     *
     * @return A new instance of CheckoutOverviewPage.
     */
    public CheckoutOverviewPage clickContinueButton() {
        try {
            logger.info("Clicking the 'Continue' button on the Checkout: Your Information page.");
            waitForWebElementToAppear(continueButton);
            continueButton.click();
            logger.info("'Continue' button clicked successfully.");
            return new CheckoutOverviewPage(driver);
        } catch (Exception e) {
            logger.error("Error while clicking the 'Continue' button.", e);
            throw e;
        }
    }

    /**
     * Clicks the "Cancel" button to return to the Cart page.
     *
     * @return A new instance of CartPage.
     */
    public CartPage clickCancelButton() {
        try {
            logger.info("Clicking the 'Cancel' button on the Checkout: Your Information page.");
            waitForWebElementToAppear(cancelButton);
            cancelButton.click();
            logger.info("'Cancel' button clicked successfully.");
            return new CartPage(driver);
        } catch (Exception e) {
            logger.error("Error while clicking the 'Cancel' button.", e);
            throw e;
        }
    }

    /**
     * Retrieves the error message displayed on the page.
     *
     * @return The error message as a String.
     */
    public String getErrorMessage() {
        try {
            logger.info("Fetching the error message displayed on the page.");
            waitForWebElementToAppear(errorMessage);
            String errorText = errorMessage.getText();
            logger.info("Error message retrieved: {}", errorText);
            return errorText;
        } catch (Exception e) {
            logger.error("Error while fetching the error message.", e);
            throw e;
        }
    }
    
    /**
     * Checks if the user is currently on the Checkout: Your Information page.
     *
     * @return True if the user is on the page, false otherwise.
     */
    public boolean isOnCheckoutYourInformationPage() {
        try {
            logger.info("Checking if on Checkout: Your Information page.");
            boolean isOnPage = firstNameInput.isDisplayed();
            logger.info("Is on Checkout: Your Information Page: {}", isOnPage);
            return isOnPage;
        } catch (NoSuchElementException e) {
            logger.warn("First name input not found, not on Checkout: Your Information Page.");
            return false;
        } catch (Exception e) {
            logger.error("Unexpected error while checking if on Checkout: Your Information Page.", e);
            return false;
        }
    }
}
