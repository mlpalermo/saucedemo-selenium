package com.saucedemo.tests;

import com.saucedemo.basetest.BaseTest;
import com.saucedemo.pageobjects.CartPage;
import com.saucedemo.pageobjects.LoginPage;
import com.saucedemo.pageobjects.ProductCatalogPage;
import com.saucedemo.pageobjects.ProductDetailsPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.HashMap;
import java.util.List;

public class ProductDetailsTest extends BaseTest {

    private static final Logger logger = LoggerFactory.getLogger(ProductDetailsTest.class);

    /**
     * Test to remove a product from the Product Details Page via the Product Catalog Page.
     */
    @Test(dataProvider = "validUsersWithProducts", dataProviderClass = com.saucedemo.utils.DataProviderUtil.class, groups = {"functional", "regression"})
    public void testRemoveFromProductDetailsPageViaProductCatalogPage(HashMap<String, String> user, List<HashMap<String, String>> products) {
        logger.info("Starting test: testRemoveFromProductDetailsPageViaProductCatalogPage");

        try {
            // Login and navigate to Product Catalog Page
        	ProductCatalogPage productCatalogPage = performLogin(user.get("username"), user.get("password"));
            logger.info("User authentication successful");
            
            // Initialize expected cart count
            int expectedCartCount = productCatalogPage.getCartItemCount();
            logger.info("Initial cart count: {}", expectedCartCount);

            for (HashMap<String, String> product : products) {
                logger.info("Testing product: {}", product.get("name"));

                // Add product to cart
                productCatalogPage.addProductToCart(product.get("name"));
                expectedCartCount++;
                logger.info("Product added to cart: {}", product.get("name"));
                assertEquals(productCatalogPage.getCartItemCount(), expectedCartCount, "Cart count mismatch after adding product.");
                logger.info("Cart count after adding product: {}", expectedCartCount);

                // Navigate to Product Details Page
                ProductDetailsPage productDetailsPage = productCatalogPage.goToProductDetailsPageByName(product.get("name"));
                assertTrue(productDetailsPage.isOnProductDetailsPage(), "Failed to navigate to Product Details Page.");
                logger.info("Navigated to Product Details Page for product: {}", product.get("name"));

                // Verify product details
                verifyProductDetails(productDetailsPage, product);
                logger.info("Verified product details on Product Details Page.");

                // Remove product from cart
                productDetailsPage.clickRemoveButton();
                expectedCartCount--;
                logger.info("Product removed from cart: {}", product.get("name"));
                assertTrue(productDetailsPage.isAddToCartButtonDisplayed(), "Add to Cart button is not displayed after removing product.");
                logger.info("Add to Cart button displayed after removing product.");
                assertEquals(productDetailsPage.getCartItemCount(), expectedCartCount, "Cart count mismatch after removing product.");
                logger.info("Cart count after removing product: {}", expectedCartCount);

                // Navigate back to Product Catalog Page
                productCatalogPage = productDetailsPage.clickBackToProducts();
                assertTrue(productCatalogPage.isOnProductCatalogPage(), "Failed to navigate back to Product Catalog Page.");
                logger.info("Successfully navigated back to Product Catalog Page.");
            }
        } catch (Exception e) {
            logger.error("Error occurred in testRemoveFromProductDetailsPageViaProductCatalogPage: {}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Test to add a product to the cart from the Product Details Page via the Product Catalog Page.
     */
    @Test(dataProvider = "validUsersWithProducts", dataProviderClass = com.saucedemo.utils.DataProviderUtil.class, groups = {"functional", "regression"})
    public void testAddToCartFromProductDetailsPageViaProductCatalogPage(HashMap<String, String> user, List<HashMap<String, String>> products) {
        logger.info("Starting test: testAddToCartFromProductDetailsPageViaProductCatalogPage");

        try {
            // Login and navigate to Product Catalog Page
        	ProductCatalogPage productCatalogPage = performLogin(user.get("username"), user.get("password"));
            logger.info("User authentication successful");
            
            // Initialize expected cart count
            int expectedCartCount = productCatalogPage.getCartItemCount();
            logger.info("Initial cart count: {}", expectedCartCount);

            for (HashMap<String, String> product : products) {
                logger.info("Testing product: {}", product.get("name"));

                // Navigate to Product Details Page
                ProductDetailsPage productDetailsPage = productCatalogPage.goToProductDetailsPageByName(product.get("name"));
                assertTrue(productDetailsPage.isOnProductDetailsPage(), "Failed to navigate to Product Details Page.");
                logger.info("Navigated to Product Details Page for product: {}", product.get("name"));

                // Verify product details
                verifyProductDetails(productDetailsPage, product);
                logger.info("Verified product details on Product Details Page.");

                // Add product to cart
                productDetailsPage.clickAddToCartButton();
                expectedCartCount++;
                logger.info("Product added to cart: {}", product.get("name"));
                assertTrue(productDetailsPage.isRemoveButtonDisplayed(), "Remove button is not displayed after adding product.");
                logger.info("Remove button displayed after adding product.");
                assertEquals(productDetailsPage.getCartItemCount(), expectedCartCount, "Cart count mismatch after adding product.");
                logger.info("Cart count after adding product: {}", expectedCartCount);

                // Navigate back to Product Catalog Page
                productCatalogPage = productDetailsPage.clickBackToProducts();
                assertTrue(productCatalogPage.isOnProductCatalogPage(), "Failed to navigate back to Product Catalog Page.");
                logger.info("Successfully navigated back to Product Catalog Page.");
            }
        } catch (Exception e) {
            logger.error("Error occurred in testAddToCartFromProductDetailsPageViaProductCatalogPage: {}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Test to remove a product from the Product Details Page via the Cart Page.
     */
    @Test(dataProvider = "validUsersWithProducts", dataProviderClass = com.saucedemo.utils.DataProviderUtil.class, groups = {"functional", "regression"})
    public void testRemoveFromProductDetailsPageViaCartPage(HashMap<String, String> user, List<HashMap<String, String>> products) {
        logger.info("Starting test: testRemoveFromProductDetailsPageViaCartPage");

        try {
            // Login and navigate to Product Catalog Page
        	ProductCatalogPage productCatalogPage = performLogin(user.get("username"), user.get("password"));
            logger.info("User authentication successful");
            
            // Initialize expected cart count
            int expectedCartCount = productCatalogPage.getCartItemCount();
            logger.info("Initial cart count: {}", expectedCartCount);

            for (HashMap<String, String> product : products) {
                logger.info("Testing product: {}", product.get("name"));

                // Add product to cart
                productCatalogPage.addProductToCart(product.get("name"));
                expectedCartCount++;
                logger.info("Product added to cart: {}", product.get("name"));
                assertEquals(productCatalogPage.getCartItemCount(), expectedCartCount, "Cart count mismatch after adding product.");
                logger.info("Cart count after adding product: {}", expectedCartCount);

                // Navigate to Cart Page
                CartPage cartPage = productCatalogPage.goToCartPage();
                assertTrue(cartPage.isOnCartPage(), "Failed to navigate to Cart Page.");
                logger.info("Successfully navigated to Cart Page.");

                // Verify product details in Cart Page
                assertTrue(cartPage.isProductInCart(product.get("name")), "Product is not in the cart.");
                logger.info("Product is in the cart: {}", product.get("name"));
                assertEquals(cartPage.getProductDescription(product.get("name")), product.get("description"), "Product description mismatch in cart.");
                logger.info("Product description in cart matches: {}", product.get("description"));
                assertEquals(cartPage.getProductPrice(product.get("name")), product.get("price"), "Product price mismatch in cart.");
                logger.info("Product price in cart matches: {}", product.get("price"));

                // Navigate to Product Details Page
                ProductDetailsPage productDetailsPage = cartPage.goToProductDetailsPage(product.get("name"));
                assertTrue(productDetailsPage.isOnProductDetailsPage(), "Failed to navigate to Product Details Page.");
                logger.info("Navigated to Product Details Page for product: {}", product.get("name"));

                // Verify product details
                verifyProductDetails(productDetailsPage, product);
                logger.info("Verified product details on Product Details Page.");

                // Remove product from cart
                productDetailsPage.clickRemoveButton();
                expectedCartCount--;
                logger.info("Product removed from cart: {}", product.get("name"));
                assertTrue(productDetailsPage.isAddToCartButtonDisplayed(), "Add to Cart button is not displayed after removing product.");
                logger.info("Add to Cart button displayed after removing product.");
                assertEquals(productDetailsPage.getCartItemCount(), expectedCartCount, "Cart count mismatch after removing product.");
                logger.info("Cart count after removing product: {}", expectedCartCount);

                // Navigate back to Product Catalog Page
                productCatalogPage = productDetailsPage.clickBackToProducts();
                assertTrue(productCatalogPage.isOnProductCatalogPage(), "Failed to navigate back to Product Catalog Page.");
                logger.info("Successfully navigated back to Product Catalog Page.");
            }
        } catch (Exception e) {
            logger.error("Error occurred in testRemoveFromProductDetailsPageViaCartPage: {}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Helper method to verify product details on the Product Details Page.
     */
    private void verifyProductDetails(ProductDetailsPage productDetailsPage, HashMap<String, String> product) {
        assertEquals(productDetailsPage.getProductName(), product.get("name"), "Product name mismatch.");
        assertEquals(productDetailsPage.getProductDescription(), product.get("description"), "Product description mismatch.");
        assertEquals(productDetailsPage.getProductPrice(), product.get("price"), "Product price mismatch.");
    }
    
    /**
	 * Helper method to perform login and navigate to Product Catalog Page
	 */
    private ProductCatalogPage performLogin(String username, String password) {
        LoginPage loginPage = getLoginPage();
        return loginPage.login(username, password);
    }
}
