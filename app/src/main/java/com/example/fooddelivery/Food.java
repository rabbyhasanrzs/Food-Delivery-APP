package com.example.fooddelivery;

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
}
