package com.saucedemo.basepage;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.saucedemo.utils.ConfigReader;
import com.saucedemo.pageobjects.CartPage;

  /**
   * BasePage class that provides common functionalities for all page objects.
   */
public class BasePage {

    protected WebDriver driver;
    private Duration explicitWaitDuration = Duration.ofSeconds(ConfigReader.getExplicitWait());
    private static final Logger logger = LoggerFactory.getLogger(BasePage.class);

    /**
	 * Constructor for BasePage.
	 *
	 * @param driver WebDriver instance.
	 */
    public BasePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    // Web elements
    @FindBy(id = "react-burger-menu-btn")
    private WebElement burgerButton;

    @FindBy(xpath = "//a[@class='bm-item menu-item']")
    private List<WebElement> burgerMenuItems;

    @FindBy(xpath = "//footer//a")
    private List<WebElement> socialMediaLinks;

    @FindBy(css = ".shopping_cart_link")
    private WebElement cartHeader;

    @FindBy(css = ".shopping_cart_badge")
    private WebElement cartBadge;
    
    By cartHeaderBy = By.cssSelector(".shopping_cart_badge");

    /**
     * Clicks a hamburger menu item by its name.
     * Opens the menu if it is not already open.
     *
     * @param itemName The name of the menu item to click.
     */
    public void clickHamburgerMenuItem(String itemName) {
        try {
            if (burgerMenuItems.isEmpty() || !burgerMenuItems.get(0).isDisplayed()) {
                logger.info("Burger menu is not open. Reopening it.");
                waitForWebElementToAppear(burgerButton);
                burgerButton.click();
                waitForVisibilityOfAllElements(burgerMenuItems);
            } else {
                logger.info("Burger menu is already open.");
            }

            logger.info("Available menu items:");
            for (WebElement menuItem : burgerMenuItems) {
                logger.info("Menu item text: '{}'", menuItem.getText().trim());
            }

            for (WebElement menuItem : burgerMenuItems) {
                if (menuItem.getText().trim().equalsIgnoreCase(itemName)) {
                    if (itemName.equalsIgnoreCase("About")) {
                        new Actions(driver).keyDown(Keys.CONTROL).click(menuItem).keyUp(Keys.CONTROL).perform();
                    } else {
                        menuItem.click();
                    }
                    return;
                }
            }

            throw new RuntimeException("Menu item not found: " + itemName);
        } catch (Exception e) {
            logger.error("Error while clicking hamburger menu item: {}", itemName, e);
            throw e;
        }
    }

    /**
     * Resets the application state by clicking the "Reset App State" menu item.
     */
    public void resetAppState() {
        try {
            logger.info("Resetting app state.");
            clickHamburgerMenuItem("Reset App State");
            waitForElementToDisappear(cartBadge);
            logger.info("App state reset successfully. Cart is now empty.");
        } catch (Exception e) {
            logger.error("Error while resetting app state.", e);
            throw e;
        }
    }

    /**
     * Clicks a social media icon based on the platform name.
     *
     * @param platform The name of the social media platform (e.g., "facebook").
     */
    public void clickSocialMediaIcon(String platform) {
        try {
            for (WebElement socialLink : socialMediaLinks) {
                String href = socialLink.getDomAttribute("href");
                logger.info("Checking social media link: {}", href);
                if (href != null && href.contains(platform.toLowerCase())) {
                	socialLink.click();
                    return;
                }
            }
            throw new RuntimeException("Social media link not found: " + platform);
        } catch (Exception e) {
            logger.error("Error while clicking social media icon: {}", platform, e);
            throw e;
        }
    }

    /**
     * Switches to a newly opened browser tab.
     */
    public void switchToNewTab() {
        try {
            String currentTab = driver.getWindowHandle();
            for (String handle : driver.getWindowHandles()) {
                if (!handle.equals(currentTab)) {
                    driver.switchTo().window(handle);
                    logger.info("Switched to new tab. Waiting for it to load.");
                    new WebDriverWait(driver, explicitWaitDuration)
                        .until(driver -> !driver.getCurrentUrl().equals("about:blank"));
                    logger.info("New tab loaded with URL: {}", driver.getCurrentUrl());
                    return;
                }
            }
        } catch (Exception e) {
            logger.error("Error while switching to new tab.", e);
            throw e;
        }
    }

