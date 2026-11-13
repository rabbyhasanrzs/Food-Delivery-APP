package com.example.fooddelivery.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Phase 1 - login session (blueprint Sections 5 and 10).
 *
 * <p>The only class that touches SharedPreferences. It saves, reads
 * and clears the logged-in user's id, name and email. Splash reads it
 * to decide where to go, and Logout (Phase 7) clears exactly these
 * values. Orders and the account itself stay in SQLite.</p>
 */
public class SessionManager {

    private final SharedPreferences preferences;

    public SessionManager(Context context) {
        preferences = context.getSharedPreferences(
                Constants.PREFS_NAME, Context.MODE_PRIVATE);
    }

    /** Save the session after a successful login or registration. */
    public void saveSession(long userId, String name, String email) {
        preferences.edit()
                .putLong(Constants.KEY_USER_ID, userId)
                .putString(Constants.KEY_USER_NAME, name)
                .putString(Constants.KEY_USER_EMAIL, email)
                .apply();
    }

    /** True when a user id has been saved, false after logout. */
    public boolean isLoggedIn() {
        return preferences.getLong(Constants.KEY_USER_ID, -1L) != -1L;
    }

    public long getUserId() {
        return preferences.getLong(Constants.KEY_USER_ID, -1L);
    }

    public String getUserName() {
        return preferences.getString(Constants.KEY_USER_NAME, "");
    }

    public String getUserEmail() {
        return preferences.getString(Constants.KEY_USER_EMAIL, "");
    }

    /** Clear the session on logout. */
    public void clearSession() {
        preferences.edit().clear().apply();
    }
}
