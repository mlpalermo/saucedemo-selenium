package com.saucedemo.tests;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.fail;

import com.saucedemo.basetest.BaseTest;
import com.saucedemo.pageobjects.*;
import com.saucedemo.utils.ConfigReader;
import com.saucedemo.utils.Messages;
import com.saucedemo.utils.DataProviderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;

public class CheckoutTest extends BaseTest {

    private static final Logger logger = LoggerFactory.getLogger(CheckoutTest.class);

    /**
     * Test to verify a valid checkout process.
     */
    @Test(dataProvider = "validUsersWithProductsAndValidCheckoutDetails", dataProviderClass = DataProviderUtil.class, groups = {"functional", "regression", "smoke"})
    public void testValidCheckout(HashMap<String, String> user, List<HashMap<String, String>> products, HashMap<String, String> checkoutDetails) {
        logger.info("Starting test: testValidCheckout");

        try {
            // Login and navigate to Product Catalog Page
        	ProductCatalogPage productCatalogPage = performLogin(user.get("username"), user.get("password"));
            logger.info("User authentication successful");

            // Add products to the cart and validate cart count
            addProductsToCart(productCatalogPage, products);
            logger.info("Added products to cart successfully.");

            // Navigate to Cart Page and validate cart details
            CartPage cartPage = navigateToCartAndValidate(productCatalogPage, products);
            logger.info("Cart details validated successfully.");

            // Navigate to Checkout: Your Information Page
            CheckoutYourInformationPage checkoutYourInformationPage = enterCheckoutInformation(cartPage, checkoutDetails);
            logger.info("Entered checkout information successfully.");

            // Navigate to Checkout: Overview Page and validate details
            CheckoutOverviewPage checkoutOverviewPage = checkoutYourInformationPage.clickContinueButton();
            validateCartOverviewProducts(checkoutOverviewPage, products);
            logger.info("Checkout Overview products validated successfully.");

            // Validate payment, shipping, and totals
            validatePaymentShippingAndTotals(checkoutOverviewPage);
            logger.info("Payment, shipping, and totals validated successfully.");

            // Complete the checkout process
            CheckoutCompletePage checkoutCompletePage = completeCheckout(checkoutOverviewPage);
            logger.info("Checkout process completed successfully.");

            // Navigate back to Product Catalog Page
            ProductCatalogPage productCatalogPageAfterCheckout = checkoutCompletePage.clickBackHomeButton();
            assertTrue(productCatalogPageAfterCheckout.isOnProductCatalogPage(), "Failed to navigate back to Product Catalog Page.");
            logger.info("Successfully navigated back to Product Catalog Page after clicking 'Back Home' button.");
        } catch (Exception e) {
            logger.error("Error occurred during testValidCheckout: {}", e.getMessage(), e);
            throw e;
        }
    }
    
	/**
	 * Test to verify invalid checkout scenarios.
	 */
    @Test(dataProvider = "validUsersWithProductsAndInvalidCheckoutDetails", dataProviderClass = DataProviderUtil.class, groups = {"functional", "negative", "regression"})
    public void testInvalidCheckout(HashMap<String, String> user, List<HashMap<String, String>> products, HashMap<String, String> checkoutDetails) {
        logger.info("Starting test: testInvalidCheckout");

        try {
            // Login and navigate to Product Catalog Page
        	ProductCatalogPage productCatalogPage = performLogin(user.get("username"), user.get("password"));
            logger.info("User authentication successful");

            // Add products to the cart and validate cart count
            addProductsToCart(productCatalogPage, products);
            logger.info("Added products to cart successfully.");

            // Navigate to Cart Page and validate cart details
            CartPage cartPage = navigateToCartAndValidate(productCatalogPage, products);
            logger.info("Cart details validated successfully.");

            // Navigate to Checkout: Your Information Page
            CheckoutYourInformationPage checkoutYourInformationPage = enterCheckoutInformation(cartPage, checkoutDetails);
            logger.info("Entered checkout information successfullly.");

            // Attempt to continue and validate error
            checkoutYourInformationPage.clickContinueButton();
            String actualErrorMessage = checkoutYourInformationPage.getErrorMessage();
            logger.info("Captured error message: {}", actualErrorMessage);

            // Validate the error message
            validateErrorMessage(checkoutDetails, actualErrorMessage);
            logger.info("Error message validated successfully.");
        } catch (Exception e) {
            logger.error("Error occurred during testInvalidCheckout: {}", e.getMessage(), e);
            throw e;
        }
    }

