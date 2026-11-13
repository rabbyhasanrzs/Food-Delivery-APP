package com.example.fooddelivery.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Unit tests for PricingConfig (blueprint Section 9).
 * Demo values for the lab final: 5% tax, ৳30 delivery fee.
 * These are demo values only - see PricingConfig.
 */
public class PricingConfigTest {

    @Test
    public void pricing_isConfiguredWithDemoValues() {
        assertTrue(PricingConfig.isConfigured());
        assertEquals(5, PricingConfig.TAX_RATE_PERCENT);
        assertEquals(30, PricingConfig.DELIVERY_FEE_BDT);
    }

    @Test
    public void calculateTax_isFivePercentOfSubtotal() {
        assertEquals(50, PricingConfig.calculateTax(1000));
        assertEquals(0, PricingConfig.calculateTax(0));
    }

    @Test
    public void calculateTotal_addsTaxAndDeliveryFee() {
        // 1000 + 5% tax (50) + 30 delivery = 1080.
        assertEquals(1080, PricingConfig.calculateTotal(1000));
    }

    @Test
    public void calculateSubtotal_sumsLineTotals() {
        assertEquals(1500, PricingConfig.calculateSubtotal(new int[]{500, 1000}));
        assertEquals(0, PricingConfig.calculateSubtotal(new int[]{}));
    }

    @Test
    public void calculateTax_negativeSubtotal_throws() {
        assertThrows(IllegalArgumentException.class,
                () -> PricingConfig.calculateTax(-100));
    }
}
