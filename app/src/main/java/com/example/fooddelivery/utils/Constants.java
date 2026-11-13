package com.example.fooddelivery.utils;

/**
 * Phase 1 - app-wide constants (blueprint Section 5).
 *
 * <p>Only values that are already decided live here. No prices, tax
 * rates or delivery fees are in this file - those belong to
 * {@link PricingConfig} and are marked PENDING there.</p>
 */
public final class Constants {

    private Constants() {
        // Utility class - do not instantiate.
    }

    /** SQLite database file name (blueprint Section 7). */
    public static final String DATABASE_NAME = "food_delivery.db";

    /** SQLite database version. Increase this only when the schema changes. */
    public static final int DATABASE_VERSION = 1;

    /** SharedPreferences file used by SessionManager. */
    public static final String PREFS_NAME = "food_delivery_session";

    /** SharedPreferences keys for the logged-in user. */
    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_USER_NAME = "user_name";
    public static final String KEY_USER_EMAIL = "user_email";

    /** How long the Splash screen is shown (blueprint Section 4: about 2 seconds). */
    public static final long SPLASH_DELAY_MS = 2000L;

    /** Minimum password length for Registration (blueprint Section 4). */
    public static final int MIN_PASSWORD_LENGTH = 6;

    /** Order status set when an order is first saved (blueprint Section 7). */
    public static final String ORDER_STATUS_PLACED = "Placed";

    /** Intent extra used by SplashActivity when it opens MainActivity. */
    public static final String EXTRA_WAS_LOGGED_IN = "was_logged_in";

    /** Intent extras for the order flow: Home -> Details -> Checkout -> Success. */
    public static final String EXTRA_PRODUCT_ID = "product_id";
    public static final String EXTRA_QUANTITY = "quantity";
    public static final String EXTRA_SPICY_LEVEL = "spicy_level";
    public static final String EXTRA_ORDER_ID = "order_id";
    public static final String EXTRA_TOTAL_BDT = "total_bdt";
}