    /**
	 * Test to verify canceling from the Checkout: Your Information Page.
	 */
    @Test(dataProvider = "validUsersWithProducts", dataProviderClass = DataProviderUtil.class, groups = {"functional", "regression"})
    public void testCancelFromCheckoutYourInformationPage(HashMap<String, String> user, List<HashMap<String, String>> products) {
        logger.info("Starting test: testCancelFromCheckoutYourInformationPage");

        try {
            // Login and navigate to Product Catalog Page
        	ProductCatalogPage productCatalogPage = performLogin(user.get("username"), user.get("password"));
            logger.info("User authentication successful");

            // Add products to the cart and validate cart count
            addProductsToCart(productCatalogPage, products);
            logger.info("Added products to cart successfully.");

            // Navigate to Cart Page and validate cart details
            CartPage cartPage = navigateToCartAndValidate(productCatalogPage, products);
            logger.info("Cart details validated successfully.");

            // Navigate to Checkout: Your Information Page
            CheckoutYourInformationPage checkoutYourInformationPage = cartPage.clickCheckoutButton();
            logger.info("Navigated to Checkout: Your Information Page.");

            // Cancel and validate navigation back to Cart Page
            cartPage = checkoutYourInformationPage.clickCancelButton();
            assertTrue(cartPage.isOnCartPage(), "Failed to navigate back to Cart Page.");
            logger.info("Successfully navigated back to Cart Page after clicking 'Cancel' button.");
        } catch (Exception e) {
            logger.error("Error occurred during testCancelFromCheckoutYourInformationPage: {}", e.getMessage(), e);
            throw e;
        }
    }
    
    /**
	 * Test to verify canceling from the Checkout: Overview Page.
	 */     
    @Test(dataProvider = "validUsersWithProductsAndValidCheckoutDetails", dataProviderClass = DataProviderUtil.class, groups = {"functional", "regression"})
    public void testCancelFromCheckoutOverviewPage(HashMap<String, String> user, List<HashMap<String, String>> products, HashMap<String, String> checkoutDetails) {
        logger.info("Starting test: testCancelFromCheckoutOverviewPage");

        try {
            // Login and navigate to Product Catalog Page
        	ProductCatalogPage productCatalogPage = performLogin(user.get("username"), user.get("password"));
            logger.info("User authentication successful");

            // Add products to the cart and validate cart count
            addProductsToCart(productCatalogPage, products);
            logger.info("Added products to cart successfully.");

            // Navigate to Cart Page and validate cart details
            CartPage cartPage = navigateToCartAndValidate(productCatalogPage, products);
            logger.info("Cart details validated successfully.");

            // Navigate to Checkout: Your Information Page
            CheckoutYourInformationPage checkoutYourInformationPage = enterCheckoutInformation(cartPage, checkoutDetails);
            logger.info("Entered checkout information successfully.");

            // Navigate to Checkout: Overview Page and validate details
            CheckoutOverviewPage checkoutOverviewPage = checkoutYourInformationPage.clickContinueButton();
            validateCartOverviewProducts(checkoutOverviewPage, products);
            logger.info("Checkout Overview products validated successfully.");

            // Validate payment, shipping, and totals
            validatePaymentShippingAndTotals(checkoutOverviewPage);
            logger.info("Payment, shipping, and totals validated successfully.");

            // Cancel and validate navigation back to Product Catalog Page
            ProductCatalogPage productCatalogPageAfterClickingCancel = checkoutOverviewPage.clickCancelButton();
            assertTrue(productCatalogPageAfterClickingCancel.isOnProductCatalogPage(), "Failed to navigate back to Product Catalog Page.");
            logger.info("Successfully navigated back to Product Catalog Page after clicking 'Cancel' button.");
        } catch (Exception e) {
            logger.error("Error occurred during testCancelFromCheckoutOverviewPage: {}", e.getMessage(), e);
            throw e;
        }
    }

