package com.example.fooddelivery.utils;

import java.util.Locale;

/**
 * Phase 1 - BDT money formatting (blueprint Sections 5 and 9).
 *
 * <p>The only place a taka amount becomes display text: an integer
 * in, a string with the ৳ symbol out. No Activity formats money by
 * hand, so no screen can show the wrong symbol (never USD or $).</p>
 *
 * <p>Example: {@code format(1000)} returns {@code "৳1,000"}.</p>
 */
public final class CurrencyFormatter {

    private CurrencyFormatter() {
        // Utility class - do not instantiate.
    }

    /**
     * Format a whole-taka amount as BDT text.
     *
     * @param amountBdt amount in whole taka; must not be negative
     * @return the amount with the ৳ symbol, e.g. "৳1,250"
     */
    public static String format(int amountBdt) {
        if (amountBdt < 0) {
            throw new IllegalArgumentException("Amount must not be negative.");
        }
        return "৳" + String.format(Locale.US, "%,d", amountBdt);
    }

    /**
     * Text for a price that is still pending configuration.
     * Used instead of inventing a number.
     */
    public static String formatPending() {
        return "৳ --";
    }
}
