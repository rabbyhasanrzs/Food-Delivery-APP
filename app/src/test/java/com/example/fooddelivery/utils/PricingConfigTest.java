package com.example.fooddelivery.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

/**
 * Phase 1 - unit tests for PricingConfig (blueprint Section 9).
 * Prices are PENDING, so calculations must refuse to run rather
 * than use an invented tax rate or delivery fee.
 */
public class PricingConfigTest {

    @Test
    public void pricing_isNotConfiguredYet() {
        assertFalse(PricingConfig.isConfigured());
        assertEquals(PricingConfig.PENDING, PricingConfig.TAX_RATE_PERCENT);
        assertEquals(PricingConfig.PENDING, PricingConfig.DELIVERY_FEE_BDT);
    }

    @Test
    public void calculateTax_whilePending_throws() {
        assertThrows(IllegalStateException.class,
                () -> PricingConfig.calculateTax(1000));
    }

    @Test
    public void calculateTotal_whilePending_throws() {
        assertThrows(IllegalStateException.class,
                () -> PricingConfig.calculateTotal(1000));
    }

    @Test
    public void calculateSubtotal_sumsLineTotals() {
        assertEquals(1500, PricingConfig.calculateSubtotal(new int[]{500, 1000}));
        assertEquals(0, PricingConfig.calculateSubtotal(new int[]{}));
    }
}
