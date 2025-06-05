package com.saucedemo.tests;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import com.saucedemo.basetest.BaseTest;
import com.saucedemo.pageobjects.CartPage;
import com.saucedemo.pageobjects.LoginPage;
import com.saucedemo.pageobjects.ProductCatalogPage;


public class CartTest extends BaseTest {

    private static final Logger logger = LoggerFactory.getLogger(CartTest.class);

    /**
     * Test to add all products to the cart and then remove all products from the cart.
     */
    @Test(dataProvider = "validUsersWithProducts", dataProviderClass = com.saucedemo.utils.DataProviderUtil.class, groups = {"functional", "regression"})
    public void testAddAllProductsToCartAndRemove(HashMap<String, String> user, List<HashMap<String, String>> products) {
        try {
            logger.info("Starting test: testAddAllProductsToCartAndRemove");

            // Login and navigate to Product Catalog Page
        	LoginPage loginPage = getLoginPage();
            ProductCatalogPage productCatalogPage = loginPage.login(user.get("username"), user.get("password"));
            logger.info("User authentication successful");

            // Initialize expected cart count
            int expectedCartCount = productCatalogPage.getCartItemCount();
            logger.info("Initial cart count: {}", expectedCartCount);

            // Add each product to the cart and verify cart count incrementally
            for (HashMap<String, String> product : products) {
                String productName = product.get("name");
                productCatalogPage.addProductToCart(productName);
                expectedCartCount++;
                logger.info("Added product to cart: {}", productName);
                assertEquals(productCatalogPage.getCartItemCount(), expectedCartCount,
                        "Cart count mismatch after adding product: " + productName);
                logger.info("Cart count verified successfully after adding product: {}", productName);
            }

            // Go to Cart Page and verify product details
            CartPage cartPage = productCatalogPage.goToCartPage();
            List<HashMap<String, String>> cartProducts = cartPage.getAllProductDetails();
            assertEquals(cartProducts, products, "Cart details mismatch!");
            logger.info("Verified cart details successfully.");

            // Remove products one by one and verify cart count dynamically
            for (HashMap<String, String> product : products) {
                String productName = product.get("name");

                // Validate if the "Remove" button is displayed
                assertTrue(cartPage.isRemoveButtonDisplayed(),
                        "'Remove' button is not displayed for product: " + productName);
                logger.info("Verified 'Remove' button is displayed for product: {}", productName);

                // Remove the product from the cart
                cartPage.removeProductFromCart(productName);
                expectedCartCount--;
                logger.info("Removed product from cart: {}", productName);
                assertEquals(cartPage.getCartItemCount(), expectedCartCount,
                        "Cart count mismatch after removing product: " + productName);
                logger.info("Cart count verified successfully after removing product: {}", productName);
            }

            // Continue shopping and verify return to Product Catalog Page
            productCatalogPage = cartPage.clickContinueShoppingButton();
            assertTrue(productCatalogPage.isOnProductCatalogPage(), "Failed to navigate back to Product Catalog Page.");
            logger.info("Successfully navigated back to Product Catalog Page.");
        } catch (Exception e) {
            logger.error("Error occurred in testAddAllProductsToCart: {}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Test to add products to the cart one by one and verify cart functionality.
     */
    @Test(dataProvider = "validUsersWithProducts", dataProviderClass = com.saucedemo.utils.DataProviderUtil.class, groups = {"functional", "regression"})
    public void testAddProductsToCartOneByOne(HashMap<String, String> user, List<HashMap<String, String>> products) {
        try {
            logger.info("Starting test: testAddProductsToCartOneByOne");

            // Login and navigate to Product Catalog Page
        	LoginPage loginPage = getLoginPage();
            ProductCatalogPage productCatalogPage = loginPage.login(user.get("username"), user.get("password"));
            logger.info("User authentication successful");

            // Initialize expected cart count
            int expectedCartCount = productCatalogPage.getCartItemCount();
            logger.info("Initial cart count: {}", expectedCartCount);

            for (HashMap<String, String> product : products) {
                String productName = product.get("name");

                // Add product to cart
                productCatalogPage.addProductToCart(productName);
                expectedCartCount++;
                logger.info("Added product to cart: {}", productName);

                // Verify cart count
                assertEquals(productCatalogPage.getCartItemCount(), expectedCartCount,
                        "Cart count mismatch after adding product: " + productName);
                logger.info("Cart count verified successfully after adding product: {}", productName);

                // Go to Cart Page and verify product details
                CartPage cartPage = productCatalogPage.goToCartPage();
                List<HashMap<String, String>> cartProducts = cartPage.getAllProductDetails();

                // Verify cart product count and details
                HashMap<String, String> lastAddedProduct = cartProducts.get(cartProducts.size() - 1);
                assertEquals(lastAddedProduct.get("name"), productName, "Product name mismatch.");
                logger.info("Verified product name in cart successfully for product: {}", productName);
                assertEquals(lastAddedProduct.get("description"), product.get("description"),
                        "Product description mismatch.");
                logger.info("Verified product description in cart successfully for product: {}", productName);
                assertEquals(lastAddedProduct.get("price"), product.get("price"), "Product price mismatch.");
                logger.info("Verified product price in cart successfully for product: {}", productName);

                // Validate if the "Remove" button is displayed
                assertTrue(cartPage.isRemoveButtonDisplayed(),
                        "'Remove' button is not displayed for product: " + productName);
                logger.info("Verified 'Remove' button is displayed for product: {}", productName);

                // Continue shopping
                productCatalogPage = cartPage.clickContinueShoppingButton();
                assertTrue(productCatalogPage.isOnProductCatalogPage(), "Failed to navigate back to Product Catalog Page.");
                logger.info("Successfully navigated back to Product Catalog Page after adding product: {}", productName);
            }
        } catch (Exception e) {
            logger.error("Error occurred in testAddProductsToCartOneByOne: {}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Test to add and remove products from the cart and verify cart functionality.
     */
    @Test(dataProvider = "validUsersWithProducts", dataProviderClass = com.saucedemo.utils.DataProviderUtil.class, groups = {"functional", "regression"})
    public void testAddAndRemoveProductsFromCart(HashMap<String, String> user, List<HashMap<String, String>> products) {
        try {
            logger.info("Starting test: testAddAndRemoveProductsFromCart");

            // Login and navigate to Product Catalog Page
        	LoginPage loginPage = getLoginPage();
            ProductCatalogPage productCatalogPage = loginPage.login(user.get("username"), user.get("password"));
            logger.info("User authentication successful");

            // Initialize expected cart count
            int expectedCartCount = productCatalogPage.getCartItemCount();
            logger.info("Initial cart count: {}", expectedCartCount);

            for (HashMap<String, String> product : products) {
                String productName = product.get("name");

                // Add product to cart
                productCatalogPage.addProductToCart(productName);
                expectedCartCount++;
                logger.info("Added product to cart: {}", productName);

                // Verify cart count
                assertEquals(productCatalogPage.getCartItemCount(), expectedCartCount,
                        "Cart count mismatch after adding product: " + productName);
                logger.info("Cart count verified successfully after adding product: {}", productName);

                // Go to Cart Page and verify product details
                CartPage cartPage = productCatalogPage.goToCartPage();
                List<HashMap<String, String>> cartProducts = cartPage.getAllProductDetails();

                // Verify cart product count and details
                HashMap<String, String> lastAddedProduct = cartProducts.get(cartProducts.size() - 1);
                assertEquals(lastAddedProduct.get("name"), productName, "Product name mismatch.");
                logger.info("Verified product name in cart successfully for product: {}", productName);
                assertEquals(lastAddedProduct.get("description"), product.get("description"),
                        "Product description mismatch.");
                logger.info("Verified product description in cart successfully for product: {}", productName);
                assertEquals(lastAddedProduct.get("price"), product.get("price"), "Product price mismatch.");
                logger.info("Verified product price in cart successfully for product: {}", productName);

                // Validate if the "Remove" button is displayed
                assertTrue(cartPage.isRemoveButtonDisplayed(),
                        "'Remove' button is not displayed for product: " + productName);
                logger.info("Verified 'Remove' button is displayed for product: {}", productName);

                // Remove product
                cartPage.removeProductFromCart(productName);
                expectedCartCount--;
                logger.info("Removed product from cart: {}", productName);
                assertEquals(cartPage.getCartItemCount(), expectedCartCount,
                        "Cart count mismatch after removing product: " + productName);
                logger.info("Cart count verified successfully after removing product: {}", productName);

                // Continue shopping
                productCatalogPage = cartPage.clickContinueShoppingButton();
                assertTrue(productCatalogPage.isOnProductCatalogPage(), "Failed to navigate back to Product Catalog Page.");
                logger.info("Successfully navigated back to Product Catalog Page after removing product: {}", productName);
            }
        } catch (Exception e) {
            logger.error("Error occurred in testAddAndRemoveProductsFromCart: {}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Test to verify cart persistence after logout and re-login.
     */
    @Test(dataProvider = "validUsersWithProducts", dataProviderClass = com.saucedemo.utils.DataProviderUtil.class, groups = {"functional", "regression"})
    public void testPersistentCart(HashMap<String, String> user, List<HashMap<String, String>> products) {
        try {
            logger.info("Starting test: testPersistentCart");

            // Login and navigate to Product Catalog Page
        	LoginPage loginPage = getLoginPage();
            ProductCatalogPage productCatalogPage = loginPage.login(user.get("username"), user.get("password"));
            logger.info("User authentication successful");

            // Initialize expected cart count
            int expectedCartCount = productCatalogPage.getCartItemCount();
            logger.info("Initial cart count: {}", expectedCartCount);

            // Add each product to the cart and verify cart count incrementally

            for (HashMap<String, String> product : products) {
                String productName = product.get("name");
                productCatalogPage.addProductToCart(productName);
                expectedCartCount++;
                logger.info("Added product to cart: {}", productName);
                assertEquals(productCatalogPage.getCartItemCount(), expectedCartCount,
                        "Cart count mismatch after adding product: " + productName);
                logger.info("Cart count verified successfully after adding product: {}", productName);
            }

            // Go to Cart Page and verify product details
            CartPage cartPage = productCatalogPage.goToCartPage();
            List<HashMap<String, String>> cartProducts = cartPage.getAllProductDetails();
            assertEquals(cartProducts, products, "Cart details mismatch!");
            logger.info("Verified cart details successfully.");

            // Logout and login again
            cartPage.logout();
            logger.info("Logged out successfully.");
            productCatalogPage = loginPage.login(user.get("username"), user.get("password"));
            logger.info("Re-logged in successfully.");

            // Verify cart persistence after re-login
            assertEquals(productCatalogPage.getCartItemCount(), expectedCartCount, "Cart count mismatch after re-login.");
            logger.info("Cart count verified successfully after re-login.");
            cartPage = productCatalogPage.goToCartPage();
            cartProducts = cartPage.getAllProductDetails();
            assertEquals(cartProducts, products, "Cart details mismatch!");
            logger.info("Cart persistence verified successfully after re-login.");
        } catch (Exception e) {
            logger.error("Error occurred in testPersistentCart: {}", e.getMessage(), e);
            throw e;
        }
    }
}

