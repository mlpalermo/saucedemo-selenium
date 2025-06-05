package com.saucedemo.utils;

import java.io.File;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class for directory-related operations.
 */
public class DirectoryUtil {

    private static final Logger logger = LoggerFactory.getLogger(DirectoryUtil.class);

    /**
     * Creates a directory at the specified path if it does not already exist.
     *
     * @param path The path of the directory to create.
     */
    public static void createDirectory(String path) {
        File directory = new File(path);

        try {
        	// Check if the directory already exists
            if (!directory.exists()) {
            	// Attempt to create the directory
                boolean dirCreated = directory.mkdirs();
                if (dirCreated) {
                    logger.info("Directory successfully created at: {}", path);
                } else {
                    logger.error("Failed to create directory at: {}", path);
                }
            } else {
                logger.info("Directory already exists at: {}", path);
            }
        } catch (Exception e) {
            logger.error("An unexpected error occurred while creating directory at: {}", path, e);
            throw e;
        }
    }
}
