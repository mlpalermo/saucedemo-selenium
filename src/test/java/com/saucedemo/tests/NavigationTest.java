package com.saucedemo.tests;

import com.saucedemo.basetest.BaseTest;
import com.saucedemo.pageobjects.CartPage;
import com.saucedemo.pageobjects.LoginPage;
import com.saucedemo.pageobjects.ProductCatalogPage;
import com.saucedemo.utils.ConfigReader;
import com.saucedemo.utils.DataProviderUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.HashMap;
import java.util.List;

public class NavigationTest extends BaseTest {

    private static final Logger logger = LoggerFactory.getLogger(NavigationTest.class);

    /**
     * Test to verify the functionality of the hamburger menu links.
     */
    @Test(dataProvider = "validUsersWithProducts", dataProviderClass = DataProviderUtil.class, groups = {"functional", "regression"})
    public void testHamburgerMenuLinks(HashMap<String, String> userData, List<HashMap<String, String>> products) {
        try {
            logger.info("Starting test: testHamburgerMenuLinks");

            // Log in to the application
        	LoginPage loginPage = getLoginPage();
            ProductCatalogPage productCatalogPage = loginPage.login(userData.get("username"), userData.get("password"));
            logger.info("User authentication successful");

            // Initialize expected cart count
            int expectedCartCount = productCatalogPage.getCartItemCount();
            logger.info("Initial cart count: {}", expectedCartCount);

            // Add products to cart
            for (HashMap<String, String> product : products) {
                String productName = product.get("name");
                productCatalogPage.addProductToCart(productName);
                expectedCartCount++;
                logger.info("Added product to cart: {}", productName);
                assertEquals(productCatalogPage.getCartItemCount(), expectedCartCount,
                        "Cart count mismatch after adding product: " + productName);
                logger.info("Cart count after adding product {}: {}", productName, expectedCartCount);
            }

            // Navigate to the Cart Page
            CartPage cartPage = productCatalogPage.goToCartPage();
            assertTrue(cartPage.isOnCartPage(), "Failed to navigate to Cart Page.");
            logger.info("Navigated to Cart Page successfully.");

            // Test "All Items"
            productCatalogPage.clickHamburgerMenuItem("All Items");
            assertTrue(productCatalogPage.isOnProductCatalogPage(), "Failed to navigate to Product Catalog Page.");
            logger.info("Verified 'All Items' menu link successfully.");

            // Test "About"
            productCatalogPage.clickHamburgerMenuItem("About");
            productCatalogPage.switchToNewTab();
            assertEquals(productCatalogPage.getCurrentUrl(), ConfigReader.getAboutPageUrl(), "About link failed.");
            logger.info("Verified 'About' menu link successfully.");
            productCatalogPage.closeCurrentTabAndSwitchBack();

            // Test "Reset App State"
            productCatalogPage.resetAppState();
            assertEquals(productCatalogPage.getCartItemCount(), 0, "Cart was not cleared after Reset App State.");
            cartPage = productCatalogPage.goToCartPage();
            assertTrue(cartPage.isCartEmpty(), "Cart is not empty after Reset App State.");
            logger.info("Verified 'Reset App State' functionality successfully.");

            // Test "Logout"
            productCatalogPage.clickHamburgerMenuItem("Logout");
            assertTrue(loginPage.isOnLoginPage(), "Failed to navigate to Login Page.");
            logger.info("Verified 'Logout' functionality successfully.");
        } catch (Exception e) {
            logger.error("Error occurred in testHamburgerMenuLinks: {}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Test to verify the functionality of social media links.
     */
    @Test(dataProvider = "validUsers", dataProviderClass = DataProviderUtil.class, groups = {"functional", "regression"})
    public void testSocialMediaLinks(HashMap<String, String> userData) {
        try {
            logger.info("Starting test: testSocialMediaLinks");

            // Log in to the application
        	LoginPage loginPage = getLoginPage();
            ProductCatalogPage productCatalogPage = loginPage.login(userData.get("username"), userData.get("password"));
            logger.info("User authentication successful");

            // Define social media platforms and their expected URLs
            HashMap<String, String> socialMediaLinks = new HashMap<>();
            socialMediaLinks.put("Twitter", ConfigReader.getTwitterUrl());
            socialMediaLinks.put("Facebook", ConfigReader.getFacebookUrl());
            socialMediaLinks.put("LinkedIn", ConfigReader.getLinkedInUrl());

            // Iterate through each social media platform and verify the link
            for (String platform : socialMediaLinks.keySet()) {
                productCatalogPage.clickSocialMediaIcon(platform);
                productCatalogPage.switchToNewTab();
                assertEquals(productCatalogPage.getCurrentUrl(), socialMediaLinks.get(platform),
                        platform + " link failed.");
                logger.info("Verified {} link successfully.", platform);
                productCatalogPage.closeCurrentTabAndSwitchBack();
            }
        } catch (Exception e) {
            logger.error("Error occurred in testSocialMediaLinks: {}", e.getMessage(), e);
            throw e;
        }
    }
}
