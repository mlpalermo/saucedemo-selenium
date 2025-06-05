package com.saucedemo.utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.WebDriverException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * WebDriverFactory class to manage WebDriver instances for different browsers.
 * It uses ThreadLocal to ensure that each thread has its own instance of WebDriver.
 */
public class WebDriverFactory {

    private static final Logger logger = LoggerFactory.getLogger(WebDriverFactory.class);
    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    /**
     * Initializes and returns the WebDriver instance based on the browser type.
     *
     * @return The WebDriver instance for the current thread.
     */
    public static WebDriver getDriver() {
        if (driver.get() == null) {
            try {
                String browser = ConfigReader.getBrowser();
                boolean isHeadless = ConfigReader.isHeadless();

                logger.info("Initializing WebDriver for browser: {}", browser);

                // Setup WebDriver based on the browser
                switch (browser.toLowerCase()) {
                    case "chrome":
                        driver.set(setupChromeDriver(isHeadless));
                        break;
                    case "firefox":
                        driver.set(setupFirefoxDriver(isHeadless));
                        break;
                    case "edge":
                        driver.set(setupEdgeDriver(isHeadless));
                        break;
                    default:
                        throw new WebDriverException("Unsupported browser: " + browser);
                }

                // Maximize the browser window and set implicit wait
                driver.get().manage().window().maximize();
                driver.get().manage().timeouts().implicitlyWait(Duration.ofSeconds(ConfigReader.getImplicitWait()));

                logger.info("WebDriver initialized successfully for browser: {}", browser);
            } catch (Exception e) {
                logger.error("Error initializing WebDriver for browser.", e);
                throw new RuntimeException("Failed to initialize WebDriver", e);
            }
        }
        return driver.get();
    }

    /**
     * Sets up the Chrome WebDriver.
     *
     * @param isHeadless Whether to run in headless mode.
     * @return The Chrome WebDriver instance.
     */
    private static WebDriver setupChromeDriver(boolean isHeadless) {
		WebDriverManager.chromedriver().setup();
		ChromeOptions options = new ChromeOptions();

		// Disable password manager using experimental options
		Map<String, Object> prefs = new HashMap<>();
		prefs.put("credentials_enable_service", false);
		prefs.put("password_manager_enabled", false);

		Map<String, Object> profile = new HashMap<>();
		profile.put("password_manager_leak_detection", false);
		prefs.put("profile", profile);

		options.setExperimentalOption("prefs", prefs);

        // Add headless mode if enabled
        if (isHeadless) {
            options.addArguments("--headless");
        }

        logger.info("Chrome WebDriver setup completed.");
        return new ChromeDriver(options);
    }

    /**
     * Sets up the Firefox WebDriver.
     *
     * @param isHeadless Whether to run in headless mode.
     * @return The Firefox WebDriver instance.
     */
    private static WebDriver setupFirefoxDriver(boolean isHeadless) {
        WebDriverManager.firefoxdriver().setup();
        FirefoxOptions options = new FirefoxOptions();

        // Add headless mode if enabled
        if (isHeadless) {
            options.addArguments("--headless");
        }

        logger.info("Firefox WebDriver setup completed.");
        return new FirefoxDriver(options);
    }

    /**
     * Sets up the Edge WebDriver.
     *
     * @param isHeadless Whether to run in headless mode.
     * @return The Edge WebDriver instance.
     */
    private static WebDriver setupEdgeDriver(boolean isHeadless) {
        WebDriverManager.edgedriver().setup();
        EdgeOptions options = new EdgeOptions();

        // Add headless mode if enabled
        if (isHeadless) {
            options.addArguments("--headless");
        }

        logger.info("Edge WebDriver setup completed.");
        return new EdgeDriver(options);
    }

    /**
     * Quits the WebDriver instance and removes it from ThreadLocal.
     */
    public static void quitDriver() {
        if (driver.get() != null) {
            try {
                logger.info("Quitting WebDriver instance.");
                driver.get().quit();
            } catch (Exception e) {
                logger.error("Error occurred while quitting WebDriver.", e);
            } finally {
                driver.remove();
                logger.info("WebDriver instance removed from ThreadLocal.");
            }
        }
    }
}
