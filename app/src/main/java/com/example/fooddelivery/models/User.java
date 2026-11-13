package com.example.fooddelivery.models;

/**
 * Minimal user model - plain Java, no Android code.
 * Local account only. This is a university demo store, not
 * production-grade security - see DatabaseHelper.
 */
public class User {

    private final long id;
    private final String fullName;
    private final String email;
    private final String password;
    private final String phone;
    private final String deliveryAddress;

    public User(long id, String fullName, String email, String password,
                String phone, String deliveryAddress) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.deliveryAddress = deliveryAddress;
    }

    public long getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getPhone() {
        return phone;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }
}
