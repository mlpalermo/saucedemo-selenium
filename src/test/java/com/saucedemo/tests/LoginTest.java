package com.saucedemo.tests;

import com.saucedemo.basetest.BaseTest;
import com.saucedemo.pageobjects.LoginPage;
import com.saucedemo.pageobjects.ProductCatalogPage;
import com.saucedemo.utils.Messages;
import com.saucedemo.utils.DataProviderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.HashMap;

public class LoginTest extends BaseTest {

    private static final Logger logger = LoggerFactory.getLogger(LoginTest.class);

    /**
     * Test to verify valid login functionality.
     */
    @Test(dataProvider = "validUsers", dataProviderClass = DataProviderUtil.class, groups = {"smoke", "functional", "regression"})
    public void testValidLogin(HashMap<String, String> userData) {
        String username = userData.get("username");
        String password = userData.get("password");

        logger.info("Starting test: testValidLogin");

        try {
            // Perform login
        	LoginPage loginPage = getLoginPage();
            ProductCatalogPage productCatalogPage = loginPage.login(username, password);

            // Validate navigation to Product Catalog Page
            assertTrue(productCatalogPage.isOnProductCatalogPage(), "Failed to navigate to Product Catalog Page.");
            logger.info("Successfully logged in and navigated to Product Catalog Page.");

        } catch (Exception e) {
            logger.error("Error occurred in testValidLogin: {}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Test to verify invalid login scenarios.
     */
    @Test(dataProvider = "invalidUsers", dataProviderClass = DataProviderUtil.class, groups = {"functional", "negative", "regression"})
    public void testInvalidLogin(HashMap<String, String> userData) {
        String username = userData.get("username");
        String password = userData.get("password");

        logger.info("Starting test: testInvalidLogin");

        try {
            // Perform login
        	LoginPage loginPage = getLoginPage();
            loginPage.login(username, password);

            // Get the actual error message displayed on the login page
            String actualErrorMessage = loginPage.getErrorMessage();

            // Validate the error message based on the input conditions
            if (username.isEmpty() && password.isEmpty()) {
                assertEquals(actualErrorMessage, Messages.USERNAME_REQUIRED, "Error message mismatch for empty username and password!");
                logger.info("Username and password are empty. Expected error message: {}", Messages.USERNAME_REQUIRED);
            } else if (username.isEmpty()) {
                assertEquals(actualErrorMessage, Messages.USERNAME_REQUIRED, "Error message mismatch for empty username!");
                logger.info("Username is empty. Expected error message: {}", Messages.USERNAME_REQUIRED);
            } else if (password.isEmpty()) {
                assertEquals(actualErrorMessage, Messages.PASSWORD_REQUIRED, "Error message mismatch for empty password!");
                logger.info("Password is empty. Expected error message: {}", Messages.PASSWORD_REQUIRED);
            } else if ("locked_out_user".equals(username)) {
                assertEquals(actualErrorMessage, Messages.LOCKED_OUT_USER, "Error message mismatch for locked out user!");
                logger.info("User is locked out. Expected error message: {}", Messages.LOCKED_OUT_USER);
            } else {
                assertEquals(actualErrorMessage, Messages.INVALID_CREDENTIALS, "Error message mismatch for invalid credentials!");
                logger.info("Invalid credentials provided. Expected error message: {}", Messages.INVALID_CREDENTIALS);
            }

            logger.info("Successfully validated error message for invalid login.");

        } catch (Exception e) {
            logger.error("Error occurred in testInvalidLogin: {}", e.getMessage(), e);
            throw e;
        }
    }
}