    /**
     * Closes the current browser tab and switches back to the original tab.
     */
    public void closeCurrentTabAndSwitchBack() {
        try {
            String originalTab = driver.getWindowHandles().iterator().next();
            driver.close();
            driver.switchTo().window(originalTab);
        } catch (Exception e) {
            logger.error("Error while closing current tab and switching back.", e);
            throw e;
        }
    }

    /**
     * Retrieves the current URL of the browser.
     *
     * @return The current URL.
     */
    public String getCurrentUrl() {
        try {
            String url = driver.getCurrentUrl();
            logger.info("Current URL retrieved: {}", url);
            return url;
        } catch (Exception e) {
            logger.error("Error while retrieving current URL.", e);
            throw e;
        }
    }

    /**
     * Waits for an element to appear on the page.
     *
     * @param locator The locator of the element.
     */
    public void waitForElementToAppear(By locator) {
        try {
            logger.info("Waiting for element to appear: {}", locator.toString());
            new WebDriverWait(driver, explicitWaitDuration)
                .until(ExpectedConditions.visibilityOfElementLocated(locator));
        } catch (Exception e) {
            logger.error("Error while waiting for element to appear: {}", locator, e);
            throw e;
        }
    }

    /**
     * Waits for a web element to appear on the page.
     *
     * @param webElement The web element.
     */
    public void waitForWebElementToAppear(WebElement webElement) {
        try {
            logger.info("Waiting for element to appear: {}", webElement.toString());
            new WebDriverWait(driver, explicitWaitDuration)
                .until(ExpectedConditions.visibilityOf(webElement));
        } catch (Exception e) {
            logger.error("Error while waiting for web element to appear.", e);
            throw e;
        }
    }

    /**
     * Waits for all elements in a list to become visible.
     *
     * @param webElements The list of web elements.
     */
    public void waitForVisibilityOfAllElements(List<WebElement> webElements) {
        try {
            logger.info("Waiting for web elements to appear: {}", webElements.toString());
            new WebDriverWait(driver, explicitWaitDuration)
                .until(ExpectedConditions.visibilityOfAllElements(webElements));
        } catch (Exception e) {
            logger.error("Error while waiting for visibility of all elements.", e);
            throw e;
        }
    }
    
    /**
     * Waits for a web element to disappear from the page.
     *
     * @param webElement The web element to wait for.
     */
    public void waitForElementToDisappear(WebElement webElement) {
        try {
            logger.info("Waiting for element to disappear: {}", webElement.toString());
            new WebDriverWait(driver, explicitWaitDuration)
                .until(ExpectedConditions.invisibilityOf(webElement));
        } catch (Exception e) {
            logger.error("Error while waiting for element to disappear.", e);
            throw e;
        }
    }

    /**
     * Navigates to the cart page.
     *
     * @return The CartPage object.
     */
    public CartPage goToCartPage() {
        try {
            cartHeader.click();
            return new CartPage(driver);
        } catch (Exception e) {
            logger.error("Error while navigating to cart page.", e);
            throw e;
        }
    }

    /**
     * Navigates to the login page.
     */
    public void goToLoginPage() {
        try {
            driver.get(ConfigReader.getBaseUrl());
        } catch (Exception e) {
            logger.error("Error while navigating to login page.", e);
            throw e;
        }
    }

    /**
     * Logs out from the application.
     */
    public void logout() {
        try {
            logger.info("Logging out from the application.");
            clickHamburgerMenuItem("Logout");
        } catch (Exception e) {
            logger.error("Error while logging out.", e);
            throw e;
        }
    }

    /**
     * Retrieves the number of items in the cart.
     *
     * @return The cart item count.
     */
    public int getCartItemCount() {
        try {
            if (driver.findElements(cartHeaderBy).size() > 0 && cartBadge.isDisplayed()) {
                int count = Integer.parseInt(cartBadge.getText());
                logger.info("Cart item count: {}", count);
                return count;
            } else {
                logger.info("Cart badge not visible, returning count as 0.");
                return 0;
            }
        } catch (Exception e) {
            logger.warn("Error while retrieving cart item count, returning count as 0.", e);
            return 0;
        }
    }
}
