package com.saucedemo.tests;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import com.saucedemo.basetest.BaseTest;
import com.saucedemo.pageobjects.LoginPage;
import com.saucedemo.pageobjects.ProductCatalogPage;
import com.saucedemo.utils.DataProviderUtil;

public class ProductCatalogTest extends BaseTest {

    private static final Logger logger = LoggerFactory.getLogger(ProductCatalogTest.class);

    /**
     * Test to verify product details on the Product Catalog Page.
     */
    @Test(dataProvider = "validUsersWithProducts", dataProviderClass = DataProviderUtil.class, groups = {"functional", "regression"})
    public void verifyProductDetails(HashMap<String, String> user, List<HashMap<String, String>> products) {
        logger.info("Starting test: verifyProductDetails");
        try {
            // Login and navigate to Product Catalog Page
            ProductCatalogPage productCatalogPage = performLogin(user.get("username"), user.get("password"));
            logger.info("User authentication successful");

            // Fetch all product details
            List<HashMap<String, String>> actualProductDetails = productCatalogPage.getAllProductDetails();
            logger.info("Expected product count: {}, Actual product count: {}", products.size(), actualProductDetails.size());
            logger.info("Actual product details: {}", actualProductDetails);

            // Compare only the subset of actualProductDetails that matches the size of the test data
            List<HashMap<String, String>> actualSubset = actualProductDetails.subList(0, products.size());
            assertEquals(actualSubset, products, "Product details mismatch!");
            logger.info("Verified product details successfully.");
        } catch (Exception e) {
            logger.error("Error occurred in verifyProductDetails: {}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Test to add products to the cart and verify cart count incrementally.
     */
    @Test(dataProvider = "validUsersWithProducts", dataProviderClass = DataProviderUtil.class, groups = {"functional", "regression"})
    public void addProductsToCart(HashMap<String, String> user, List<HashMap<String, String>> products) {
        logger.info("Starting test: addProductsToCart");
        try {
            // Login and navigate to Product Catalog Page
        	ProductCatalogPage productCatalogPage = performLogin(user.get("username"), user.get("password"));
            logger.info("User authentication successful");

            // Add each product to the cart and verify cart count incrementally
            addProductToCartAndVerify(productCatalogPage, products);
            logger.info("Added products to cart and verified count.");
        } catch (Exception e) {
            logger.error("Error occurred in addProductsToCart: {}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Test to add and remove products from the cart and verify cart count dynamically.
     */
    @Test(dataProvider = "validUsersWithProducts", dataProviderClass = DataProviderUtil.class, groups = {"functional", "regression"})
    public void testAddAndRemoveProducts(HashMap<String, String> user, List<HashMap<String, String>> products) {
        logger.info("Starting test: testAddAndRemoveProducts");
        try {
            // Login and navigate to Product Catalog Page
        	ProductCatalogPage productCatalogPage = performLogin(user.get("username"), user.get("password"));
            logger.info("User authentication successful");

            // Add each product to the cart and verify cart count incrementally
            addProductToCartAndVerify(productCatalogPage, products);
            logger.info("Added products to cart and verified count.");

            // Remove products one by one and verify cart count decreases
            removeProductFromCartAndVerify(productCatalogPage, products);
            logger.info("Removed products from cart and verified count.");
        } catch (Exception e) {
            logger.error("Error occurred in testAddAndRemoveProducts: {}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Test to verify product sorting functionality on the Product Catalog Page.
     */
    @Test(dataProvider = "validUsers", dataProviderClass = DataProviderUtil.class, groups = {"functional", "regression"})
    public void testProductSorting(HashMap<String, String> user) {
        logger.info("Starting test: testProductSorting");
        try {
            // Login and navigate to Product Catalog Page
        	ProductCatalogPage productCatalogPage = performLogin(user.get("username"), user.get("password"));
            logger.info("User authentication successful");

            // Verify sorting by Name (A to Z)
            productCatalogPage.sortProducts("Name (A to Z)");
            List<String> sortedNamesAsc = productCatalogPage.getAllProductNames();
            assertTrue(isSorted(sortedNamesAsc, true), "Products are not sorted by Name (A to Z)");
            logger.info("Verified sorting by Name (A to Z)");

            // Verify sorting by Name (Z to A)
            productCatalogPage.sortProducts("Name (Z to A)");
            List<String> sortedNamesDesc = productCatalogPage.getAllProductNames();
            assertTrue(isSorted(sortedNamesDesc, false), "Products are not sorted by Name (Z to A)");
            logger.info("Verified sorting by Name (Z to A)");

            // Verify sorting by Price (low to high)
            productCatalogPage.sortProducts("Price (low to high)");
            List<Double> sortedPricesAsc = productCatalogPage.getAllProductPrices();
            assertTrue(isSorted(sortedPricesAsc, true), "Products are not sorted by Price (low to high)");
            logger.info("Verified sorting by Price (low to high)");

            // Verify sorting by Price (high to low)
            productCatalogPage.sortProducts("Price (high to low)");
            List<Double> sortedPricesDesc = productCatalogPage.getAllProductPrices();
            assertTrue(isSorted(sortedPricesDesc, false), "Products are not sorted by Price (high to low)");
            logger.info("Verified sorting by Price (high to low)");
        } catch (Exception e) {
            logger.error("Error occurred in testProductSorting: {}", e.getMessage(), e);
            throw e;
        }
    }

    /** 
     * Helper method to check if a list is sorted
     */
    private <T extends Comparable<T>> boolean isSorted(List<T> list, boolean ascending) {
        for (int i = 1; i < list.size(); i++) {
            if (ascending && list.get(i - 1).compareTo(list.get(i)) > 0) {
                return false;
            } else if (!ascending && list.get(i - 1).compareTo(list.get(i)) < 0) {
                return false;
            }
        }
        return true;
    }
    
    /**
    * Helper method to add a product to the cart and verify the cart count
    */
    private void addProductToCartAndVerify(ProductCatalogPage productCatalogPage, List<HashMap<String, String>> products) {
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
     * Helper method to remove a product from the cart and verify the cart count
     */
    private void removeProductFromCartAndVerify(ProductCatalogPage productCatalogPage, List<HashMap<String, String>> products) {
        int expectedCartCount = productCatalogPage.getCartItemCount();

        for (HashMap<String, String> product : products) {
        String productName = product.get("name");
    	productCatalogPage.removeProductFromCart(productName);
    	expectedCartCount--;
        assertEquals(productCatalogPage.getCartItemCount(), expectedCartCount, 
            "Cart count mismatch after removing product: " + productName);
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
