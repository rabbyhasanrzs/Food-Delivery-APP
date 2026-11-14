package com.example.fooddelivery;

import android.content.Intent;

// Model class: ekta food er sob data ekhane thake (database er ek row = ek Food object)
public class Food {
    private int id;
    private String name, subtitle, description, image, category;
    private double rating;
    private int price; // DEMO price in BDT (Taka, integer - never USD)
    private int time;          // minutes
    private boolean favorite;

    public Food(int id, String name, String subtitle, double rating, int price,
                String description, String image, String category, int time, boolean favorite) {
        this.id = id; this.name = name; this.subtitle = subtitle; this.rating = rating;
        this.price = price; this.description = description; this.image = image;
        this.category = category; this.time = time; this.favorite = favorite;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getSubtitle() { return subtitle; }
    public double getRating() { return rating; }
    public int getPrice() { return price; } // BDT Taka
    public String getDescription() { return description; }
    public String getImage() { return image; }
    public String getCategory() { return category; }
    public int getTime() { return time; }
    public boolean isFavorite() { return favorite; }
    public void setFavorite(boolean favorite) { this.favorite = favorite; }

    // Home theke Intent e kore pathano food data theke Food banay
    // (cloud menu er dam/name thik rakhte — DB theke abar ante hoy na)
    public static Food fromIntent(Intent i) {
        if (i == null || !i.hasExtra("food_name")) return null;
        return new Food(
                i.getIntExtra("food_id", -1),
                i.getStringExtra("food_name"),
                i.getStringExtra("food_subtitle"),
                i.getDoubleExtra("food_rating", 0),
                i.getIntExtra("food_price", 0),
                i.getStringExtra("food_desc"),
                i.getStringExtra("food_image"),
                i.getStringExtra("food_category"),
                i.getIntExtra("food_time", 0),
                false);
    }

    public static void putInIntent(Intent i, Food f) {
        i.putExtra("food_id", f.getId());
        i.putExtra("food_name", f.getName());
        i.putExtra("food_subtitle", f.getSubtitle());
        i.putExtra("food_rating", f.getRating());
        i.putExtra("food_price", f.getPrice());
        i.putExtra("food_desc", f.getDescription());
        i.putExtra("food_image", f.getImage());
        i.putExtra("food_category", f.getCategory());
        i.putExtra("food_time", f.getTime());
    }
}
