package com.saucedemo.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class to read configuration properties from the config.properties file.
 * It prioritizes system properties over values in config.properties.
 */
public class ConfigReader {

    private static final Logger logger = LoggerFactory.getLogger(ConfigReader.class);
    private static final String CONFIG_FILE_PATH = Paths.get(System.getProperty("user.dir"), "src/main/resources/config.properties").toString();
    private static final Properties properties = new Properties();

    // Static block to load properties at class initialization
    static {
        try (FileInputStream inputStream = new FileInputStream(CONFIG_FILE_PATH)) {
            properties.load(inputStream);
            logger.info("Configuration properties loaded successfully from {}", CONFIG_FILE_PATH);
        } catch (IOException e) {
            logger.error("Failed to load configuration properties from {}", CONFIG_FILE_PATH, e);
            throw new RuntimeException("Could not load configuration properties file", e);
        }
    }

    /**
	 * Retrieves a property value from system properties or the properties file.
	 * If the property is not found in either, it logs a warning and returns null.
	 *
	 * @param key The property key.
	 * @return The property value, or null if not found.
	 */
    private static String getProperty(String key) {
        //Check for system property first
        String systemPropertyValue = System.getProperty(key);
        if (systemPropertyValue != null && !systemPropertyValue.trim().isEmpty()) {
            logger.debug("Using system property '{}' with value: '{}'", key, systemPropertyValue);
            return systemPropertyValue.trim();
        }

        //Fallback to properties file
        String filePropertyValue = properties.getProperty(key);
        if (filePropertyValue == null) {
            logger.warn("Property '{}' not found in system properties or configuration file.", key);
        } else {
            logger.debug("Using value from config.properties for '{}': '{}'", key, filePropertyValue);
        }
        return filePropertyValue;
    }

    /**
     * Retrieves a property value as an integer.
     *
     * @param key The property key.
     * @return The property value as an integer.
     * @throws NumberFormatException if the value cannot be parsed as an integer.
     */
    private static int getIntProperty(String key) {
        String value = getProperty(key);
        if (value == null) {
            logger.error("Property '{}' not found for integer conversion.", key);
            throw new IllegalArgumentException("Property '" + key + "' not found.");
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            logger.error("Invalid integer value '{}' for property '{}'", value, key, e);
            throw e;
        }
    }

    /**
     * Retrieves a property value as a boolean.
     *
     * @param key The property key.
     * @return The property value as a boolean.
     */
    private static boolean getBooleanProperty(String key) {
        String value = getProperty(key);
        if (value == null) {
            logger.warn("Property '{}' not found for boolean conversion, defaulting to false.", key);
            return false;
        }
        return Boolean.parseBoolean(value);
    }

    /**
     * Retrieves a property value as a double.
     *
     * @param key The property key.
     * @return The property value as a double.
     * @throws NumberFormatException if the value cannot be parsed as a double.
     */
    private static double getDoubleProperty(String key) {
        String value = getProperty(key);
        if (value == null) {
            logger.error("Property '{}' not found for double conversion.", key);
            throw new IllegalArgumentException("Property '" + key + "' not found.");
        }
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            logger.error("Invalid double value '{}' for property '{}'", value, key, e);
            throw e;
        }
    }

    // Public methods to retrieve specific configuration properties

    public static String getBrowser() {
        return getProperty("browser");
    }

    public static String getBaseUrl() {
        return getProperty("baseUrl");
    }

    public static int getImplicitWait() {
        return getIntProperty("implicitWait");
    }

    public static int getExplicitWait() {
        return getIntProperty("explicitWait");
    }

    public static boolean isHeadless() {
        return getBooleanProperty("headless");
    }

    public static String getTestDataPath() {
        return Paths.get(System.getProperty("user.dir"), getProperty("testDataPath")).toString();
    }

    public static String getScreenshotPath() {
        return Paths.get(System.getProperty("user.dir"), getProperty("screenshotPath")).toString();
    }

    public static String getReportPath() {
        return Paths.get(System.getProperty("user.dir"), getProperty("reportPath")).toString();
    }

    public static String getLogPath() {
        return Paths.get(System.getProperty("user.dir"), getProperty("logPath")).toString();
    }

    public static String getLogLevel() {
        return getProperty("logLevel");
    }

    public static String getAboutPageUrl() {
        return getProperty("aboutPageUrl");
    }

    public static String getTwitterUrl() {
        return getProperty("twitterUrl");
    }

    public static String getFacebookUrl() {
        return getProperty("facebookUrl");
    }

    public static String getLinkedInUrl() {
        return getProperty("linkedinUrl");
    }

    public static double getTaxRate() {
        return getDoubleProperty("taxRate");
    }
}