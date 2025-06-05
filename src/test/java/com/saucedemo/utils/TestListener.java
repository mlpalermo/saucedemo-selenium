package com.saucedemo.utils;

import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.saucedemo.basetest.BaseTest;

/**
 * TestListener class implements ITestListener to handle test events and logging.
 * It captures test start, success, failure, and skip events, and logs relevant information.
 */
public class TestListener implements ITestListener {

    private static final Logger logger = LoggerFactory.getLogger(TestListener.class);

    /**
     * Called when a test starts.
     */
    @Override
    public void onTestStart(ITestResult result) {
        logger.info("Test started: {}", result.getName());

        try {
            ExtentReportManager.startTest(result.getName(), result.getMethod().getDescription());

            // Dynamically fetch browser name
            BaseTest testInstance = (BaseTest) result.getInstance();
            WebDriver driver = testInstance.getDriver();
            if (driver != null) {
                Capabilities caps = ((RemoteWebDriver) driver).getCapabilities();
                String browserName = caps.getBrowserName();
                ExtentReportManager.addBrowserInfo(browserName);
                logger.info("Browser detected: {}", browserName);
            }
        } catch (Exception e) {
            logger.error("Error while fetching browser information: {}", e.getMessage(), e);
            ExtentReportManager.logException(e);
        }

        // Add category based on test name
        String category = result.getTestContext().getName();
        ExtentReportManager.addCategory(category);
        logger.info("Test category: {}", category);
    }

    /**
     * Called when a test is skipped.
     */
    @Override
    public void onTestSkipped(ITestResult result) {
        logger.warn("Test skipped: {}", result.getName());
        ExtentReportManager.logSkip(result.getName() + " skipped");
    }

    /**
     * Called when a test passes.
     */
    @Override
    public void onTestSuccess(ITestResult result) {
        logger.info("Test passed: {}", result.getName());
        ExtentReportManager.logPass(result.getName() + " passed");
    }

    /**
     * Called when a test fails.
     */
    @Override
    public void onTestFailure(ITestResult result) {
        logger.error("Test failed: {}", result.getName());
        ExtentReportManager.logFailure(result.getName() + " failed");

        try {
            BaseTest testInstance = (BaseTest) result.getInstance();
            WebDriver driver = testInstance.getDriver();
            if (driver != null) {
                ExtentReportManager.logScreenshot(driver, result.getName() + "_Failure");
                logger.info("Screenshot captured for failed test: {}", result.getName());
            }
        } catch (Exception e) {
            logger.error("Error while capturing screenshot: {}", e.getMessage(), e);
            ExtentReportManager.logException(e);
        }

        Throwable throwable = result.getThrowable();
        if (throwable != null) {
            logger.error("Test failure exception: {}", throwable.getMessage(), throwable);
            ExtentReportManager.logException(throwable);
        }
    }

    /**
     * Called when the test starts.
     */
    @Override
    public void onStart(ITestContext context) {
        logger.info("Test started: {}", context.getName());
        List<String> directories = Arrays.asList(
                ConfigReader.getLogPath(),
                ConfigReader.getReportPath(),
                ConfigReader.getScreenshotPath()
        );

        for (String directory : directories) {
            try {
                DirectoryUtil.createDirectory(directory);
                logger.info("Directory created: {}", directory);
            } catch (Exception e) {
                logger.error("Error creating directory: {}", directory, e);
            }
        }
    }

    /**
     * Called when the test finishes.
     */
    @Override
    public void onFinish(ITestContext context) {
        logger.info("Test finished: {}", context.getName());
        try {
            ExtentReportManager.closeReporter();
            logger.info("Extent report finalized.");
        } catch (Exception e) {
            logger.error("Error finalizing extent report: {}", e.getMessage(), e);
        }
    }
}
