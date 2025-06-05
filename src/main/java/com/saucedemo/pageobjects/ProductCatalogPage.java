package com.saucedemo.pageobjects;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.saucedemo.basepage.BasePage;

/**
 * Page Object Model class for the Product Catalog page of the application.
 * This class contains methods to interact with the product catalog page elements.
 */
public class ProductCatalogPage extends BasePage {

    private static final Logger logger = LoggerFactory.getLogger(ProductCatalogPage.class);
        
    /**
	 * Constructor for ProductCatalogPage.
	 *
	 * @param driver WebDriver instance.
	 */
    public ProductCatalogPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    // Locators for product catalog page elements
    @FindBy(css = ".product_sort_container")
    private WebElement sortDropdown;
    
    private final By productsBy = By.cssSelector(".inventory_item");
    private final By productNameBy = By.cssSelector(".inventory_item_name");
    private final By productDescriptionBy = By.cssSelector(".inventory_item_desc");
    private final By productPriceBy = By.cssSelector(".inventory_item_price");
    private final By addToCartButtonBy = By.cssSelector(".btn_inventory");
    private final By removeButtonBy = By.cssSelector(".btn_secondary");
    private final By productImageBy = By.cssSelector(".inventory_item_img");

    /**
     * Fetches the list of all products displayed on the page.
     *
     * @return List of WebElements representing the products.
     */
    public List<WebElement> getProductList() {
        try {
            waitForElementToAppear(productsBy);
            List<WebElement> productList = driver.findElements(productsBy);
            logger.info("Fetched the list of all products.");
            return productList;
        } catch (Exception e) {
            logger.error("Error while fetching the product list.", e);
            throw e;
        }
    }

    /**
     * Retrieves details of all products on the page.
     *
     * @return List of HashMaps containing product details.
     */
    public List<HashMap<String, String>> getAllProductDetails() {
        try {
            logger.info("Fetching all product details.");
            return getProductList().stream().map(item -> {
                HashMap<String, String> productDetails = new HashMap<>();
                productDetails.put("name", item.findElement(productNameBy).getText());
                productDetails.put("description", item.findElement(productDescriptionBy).getText());
                productDetails.put("price", item.findElement(productPriceBy).getText());
                return productDetails;
            }).collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error while fetching product details.", e);
            throw e;
        }
    }

    /**
     * Retrieves a product WebElement by its name.
     *
     * @param productName The name of the product.
     * @return WebElement representing the product, or null if not found.
     */
    public WebElement getProductByName(String productName) {
        try {
            logger.info("Searching for product: {}", productName);
            return getProductList().stream()
                    .filter(product -> product.findElement(productNameBy).getText().equals(productName))
                    .findFirst()
                    .orElse(null);
        } catch (Exception e) {
            logger.error("Error while searching for product: {}", productName, e);
            throw e;
        }
    }

    /**
     * Adds a product to the cart by its name.
     *
     * @param productName The name of the product to add.
     */
    public void addProductToCart(String productName) {
        try {
            WebElement product = getProductByName(productName);
            if (product != null) {
                product.findElement(addToCartButtonBy).click();
                logger.info("Added product '{}' to the cart.", productName);
            } else {
                logger.warn("Product '{}' not found for adding to the cart.", productName);
            }
        } catch (Exception e) {
            logger.error("Error while adding product '{}' to the cart.", productName, e);
            throw e;
        }
    }

    /**
     * Removes a product from the cart by its name.
     *
     * @param productName The name of the product to remove.
     */
    public void removeProductFromCart(String productName) {
        try {
            WebElement product = getProductByName(productName);
            if (product != null) {
                product.findElement(removeButtonBy).click();
                logger.info("Removed product '{}' from the cart.", productName);
            } else {
                logger.warn("Product '{}' not found for removal from the cart.", productName);
            }
        } catch (Exception e) {
            logger.error("Error while removing product '{}' from the cart.", productName, e);
            throw e;
        }
    }

    /**
     * Sorts products based on the given sort option.
     *
     * @param sortOption The sort option (e.g., "Name (A to Z)", "Price (low to high)").
     */
    public void sortProducts(String sortOption) {
        try {
            logger.info("Sorting products by: {}", sortOption);
            new Select(sortDropdown).selectByVisibleText(sortOption);
            waitForElementToAppear(productsBy);

            switch (sortOption) {
                case "Name (A to Z)":
                    validateSorting(getAllProductNames(), true, "name");
                    break;
                case "Name (Z to A)":
                    validateSorting(getAllProductNames(), false, "name");
                    break;
                case "Price (low to high)":
                    validateSorting(getAllProductPrices(), true, "price");
                    break;
                case "Price (high to low)":
                    validateSorting(getAllProductPrices(), false, "price");
                    break;
                default:
                    logger.warn("Invalid sort option: {}", sortOption);
            }
        } catch (Exception e) {
            logger.error("Error while sorting products by: {}", sortOption, e);
            throw e;
        }
    }

