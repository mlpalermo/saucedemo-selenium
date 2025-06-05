package com.saucedemo.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;

/**
 * Utility class to read JSON data from a file and map it to Java objects.
 */
public class JsonReader {

    private static final Logger logger = LoggerFactory.getLogger(JsonReader.class);

    // Fetch the path for the test data JSON file from the ConfigReader class
    private static final String JSON_FILE_PATH = ConfigReader.getTestDataPath();

    /**
     * Reads the JSON file and retrieves data for a specific key.
     *
     * @param key The key to fetch data for.
     * @return List of HashMaps containing the data for the specified key.
     * @throws IOException if there is an error reading the file or parsing the JSON.
     */
    public static List<HashMap<String, String>> getJsonDataByKey(String key) throws IOException {
        try {
            logger.info("Reading JSON data for key: {} from file: {}", key, JSON_FILE_PATH);

            // Read the JSON file as a string
            String jsonContent = FileUtils.readFileToString(new File(JSON_FILE_PATH), StandardCharsets.UTF_8);

            // Create an ObjectMapper instance
            ObjectMapper mapper = new ObjectMapper();

            // Parse the JSON content into a HashMap
            HashMap<String, List<HashMap<String, String>>> data = mapper.readValue(jsonContent,
                    new TypeReference<>() {});

            // Retrieve and return the data for the specified key
            List<HashMap<String, String>> keyData = data.get(key);
            if (keyData == null) {
                logger.warn("No data found for key: {}", key);
            } else {
                logger.info("Successfully retrieved data for key: {}", key);
            }
            return keyData;

        } catch (IOException e) {
            logger.error("Error reading or parsing JSON file for key: {}", key, e);
            throw e;
        }
    }
}
