package com.saucedemo.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.DataProvider;

/**
 * Utility class for providing test data using TestNG DataProviders.
 */
public class DataProviderUtil {

    private static final Logger logger = LoggerFactory.getLogger(DataProviderUtil.class);
    private static final Map<String, List<HashMap<String, String>>> cachedData = new ConcurrentHashMap<>();

    /**
     * Retrieves and caches data by key.
     *
     * @param key The key to retrieve data for.
     * @return The cached data as a list of HashMaps.
     * @throws IOException If an error occurs while reading the data.
     */
    private static List<HashMap<String, String>> getCachedData(String key) throws IOException {
        try {
            if (!cachedData.containsKey(key)) {
                logger.info("Loading data for key: {}", key);
                cachedData.put(key, JsonReader.getJsonDataByKey(key));
            }
            return cachedData.get(key);
        } catch (IOException e) {
            logger.error("Error loading data for key: {}", key, e);
            throw e;
        }
    }

    /**
     * Provides valid users data.
     */
    @DataProvider(name = "validUsers")
    public static Object[][] validUsers() throws IOException {
        return convertToDataProvider(getCachedData("validUsers"));
    }

    /**
     * Provides invalid users data.
     */
    @DataProvider(name = "invalidUsers")
    public static Object[][] invalidUsers() throws IOException {
        return convertToDataProvider(getCachedData("invalidUsers"));
    }

    /**
     * Provides products data.
     */
    @DataProvider(name = "products")
    public static Object[][] products() throws IOException {
        return convertToDataProvider(getCachedData("products"));
    }

    /**
     * Provides valid checkout details data.
     */
    @DataProvider(name = "validCheckoutDetails")
    public static Object[][] validCheckoutDetails() throws IOException {
        return convertToDataProvider(getCachedData("validCheckoutDetails"));
    }

    /**
     * Provides invalid checkout details data.
     */
    @DataProvider(name = "invalidCheckoutDetails")
    public static Object[][] invalidCheckoutDetails() throws IOException {
        return convertToDataProvider(getCachedData("invalidCheckoutDetails"));
    }

    /**
     * Provides valid users with products data.
     */
    @DataProvider(name = "validUsersWithProducts")
    public static Object[][] validUsersWithProducts() throws IOException {
        List<HashMap<String, String>> users = getCachedData("validUsers");
        List<HashMap<String, String>> products = getCachedData("products");

        return users.stream()
                .map(user -> new Object[]{user, products})
                .toArray(Object[][]::new);
    }

    /**
     * Provides valid users with products and valid checkout details data.
     */
    @DataProvider(name = "validUsersWithProductsAndValidCheckoutDetails")
    public static Object[][] validUsersWithProductsAndValidCheckoutDetails() throws IOException {
        List<HashMap<String, String>> users = getCachedData("validUsers");
        List<HashMap<String, String>> products = getCachedData("products");
        List<HashMap<String, String>> validCheckoutDetails = getCachedData("validCheckoutDetails");

        return users.stream()
                .flatMap(user -> validCheckoutDetails.stream()
                        .map(checkoutDetail -> new Object[]{user, products, checkoutDetail}))
                .toArray(Object[][]::new);
    }

    /**
     * Provides valid users with products and invalid checkout details data.
     */
    @DataProvider(name = "validUsersWithProductsAndInvalidCheckoutDetails")
    public static Object[][] validUsersWithProductsAndInvalidCheckoutDetails() throws IOException {
        List<HashMap<String, String>> users = getCachedData("validUsers");
        List<HashMap<String, String>> products = getCachedData("products");
        List<HashMap<String, String>> invalidCheckoutDetails = getCachedData("invalidCheckoutDetails");

        return users.stream()
                .flatMap(user -> invalidCheckoutDetails.stream()
                        .map(checkoutDetail -> new Object[]{user, products, checkoutDetail}))
                .toArray(Object[][]::new);
    }

    /**
     * Converts a list of data to a TestNG DataProvider format.
     *
     * @param dataList The list of data to convert.
     * @return The data in DataProvider format.
     */
    private static Object[][] convertToDataProvider(List<HashMap<String, String>> dataList) {
        return dataList.stream()
                .map(data -> new Object[]{data})
                .toArray(Object[][]::new);
    }
}
