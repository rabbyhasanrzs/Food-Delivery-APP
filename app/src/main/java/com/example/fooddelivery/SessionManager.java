package com.example.fooddelivery;

import android.content.Context;
import android.content.SharedPreferences;

// Login thaka user ke mone rakhe (SharedPreferences = choto key-value storage)
public class SessionManager {
    private final SharedPreferences prefs;

    public SessionManager(Context context) {
        prefs = context.getSharedPreferences("rabby_session", Context.MODE_PRIVATE);
    }

    public void login(String name, String email) {
        prefs.edit().putBoolean("logged_in", true)
                .putString("name", name).putString("email", email).apply();
    }

    public boolean isLoggedIn() { return prefs.getBoolean("logged_in", false); }
    public String getEmail() { return prefs.getString("email", ""); }
    public String getName() { return prefs.getString("name", ""); }
    public void logout() { prefs.edit().clear().apply(); }
}
