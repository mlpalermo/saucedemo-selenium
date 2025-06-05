package com.saucedemo.utils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Utility class for capturing screenshots during test execution.
 */
public class ScreenshotUtil {

    private static final Logger logger = LoggerFactory.getLogger(ScreenshotUtil.class);

    /**
     * Captures a screenshot and saves it to the specified location.
     *
     * @param driver         The WebDriver instance.
     * @param screenshotName The name to be used for the screenshot file.
     * @return The file path where the screenshot is saved.
     */
    public static String captureScreenshot(WebDriver driver, String screenshotName) {
        String screenshotPath = ConfigReader.getScreenshotPath();
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = screenshotName + "_" + timestamp + ".png";
        String filePath = screenshotPath + File.separator + fileName;

        logger.info("Attempting to capture screenshot: {}", fileName);

        try {
            // Take screenshot
            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

            // Save the screenshot to the specified path
            FileUtils.copyFile(screenshot, new File(filePath));
            logger.info("Screenshot saved successfully at: {}", filePath);
        } catch (IOException e) {
            logger.error("Failed to save screenshot: {}", e.getMessage(), e);
            throw new RuntimeException("Error while saving screenshot: " + filePath, e);
        } catch (Exception e) {
            logger.error("Unexpected error occurred while capturing screenshot: {}", e.getMessage(), e);
            throw new RuntimeException("Unexpected error while capturing screenshot", e);
        }

        return filePath;
    }
}
