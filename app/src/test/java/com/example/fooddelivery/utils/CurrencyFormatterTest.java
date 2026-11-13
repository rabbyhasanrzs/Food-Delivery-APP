package com.example.fooddelivery.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

/** Phase 1 - unit tests for BDT formatting (blueprint Section 9). */
public class CurrencyFormatterTest {

    @Test
    public void format_zero_isTakaZero() {
        assertEquals("৳0", CurrencyFormatter.format(0));
    }

    @Test
    public void format_usesTakaSymbolAndGrouping() {
        assertEquals("৳1,000", CurrencyFormatter.format(1000));
        assertEquals("৳18,250", CurrencyFormatter.format(18250));
    }

    @Test
    public void format_neverUsesDollarSymbol() {
        String formatted = CurrencyFormatter.format(500);
        assertEquals("৳500", formatted);
    }

    @Test
    public void format_negativeAmount_throws() {
        assertThrows(IllegalArgumentException.class,
                () -> CurrencyFormatter.format(-1));
    }

    @Test
    public void formatPending_showsNoInventedPrice() {
        assertEquals("৳ --", CurrencyFormatter.formatPending());
    }
}
