package com.example.fooddelivery.models;

/**
 * Minimal product model - plain Java, no Android code.
 * Price is whole taka (BDT), as an integer - never float/double.
 */
public class Product {

    private final int id;
    private final String name;
    private final String description;
    private final int priceBdt;
    private final String imageName;
    private final double rating;
    private final String deliveryTime;
    private final String category;
    private boolean isFavourite;

    public Product(int id, String name, String description, int priceBdt,
                   String imageName, double rating, String deliveryTime,
                   String category, boolean isFavourite) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.priceBdt = priceBdt;
        this.imageName = imageName;
        this.rating = rating;
        this.deliveryTime = deliveryTime;
        this.category = category;
        this.isFavourite = isFavourite;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getPriceBdt() {
        return priceBdt;
    }

    public String getImageName() {
        return imageName;
    }

    public double getRating() {
        return rating;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public String getCategory() {
        return category;
    }

    public boolean isFavourite() {
        return isFavourite;
    }

    public void setFavourite(boolean favourite) {
        isFavourite = favourite;
    }
}
