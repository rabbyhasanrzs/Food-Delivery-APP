package com.example.fooddelivery;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

// SQLite er shob kaj ekhane (table create, insert, select, update)
// DEMO prices in BDT (Taka, integer). Final prices pending - do not treat as final.
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "rabby.db";
    private static final int DB_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    // App prothom bar cholle ekbar call hoy: table banay + sample food dhukay
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE users (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, email TEXT UNIQUE, password TEXT)");

        db.execSQL("CREATE TABLE foods (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, subtitle TEXT, rating REAL, price INTEGER, description TEXT, " +
                "image TEXT, category TEXT, time INTEGER, favorite INTEGER DEFAULT 0)");

        db.execSQL("CREATE TABLE orders (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_email TEXT, food_name TEXT, portion INTEGER, total INTEGER, " +
                "date TEXT DEFAULT CURRENT_TIMESTAMP)");

        // image = drawable er file name (jemon burger1). File na thakle placeholder dekhabe
        addFood(db, "Cheeseburger", "Wendy's Burger", 4.9, 350, // DEMO ৳350
                "The Cheeseburger Wendy's Burger is a classic fast food burger that packs a punch of flavor in every bite. Made with a juicy beef patty cooked to perfection, it's topped with melted American cheese, crispy lettuce, ripe tomato, and crunchy pickles.",
                "burger1", "Classic", 26);
        addFood(db, "Hamburger", "Veggie Burger", 4.8, 300, // DEMO ৳300
                "Enjoy our delicious Hamburger Veggie Burger, made with a savory blend of vegetables and herbs, topped with crisp lettuce, juicy tomatoes, and tangy pickles, all served on a soft, toasted bun.",
                "burger2", "Combos", 14);
        addFood(db, "Hamburger", "Chicken Burger", 4.6, 320, // DEMO ৳320
                "A tender grilled chicken fillet with fresh lettuce, tomato and creamy sauce in a soft sesame bun.",
                "burger3", "Sliders", 18);
        addFood(db, "Hamburger", "Fried Chicken Burger", 4.5, 340, // DEMO ৳340
                "Crispy golden fried chicken with spicy mayo and crunchy slaw, served in a toasted bun.",
                "burger4", "Classic", 20);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS foods");
        db.execSQL("DROP TABLE IF EXISTS orders");
        onCreate(db);
    }

    private void addFood(SQLiteDatabase db, String name, String sub, double rating, int price,
                         String desc, String image, String category, int time) {
        ContentValues cv = new ContentValues();
        cv.put("name", name);
        cv.put("subtitle", sub);
        cv.put("rating", rating);
        cv.put("price", price);
        cv.put("description", desc);
        cv.put("image", image);
        cv.put("category", category);
        cv.put("time", time);
        db.insert("foods", null, cv);
    }

    // ================= USER =================

    public boolean emailExists(String email) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT id FROM users WHERE email=?", new String[]{email});
        boolean exists = c.getCount() > 0;
        c.close();
        return exists;
    }

    // Notun user save kore. Email age thakle false return kore
    public boolean registerUser(String name, String email, String password) {
        if (emailExists(email)) return false;
        ContentValues cv = new ContentValues();
        cv.put("name", name);
        cv.put("email", email);
        cv.put("password", password);
        return getWritableDatabase().insert("users", null, cv) != -1;
    }

    // Login thik hole user er name return kore, na hole null
    public String loginUser(String email, String password) {
        Cursor c = getReadableDatabase().rawQuery(
                "SELECT name FROM users WHERE email=? AND password=?", new String[]{email, password});
        String name = null;
        if (c.moveToFirst()) name = c.getString(0);
        c.close();
        return name;
    }

    // ================= FOOD =================

    // category = "All" hole sob dekhabe. search = nam diye khuje. onlyFav = shudhu favourite
    public List<Food> getFoods(String category, String search, boolean onlyFav) {
        List<Food> list = new ArrayList<>();
        StringBuilder where = new StringBuilder("1=1");
        List<String> args = new ArrayList<>();

        if (!category.equals("All")) {
            where.append(" AND category=?");
            args.add(category);
        }
        if (search != null && !search.trim().isEmpty()) {
            where.append(" AND (name LIKE ? OR subtitle LIKE ?)");
            args.add("%" + search.trim() + "%");
            args.add("%" + search.trim() + "%");
        }
        if (onlyFav) where.append(" AND favorite=1");

        Cursor c = getReadableDatabase().rawQuery(
                "SELECT * FROM foods WHERE " + where, args.toArray(new String[0]));
        while (c.moveToNext()) list.add(fromCursor(c));
        c.close();
        return list;
    }

    public Food getFood(int id) {
        Cursor c = getReadableDatabase().rawQuery(
                "SELECT * FROM foods WHERE id=?", new String[]{String.valueOf(id)});
        Food f = null;
        if (c.moveToFirst()) f = fromCursor(c);
        c.close();
        return f;
    }

    public void setFavorite(int id, boolean fav) {
        ContentValues cv = new ContentValues();
        cv.put("favorite", fav ? 1 : 0);
        getWritableDatabase().update("foods", cv, "id=?", new String[]{String.valueOf(id)});
    }

    // Cursor er ek row ke Food object e convert kore
    private Food fromCursor(Cursor c) {
        return new Food(
                c.getInt(c.getColumnIndexOrThrow("id")),
                c.getString(c.getColumnIndexOrThrow("name")),
                c.getString(c.getColumnIndexOrThrow("subtitle")),
                c.getDouble(c.getColumnIndexOrThrow("rating")),
                c.getInt(c.getColumnIndexOrThrow("price")),
                c.getString(c.getColumnIndexOrThrow("description")),
                c.getString(c.getColumnIndexOrThrow("image")),
                c.getString(c.getColumnIndexOrThrow("category")),
                c.getInt(c.getColumnIndexOrThrow("time")),
                c.getInt(c.getColumnIndexOrThrow("favorite")) == 1);
    }

    // ================= ORDER =================

    public boolean insertOrder(String email, String foodName, int portion, int total) {
        ContentValues cv = new ContentValues();
        cv.put("user_email", email);
        cv.put("food_name", foodName);
        cv.put("portion", portion);
        cv.put("total", total);
        return getWritableDatabase().insert("orders", null, cv) != -1;
    }
}
