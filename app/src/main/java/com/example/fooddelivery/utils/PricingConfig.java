package com.example.fooddelivery.utils;

/**
 * Phase 1 - central pricing configuration (blueprint Sections 5 and 9).
 *
 * <p>This is the single central place for the tax rate and the
 * delivery fee. Product, topping and side prices do <b>not</b> live
 * here; they are stored per item in SQLite (see DatabaseHelper).</p>
 *
 * <p><b>DEMO VALUES FOR THE LAB FINAL - NOT FINAL CONFIRMED PRICES.</b>
 * Tax is 5% and the delivery fee is ৳30, chosen only so the app can
 * run end to end tomorrow. They must be replaced by the confirmed
 * BDT values before any real use. They live only here - one place -
 * and the demo menu prices live only in ProductCatalog.</p>
 *
 * <p>Money is always whole taka, stored and calculated as integers
 * - never as float or double (blueprint Section 9).</p>
 */
public final class PricingConfig {

    private PricingConfig() {
        // Utility class - do not instantiate.
    }

    /** Sentinel value meaning "not configured yet". */
    public static final int PENDING = -1;

    /**
     * Tax rate in percent.
     * DEMO VALUE FOR THE LAB FINAL (5%) - replace with the confirmed rate.
     */
    public static final int TAX_RATE_PERCENT = 5;

    /**
     * Delivery fee in whole taka (BDT).
     * DEMO VALUE FOR THE LAB FINAL (৳30) - replace with the confirmed fee.
     */
    public static final int DELIVERY_FEE_BDT = 30;

    /** True only when every pending value has been confirmed. */
    public static boolean isConfigured() {
        return TAX_RATE_PERCENT >= 0 && DELIVERY_FEE_BDT >= 0;
    }

    /**
     * tax = subtotal x tax rate.
     *
     * @throws IllegalStateException if pricing is still pending
     */
    public static int calculateTax(int subtotalBdt) {
        if (!isConfigured()) {
            throw new IllegalStateException(
                    "Pricing is not configured yet: tax rate is PENDING "
                            + "(see blueprint Section 9).");
        }
        if (subtotalBdt < 0) {
            throw new IllegalArgumentException("Subtotal must not be negative.");
        }
        return (int) Math.round(subtotalBdt * TAX_RATE_PERCENT / 100.0);
    }

    /**
     * total = subtotal + tax + delivery fee.
     *
     * @throws IllegalStateException if pricing is still pending
     */
    public static int calculateTotal(int subtotalBdt) {
        int taxBdt = calculateTax(subtotalBdt);
        return subtotalBdt + taxBdt + DELIVERY_FEE_BDT;
    }

    /**
     * subtotal = sum of all line totals.
     * Used by Product Details and again by Checkout, so the total is
     * always recalculated instead of trusting a passed-in total.
     */
    public static int calculateSubtotal(int[] lineTotalsBdt) {
        int subtotal = 0;
        for (int lineTotal : lineTotalsBdt) {
            if (lineTotal < 0) {
                throw new IllegalArgumentException("Line total must not be negative.");
            }
            subtotal += lineTotal;
        }
        return subtotal;
    }
}
