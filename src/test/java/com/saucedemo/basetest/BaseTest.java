package com.saucedemo.basetest;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import com.saucedemo.pageobjects.LoginPage;
import com.saucedemo.utils.WebDriverFactory;

/**
 * BaseTest class provides setup and teardown methods for WebDriver.
 * It initializes the WebDriver instance and navigates to the login page before each test method.
 * It also provides a method to quit the WebDriver instance after each test method.
 */
public class BaseTest {

    private static final Logger logger = LoggerFactory.getLogger(BaseTest.class);  
    private static final ThreadLocal<LoginPage> loginPageThreadLocal = new ThreadLocal<>();

    /**
	 * Sets up the WebDriver instance before each test method.
	 * Initializes the LoginPage object and navigates to the login page.
	 */
    @BeforeMethod(alwaysRun = true)
    public void setUpDriver() {
        try {
            logger.info("Setting up WebDriver with default browser.");
            // Initialize WebDriver with a default browser
            WebDriver driver = WebDriverFactory.getDriver();

            // Initialize the LoginPage object and navigate to the login page
            LoginPage loginPage = new LoginPage(driver);
            loginPage.goToLoginPage();
            loginPageThreadLocal.set(loginPage);
            logger.info("Navigated to the login page successfully.");
        } catch (Exception e) {
            logger.error("Error occurred during setup: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to set up WebDriver or navigate to the login page.", e);
        }
    }

    /**
     * Tears down the WebDriver instance after each test method.
     */
    @AfterMethod(alwaysRun = true)
    public void tearDownDriver() {
        try {
            logger.info("Tearing down WebDriver.");
            WebDriverFactory.quitDriver();
            logger.info("WebDriver quit successfully.");
        } catch (Exception e) {
            logger.error("Error occurred during teardown: {}", e.getMessage(), e);
        }
    }

    /**
	 * Provides the WebDriver instance for the current thread.
	 *
	 * @return The WebDriver instance for the current thread.
	 */
    public WebDriver getDriver() {
        return WebDriverFactory.getDriver();
    }
        
    /**
     * Provides the LoginPage instance for the current thread.
     *
     * @return The LoginPage instance for the current thread.
     */
    public LoginPage getLoginPage() {
        return loginPageThreadLocal.get();
    }
}