    /**
     * Validates if the products are sorted correctly.
     *
     * @param list      The list of items to validate.
     * @param ascending True for ascending order, false for descending.
     * @param type      The type of sorting (e.g., "name", "price").
     */
    private <T extends Comparable<T>> void validateSorting(List<T> list, boolean ascending, String type) {
        boolean isSorted;
        if (ascending) {
            isSorted = list.equals(list.stream().sorted().collect(Collectors.toList()));
        } else {
            isSorted = list.equals(list.stream().sorted((a, b) -> b.compareTo(a)).collect(Collectors.toList()));
        }

        if (isSorted) {
            logger.info("Products are sorted in {} order by {}.", ascending ? "ascending" : "descending", type);
        } else {
            logger.warn("Products are NOT sorted correctly by {}.", type);
        }
    }


    /**
     * Navigates to the product details page for a specific product.
     *
     * @param productName The name of the product.
     * @return A new instance of ProductDetailsPage if navigation succeeds.
     * @throws NoSuchElementException if product or product name element is not found or not displayed.
     */
    public ProductDetailsPage goToProductDetailsPageByName(String productName) {
        try {
            logger.info("Navigating to the product details page by clicking the name for '{}'.", productName);
            WebElement product = getProductByName(productName);
            if (product == null) {
            	logger.warn("Product '{}' not found for navigation.", productName);
                throw new NoSuchElementException("Product '" + productName + "' not found for navigation.");
            }

            WebElement productNameElement = product.findElement(productNameBy);
            if (!productNameElement.isDisplayed()) {
            	logger.warn("Product name for '{}' is not displayed.", productName);
                throw new NoSuchElementException("Product name element for '" + productName + "' is not displayed.");
            }
            productNameElement.click();
            logger.info("Navigated to the details page of product: {}", productName);
            return new ProductDetailsPage(driver);
        } catch (Exception e) {
            logger.error("Error while navigating to product details page by name for '{}'.", productName, e);
            throw e;
        }
    }
    
    /**
     * Navigates to the product details page by clicking the product image.
     *
     * @param productName The name of the product.
     * @return A new instance of ProductDetailsPage if navigation succeeds.
     * @throws NoSuchElementException if product or image element is not found or not displayed.
     */
    public ProductDetailsPage goToProductDetailsPageByImage(String productName) {
        try {
            logger.info("Navigating to the product details page by clicking the image for '{}'.", productName);
            WebElement product = getProductByName(productName);
            if (product == null) {
            	logger.warn("Product '{}' not found for navigation.", productName);
                throw new NoSuchElementException("Product '" + productName + "' not found for navigation.");
            }

            WebElement productImageElement = product.findElement(productImageBy);
            if (!productImageElement.isDisplayed()) {
            	logger.warn("Product image for '{}' is not displayed.", productName);
                throw new NoSuchElementException("Product image for '" + productName + "' is not displayed.");
            }
            productImageElement.click();
            logger.info("Navigated to the details page of product: {}", productName);
            return new ProductDetailsPage(driver);
        } catch (Exception e) {
            logger.error("Error while navigating to product details page by image for '{}'.", productName, e);
            throw e;
        }
    }

    /**
     * Checks if the user is currently on the Product Catalog page.
     *
     * @return True if the user is on the page, false otherwise.
     */
    public boolean isOnProductCatalogPage() {
        try {
            logger.info("Checking if on Product Catalog Page.");
            boolean isDisplayed = sortDropdown.isDisplayed();
            logger.info("Is on Product Catalog Page: {}", isDisplayed);
            return isDisplayed;
        } catch (NoSuchElementException e) {
            logger.warn("Product sort dropdown not found, not on Product Catalog Page.");
            return false;
        } catch (Exception e) {
            logger.error("Unexpected error while checking if on Product Catalog Page.", e);
            return false;
        }
    }

    /**
     * Retrieves all product names.
     *
     * @return List of product names.
     */
    public List<String> getAllProductNames() {
        try {
            return getProductList().stream()
                    .map(product -> product.findElement(productNameBy).getText())
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error while fetching all product names.", e);
            throw e;
        }
    }

    /**
     * Retrieves all product prices.
     *
     * @return List of product prices as doubles.
     */
    public List<Double> getAllProductPrices() {
        try {
            return getProductList().stream()
                    .map(product -> Double.parseDouble(product.findElement(productPriceBy).getText().replaceAll("[^\\d.]", "")))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error while fetching all product prices.", e);
            throw e;
        }
    }
}
