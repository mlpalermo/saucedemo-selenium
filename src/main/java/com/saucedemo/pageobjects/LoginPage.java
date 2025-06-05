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
 * Page Object Model class for the Login page of the application.
 * This class contains methods to interact with the login page elements.
 */
public class LoginPage extends BasePage {
	
    private static final Logger logger = LoggerFactory.getLogger(LoginPage.class);
    
    /**
	 * Constructor for the LoginPage class.
	 *
	 * @param driver The WebDriver instance to be used for this page.
	 */
    public LoginPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    // Locators for login page elements
    @FindBy(id = "user-name")
    private WebElement userEmail;

    @FindBy(id = "password")
    private WebElement userPassword;

    @FindBy(id = "login-button")
    private WebElement loginButton;

    @FindBy(css = ".error-message-container")
    private WebElement errorMessage;

    /**
     * Logs in to the application using the provided username and password.
     *
     * @param username The username to log in with.
     * @param password The password to log in with.
     * @return A new instance of ProductCatalogPage if login is successful.
     */
    public ProductCatalogPage login(String username, String password) {
        try {
            logger.info("Attempting to log in.");
            waitForWebElementToAppear(userEmail);
            userEmail.sendKeys(username);
            userPassword.sendKeys(password);
            loginButton.click();
            logger.info("Login button clicked successfully.");
            return new ProductCatalogPage(driver);
        } catch (Exception e) {
            logger.error("Error occurred during login.", e);
            throw e;
        }
    }

    /**
     * Retrieves the error message displayed on the login page.
     *
     * @return The error message as a String.
     */
    public String getErrorMessage() {
        try {
            logger.info("Fetching the error message displayed on the login page.");
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
     * Checks if the user is currently on the login page.
     *
     * @return True if the user is on the login page, false otherwise.
     */
    public boolean isOnLoginPage() {
        try {
            logger.info("Checking if on login page.");
            boolean isOnPage = userEmail.isDisplayed() && loginButton.isDisplayed();
            logger.info("Is on Login Page: {}", isOnPage);
            return isOnPage;
        } catch (NoSuchElementException e) {
            logger.warn("Login page elements not found, not on Login Page.");
            return false;
        } catch (Exception e) {
            logger.error("Unexpected error while checking if on Login Page.", e);
            return false;
        }
    }
}
