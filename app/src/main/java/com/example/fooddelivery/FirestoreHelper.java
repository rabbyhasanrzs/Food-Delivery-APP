package com.example.fooddelivery;

import android.content.Context;
import android.util.Log;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Firebase Firestore er sob kaj ekhane: order save, live tracking, cloud menu.
// Firebase setup na thakle (google-services.json nai) sob method chupchap
// null / error dey — app crash korbe na, SQLite diye cholbe.
public class FirestoreHelper {
    private static final String TAG = "FirestoreHelper";
    public static final String STATUS_PLACED = "Order Placed";
    public static final String STATUS_PREPARING = "Preparing";
    public static final String STATUS_OUT = "Out for Delivery";
    public static final String STATUS_DELIVERED = "Delivered";

    public interface MenuCallback {
        void onMenu(List<Food> foods);
        void onError(Exception e);
    }

    public interface StatusCallback {
        void onStatus(String status);
        void onError(Exception e);
    }

    private static FirebaseFirestore db(Context context) {
        try {
            if (FirebaseApp.getApps(context).isEmpty()) return null; // setup hoy nai
            return FirebaseFirestore.getInstance();
        } catch (Exception e) {
            Log.w(TAG, "Firestore pawa jay nai: " + e.getMessage());
            return null;
        }
    }

    public static boolean isAvailable(Context context) {
        return db(context) != null;
    }

    // Pay Now er por order Firestore e save kore (SQLite er pashapashi).
    public static void saveOrder(Context context, String orderId, String email,
                                 String foodName, int portion, int total) {
        FirebaseFirestore fs = db(context);
        if (fs == null) return;
        Map<String, Object> order = new HashMap<>();
        order.put("orderId", orderId);
        order.put("userEmail", email);
        order.put("foodName", foodName);
        order.put("portion", portion);
        order.put("total", total);
        order.put("status", STATUS_PLACED);
        order.put("createdAt", FieldValue.serverTimestamp());
        fs.collection("orders").document(orderId).set(order)
                .addOnFailureListener(e -> Log.w(TAG, "Order Firestore e save hoy nai: " + e.getMessage()));
    }

    // Ekta order er status live shone — bodle gelei callback ashe.
    public static ListenerRegistration listenOrderStatus(Context context, String orderId, StatusCallback cb) {
        FirebaseFirestore fs = db(context);
        if (fs == null) { cb.onError(new IllegalStateException("Firebase setup hoy nai")); return null; }
        return fs.collection("orders").document(orderId)
                .addSnapshotListener((snap, e) -> {
                    if (e != null) { cb.onError(e); return; }
                    if (snap != null && snap.exists()) {
                        String status = snap.getString("status");
                        cb.onStatus(status != null ? status : STATUS_PLACED);
                    } else {
                        cb.onError(new IllegalStateException("Order ta Firestore e pawa jay nai"));
                    }
                });
    }

    // Cloud menu: Firestore er "foods" collection theke burger list ane.
    public static void fetchMenu(Context context, MenuCallback cb) {
        FirebaseFirestore fs = db(context);
        if (fs == null) { cb.onError(new IllegalStateException("Firebase setup hoy nai")); return; }
        fs.collection("foods").get()
                .addOnSuccessListener(qs -> {
                    List<Food> foods = new ArrayList<>();
                    for (DocumentSnapshot d : qs.getDocuments()) {
                        Food f = foodFromDoc(d);
                        if (f != null) foods.add(f);
                    }
                    cb.onMenu(foods);
                })
                .addOnFailureListener(cb::onError);
    }

    private static Food foodFromDoc(DocumentSnapshot d) {
        try {
            Long idL = d.getLong("id");
            Long priceL = d.getLong("price");
            Long timeL = d.getLong("time");
            Double rating = d.getDouble("rating");
            String name = d.getString("name");
            if (name == null) return null;
            return new Food(
                    idL != null ? idL.intValue() : 0,
                    name,
                    str(d, "subtitle"),
                    rating != null ? rating : 0,
                    priceL != null ? priceL.intValue() : 0,
                    str(d, "description"),
                    str(d, "image"),
                    str(d, "category"),
                    timeL != null ? timeL.intValue() : 0,
                    false);
        } catch (Exception e) {
            Log.w(TAG, "Food doc porte pari nai: " + e.getMessage());
            return null;
        }
    }

    private static String str(DocumentSnapshot d, String field) {
        String v = d.getString(field);
        return v != null ? v : "";
    }
}