    /**
	 * Test to verify removing products from the Checkout: Overview Page via Product Details Page.
	 */         
    @Test(dataProvider = "validUsersWithProductsAndValidCheckoutDetails", dataProviderClass = DataProviderUtil.class, groups = {"functional", "regression"})
    public void testRemoveFromProductDetailsPageViaCheckoutOverviewPage(HashMap<String, String> user, List<HashMap<String, String>> products, HashMap<String, String> checkoutDetails) {
        logger.info("Starting test: testRemoveFromProductDetailsPageViaCheckoutOverviewPage");

        try {
            // Login and navigate to Product Catalog Page
        	ProductCatalogPage productCatalogPage = performLogin(user.get("username"), user.get("password"));
            logger.info("User authentication successful");

            // Add products to the cart and validate cart count
            addProductsToCart(productCatalogPage, products);
            logger.info("Added products to cart successfully.");

            // Initialize expected cart count
            int expectedCartCount = productCatalogPage.getCartItemCount();
            logger.info("Initial cart count: {}", expectedCartCount);

            // Navigate to Cart Page and validate cart details
            CartPage cartPage = navigateToCartAndValidate(productCatalogPage, products);
            logger.info("Cart details validated successfully.");

            // Navigate to Checkout: Your Information Page
            CheckoutYourInformationPage checkoutYourInformationPage = enterCheckoutInformation(cartPage, checkoutDetails);
            logger.info("Entered checkout information successfully.");

            // Navigate to Checkout: Overview Page and validate details
            CheckoutOverviewPage checkoutOverviewPage = checkoutYourInformationPage.clickContinueButton();
            validateCartOverviewProducts(checkoutOverviewPage, products);
            logger.info("Checkout Overview products validated successfully.");

            // Validate payment, shipping, and totals
            validatePaymentShippingAndTotals(checkoutOverviewPage);
            logger.info("Payment, shipping, and totals validated successfully.");

            // Remove products one by one and validate cart state
            for (HashMap<String, String> product : products) {
                String productName = product.get("name");

                // Navigate to Product Details Page
                ProductDetailsPage productDetailsPage = checkoutOverviewPage.goToProductDetailsPage(productName);
                validateProductDetails(productDetailsPage, product);
                logger.info("Product details validated on Product Details Page.");

                // Remove product from product details page
                productDetailsPage.clickRemoveButton();
                expectedCartCount--;
                logger.info("Removed product from cart: {}", productName);

                // Navigate back to Checkout Overview Page
                checkoutOverviewPage = productDetailsPage.navigateBackToCheckoutOverview();
                assertTrue(checkoutOverviewPage.isOnCheckoutOverviewPage(), "Failed to navigate back to Checkout Overview Page.");
                logger.info("Successfully navigated back to Checkout Overview Page.");

                // Check if the cart is empty
                if (checkoutOverviewPage.isCartEmpty()) {
                    logger.info("Cart is empty after removing all products.");
                    break; // Exit the loop if the cart is empty
                }

                // Validate the product is removed and other details
                assertFalse(checkoutOverviewPage.isProductInOverviewCart(productName), "Product should be removed from overview!");
                logger.info("Product {} removed from overview successfully.", productName);
                validatePaymentShippingAndTotals(checkoutOverviewPage);
                logger.info("Payment, shipping, and totals validated successfully.");
            }

            // Step 8: Validate cart count is 0
            assertEquals(expectedCartCount, 0, "Cart count should be 0 after removing all products!");
            logger.info("Cart count validated successfully after removing all products.");
        } catch (Exception e) {
            logger.error("Error occurred during testRemoveFromProductDetailsPageViaCheckoutOverviewPage: {}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Helper method to validate payment, shipping, and total computation on the Checkout Overview Page
     */
    private void validatePaymentShippingAndTotals(CheckoutOverviewPage checkoutOverviewPage) {
        assertEquals(checkoutOverviewPage.getPaymentInformation(), "SauceCard #31337", 
            "Payment information mismatch!");
        assertEquals(checkoutOverviewPage.getShippingInformation(), "Free Pony Express Delivery!", 
            "Shipping information mismatch!");
        assertTrue(checkoutOverviewPage.verifyTotalComputation(ConfigReader.getTaxRate()), 
            "Total computation mismatch!");
    }

    /**
     * Helper method to validate the details of a product on the Product Details Page
     */
    private void validateProductDetails(ProductDetailsPage productDetailsPage, HashMap<String, String> product) {
        assertEquals(productDetailsPage.getProductName(), product.get("name"), 
            "Product name mismatch!");
        assertEquals(productDetailsPage.getProductDescription(), product.get("description"), 
            "Product description mismatch!");
        assertEquals(productDetailsPage.getProductPrice(), product.get("price"), 
            "Product price mismatch!");
    }

    /**
     * Helper method to add products to the cart and validate cart count
     */
    private void addProductsToCart(ProductCatalogPage productCatalogPage, List<HashMap<String, String>> products) {
        int expectedCartCount = productCatalogPage.getCartItemCount();

        for (HashMap<String, String> product : products) {
            String productName = product.get("name");
            productCatalogPage.addProductToCart(productName);
            expectedCartCount++;
            assertEquals(productCatalogPage.getCartItemCount(), expectedCartCount, 
                "Cart count mismatch after adding product: " + productName);
        }
    }

    /**
     * Helper method to navigate to the cart page and validate details
     */
    private CartPage navigateToCartAndValidate(ProductCatalogPage productCatalogPage, List<HashMap<String, String>> products) {
        CartPage cartPage = productCatalogPage.goToCartPage();
        List<HashMap<String, String>> cartDetails = cartPage.getAllProductDetails();
        assertEquals(cartDetails, products, "Cart details mismatch!");

        return cartPage;
    }

    /**
     * Helper method to enter checkout information
     */
    private CheckoutYourInformationPage enterCheckoutInformation(CartPage cartPage, HashMap<String, String> checkoutDetails) {
        CheckoutYourInformationPage checkoutYourInformationPage = cartPage.clickCheckoutButton();
        assertTrue(checkoutYourInformationPage.isOnCheckoutYourInformationPage(), 
            "Failed to navigate to Checkout: Your Information Page!");
        checkoutYourInformationPage.enterCheckoutInformation(
            checkoutDetails.get("firstName"),
            checkoutDetails.get("lastName"),
            checkoutDetails.get("postalCode")
        );

        return checkoutYourInformationPage;
    }

    /**
     * Helper method to validate cart overview products
     */
    private void validateCartOverviewProducts(CheckoutOverviewPage checkoutOverviewPage, List<HashMap<String, String>> products) {
        List<HashMap<String, String>> overviewDetails = checkoutOverviewPage.getAllProductDetails();
        assertEquals(overviewDetails, products, "Overview details mismatch!");
    }

    /**
     * Helper method to complete the checkout process
     */
    private CheckoutCompletePage completeCheckout(CheckoutOverviewPage checkoutOverviewPage) {
        CheckoutCompletePage checkoutCompletePage = checkoutOverviewPage.clickFinishButton();
        assertEquals(checkoutCompletePage.getCompleteHeaderText(), Messages.THANK_YOU_FOR_YOUR_ORDER, 
            "Order completion header mismatch!");
        assertEquals(checkoutCompletePage.getCompleteMessageText(), Messages.ORDER_DISPATCHED_MESSAGE, 
            "Order dispatched message mismatch!");
        assertEquals(checkoutCompletePage.getCartItemCount(), 0, 
            "Cart count should be 0 after order completion!");

        return checkoutCompletePage;
    }

    /**
     * Helper method to validate error messages based on missing fields
     */
    private void validateErrorMessage(HashMap<String, String> checkoutDetails, String actualErrorMessage) {
        if (checkoutDetails.get("firstName").isEmpty()) {
            assertEquals(actualErrorMessage, Messages.FIRST_NAME_REQUIRED, 
                "Unexpected error message for missing first name!");
        } else if (checkoutDetails.get("lastName").isEmpty()) {
            assertEquals(actualErrorMessage, Messages.LAST_NAME_REQUIRED, 
                "Unexpected error message for missing last name!");
        } else if (checkoutDetails.get("postalCode").isEmpty()) {
            assertEquals(actualErrorMessage, Messages.POSTAL_CODE_REQUIRED, 
                "Unexpected error message for missing postal code!");
        } else {
            fail("Unexpected scenario: No error message expected but one was displayed.");
        }
    }
    
    /**
	 * Helper method to perform login and navigate to Product Catalog Page
	 */
    private ProductCatalogPage performLogin(String username, String password) {
        LoginPage loginPage = getLoginPage();
        return loginPage.login(username, password);
    }
}
