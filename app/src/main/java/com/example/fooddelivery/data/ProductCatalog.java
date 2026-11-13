package com.example.fooddelivery.data;

import com.example.fooddelivery.database.DatabaseHelper;
import com.example.fooddelivery.models.Product;

import java.util.ArrayList;
import java.util.List;

/**
 * Minimal demo menu (blueprint Section 5 - ProductCatalog).
 *
 * <p><b>DEMO DATA FOR THE LAB FINAL - NOT FINAL MENU PRICES.</b>
 * These four burgers are the ones visible in the design board, with
 * simple round BDT prices chosen only so the app can run end to end
 * tomorrow. They must be replaced by the confirmed BDT prices before
 * any real use. Prices live in one place - this list - and in the
 * SQLite products table, never in a layout.</p>
 */
public final class ProductCatalog {

    private ProductCatalog() {
        // Utility class - do not instantiate.
    }

    /** Demo menu - 4 burgers from the design board. */
    public static List<Product> getDemoProducts() {
        List<Product> products = new ArrayList<>();
        products.add(new Product(1, "Cheeseburger Wendy's Burger",
                "A classic fast food burger with a juicy beef patty, melted "
                        + "American cheese, crisp lettuce, ripe tomato and crunchy pickles.",
                350, "img_cheeseburger_wendys", 4.9, "26 mins", "Burgers", false));
        products.add(new Product(2, "Hamburger Veggie Burger",
                "A savoury blend of fresh vegetables and herbs, topped with "
                        + "crisp lettuce, juicy tomatoes and tangy pickles on a soft, toasted bun.",
                300, "img_veggie_burger", 4.8, "14 mins", "Burgers", false));
        products.add(new Product(3, "Hamburger Chicken Burger",
                "A crispy chicken burger with fresh lettuce and tomato, "
                        + "served on a soft, toasted bun.",
                320, "img_chicken_burger", 4.6, "20 mins", "Burgers", false));
        products.add(new Product(4, "Hamburger Fried Chicken Burger",
                "A crunchy fried chicken burger with lettuce and sauce, "
                        + "served hot on a soft, toasted bun.",
                340, "img_fried_chicken_burger", 4.5, "18 mins", "Burgers", false));
        return products;
    }

    /** Insert the demo menu once, the first time Home opens. */
    public static void seedIfEmpty(DatabaseHelper db) {
        if (!db.getAllProducts().isEmpty()) {
            return;
        }
        for (Product product : getDemoProducts()) {
            db.insertProduct(product);
        }
    }
}
