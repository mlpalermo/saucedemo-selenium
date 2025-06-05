package com.saucedemo.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.openqa.selenium.WebDriver;

/**
 * ExtentReportManager is a utility class for managing ExtentReports in a thread-safe manner.
 * It provides methods to initialize the report, log messages, attach screenshots, and close the report.
 */
public class ExtentReportManager {

    private static ExtentReports extent;
    private static ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();
    private static ExtentSparkReporter sparkReporter;
    private static final Logger logger = LoggerFactory.getLogger(ExtentReportManager.class);

    /**
     * Initializes and returns the ExtentReports instance (singleton with thread safety).
     */
    public static ExtentReports getExtentReports() {
        if (extent == null) {
            synchronized (ExtentReportManager.class) {
                if (extent == null) {
                    try {
                        logger.info("Initializing ExtentReports...");
                        String reportFolderPath = ConfigReader.getReportPath();
                        String reportFileName = reportFolderPath + "/ExtentReport_" + getCurrentDateTime() + ".html";
                        sparkReporter = new ExtentSparkReporter(reportFileName);
                        sparkReporter.config().setReportName("SauceDemo Automation Test Report");
                        sparkReporter.config().setDocumentTitle("Automation Test Results");
                        extent = new ExtentReports();
                        extent.attachReporter(sparkReporter);
                    } catch (Exception e) {
                        logger.error("Failed to initialize ExtentReports: {}", e.getMessage(), e);
                        throw new RuntimeException("Failed to initialize ExtentReports", e);
                    }
                }
            }
        }
        return extent;
    }

    /**
     * Starts a new test in the ExtentReports instance.
     */
    public static void startTest(String testName, String description) {
        try {
            String uniqueId = "_" + Thread.currentThread().threadId();
            String uniqueTestName = testName + uniqueId;

            ExtentTest test = getExtentReports().createTest(uniqueTestName, description);
            extentTest.set(test); // Set the ExtentTest instance for the current thread
            logger.info("Started test: {} on thread: {}", uniqueTestName, Thread.currentThread().threadId());
        } catch (Exception e) {
            logger.error("Failed to start test: {}", e.getMessage(), e);
        }
    }

    /**
     * Logs an informational message to the report and console.
     */
    public static void logInfo(String message) {
        if (extentTest.get() != null) {
            extentTest.get().log(Status.INFO, message);
        }
        logger.info(message);
    }

    /**
     * Logs a failure message to the report and console.
     */
    public static void logFailure(String message) {
        if (extentTest.get() != null) {
            extentTest.get().log(Status.FAIL, message);
        }
        logger.error(message);
    }

    /**
     * Logs a success message to the report and console.
     */
    public static void logPass(String message) {
        if (extentTest.get() != null) {
            extentTest.get().log(Status.PASS, message);
        }
        logger.info(message);
    }
    
    /**
	 * Logs a warning message to the report and console.
	 */
    public static void logSkip(String message) {
        if (extentTest.get() != null) {
            extentTest.get().log(Status.SKIP, message);
        }
        logger.warn(message);
    }

    /**
     * Logs an exception to the report and console.
     */
    public static void logException(Throwable e) {
        if (extentTest.get() != null) {
            extentTest.get().fail(e);
        }
        logger.error("Exception occurred: ", e);
    }

    /**
     * Captures and attaches a screenshot to the report.
     */
    public static void logScreenshot(WebDriver driver, String screenshotName) {
        if (extentTest.get() != null && driver != null) {
            try {
                String screenshotPath = ScreenshotUtil.captureScreenshot(driver, screenshotName);
                extentTest.get().addScreenCaptureFromPath(screenshotPath);
            } catch (Exception e) {
                logger.error("Failed to attach screenshot: {}", e.getMessage(), e);
            }
        }
    }

    /**
     * Adds a category to the current test.
     */
    public static void addCategory(String category) {
        if (extentTest.get() != null) {
            extentTest.get().assignCategory(category);
        }
    }

    /**
     * Adds browser information to the current test.
     */
    public static void addBrowserInfo(String browser) {
        if (extentTest.get() != null) {
            extentTest.get().assignDevice(browser);
        }
    }

    /**
     * Closes the ExtentReports instance and flushes the report.
     */
    public static synchronized void closeReporter() {
        try {
            if (extent != null) {
                extent.flush();
                extent = null; // Reset extent to ensure proper reinitialization
            }
            extentTest.remove();
        } catch (Exception e) {
            logger.error("Failed to close ExtentReports: {}", e.getMessage(), e);
        }
    }

    /**
     * Returns the current date and time in a specific format.
     */
    private static String getCurrentDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        return sdf.format(new Date());
    }
}
