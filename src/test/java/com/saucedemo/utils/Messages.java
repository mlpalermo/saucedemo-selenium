package com.saucedemo.utils;

/**
 * Utility class to store application-wide constant messages.
 * This class provides a centralized location for all user-facing messages,
 * making it easier to maintain and update.
 */
public class Messages {

    // Login-related messages
    public static final String USERNAME_REQUIRED = "Epic sadface: Username is required";
    public static final String PASSWORD_REQUIRED = "Epic sadface: Password is required";
    public static final String INVALID_CREDENTIALS = "Epic sadface: Username and password do not match any user in this service";
    public static final String LOCKED_OUT_USER = "Epic sadface: Sorry, this user has been locked out.";

    // Checkout-related messages
    public static final String FIRST_NAME_REQUIRED = "Error: First Name is required";
    public static final String LAST_NAME_REQUIRED = "Error: Last Name is required";
    public static final String POSTAL_CODE_REQUIRED = "Error: Postal Code is required";

    // Order confirmation messages
    public static final String THANK_YOU_FOR_YOUR_ORDER = "Thank you for your order!";
    public static final String ORDER_DISPATCHED_MESSAGE = "Your order has been dispatched, and will arrive just as fast as the pony can get there!";
}
