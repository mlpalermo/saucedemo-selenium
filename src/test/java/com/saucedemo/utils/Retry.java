package com.saucedemo.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * Retry logic for failed test cases.
 * This class implements the IRetryAnalyzer interface from TestNG.
 */
public class Retry implements IRetryAnalyzer {

    private static final Logger logger = LoggerFactory.getLogger(Retry.class);

    // Maximum number of retry attempts
    private static final int MAX_RETRY_COUNT = 1;

    // Current retry attempt
    private int retryCount = 0;

    /**
     * Determines whether a test should be retried.
     *
     * @param result The result of the test method that just ran.
     * @return True if the test should be retried, false otherwise.
     */
    @Override
    public boolean retry(ITestResult result) {
        if (retryCount < MAX_RETRY_COUNT) {
            retryCount++;
            logger.warn("Retrying test '{}' ({} out of {} attempts).", 
                        result.getName(), retryCount, MAX_RETRY_COUNT);
            return true;
        }
        logger.error("Test '{}' failed after {} attempt.", result.getName(), MAX_RETRY_COUNT);
        return false;
    }
}
