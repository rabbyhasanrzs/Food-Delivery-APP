package com.example.fooddelivery.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.fooddelivery.models.Product;
import com.example.fooddelivery.models.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Phase 1 - local SQLite database (blueprint Section 7).
 *
 * <p>One local database file, {@code food_delivery.db}, version 1.
 * Five tables, all on the phone, no server. Every money column is an
 * integer number of whole taka with {@code _bdt} in its name, so
 * nobody reading the schema can mistake the currency or the type.</p>
 *
 * <p>No Activity writes SQL. All queries live here and are added in
 * the later phases that use them.</p>
 *
 * <p><b>Honesty note for the viva (blueprint Section 7):</b> the
 * {@code users} table is a simple local password store for a
 * classroom demo on one phone. It is <b>not</b> production-grade
 * security and must never be described as such. No real card data is
 * stored anywhere - payment is a demo label only.</p>
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    // Table names
    public static final String TABLE_USERS = "users";
    public static final String TABLE_PRODUCTS = "products";
    public static final String TABLE_ORDERS = "orders";
    public static final String TABLE_ORDER_ITEMS = "order_items";
    public static final String TABLE_ORDER_ITEM_CUSTOMISATIONS = "order_item_customisations";

    // Common column
    public static final String COL_ID = "id";

    // users columns
    public static final String COL_FULL_NAME = "full_name";
    public static final String COL_EMAIL = "email";
    public static final String COL_PHONE = "phone";
    public static final String COL_DELIVERY_ADDRESS = "delivery_address";
    public static final String COL_PASSWORD = "password";
    public static final String COL_CREATED_AT = "created_at";

    // products columns
    public static final String COL_NAME = "name";
    public static final String COL_DESCRIPTION = "description";
    public static final String COL_PRICE_BDT = "price_bdt";
    public static final String COL_IMAGE_NAME = "image_name";
    public static final String COL_RATING = "rating";
    public static final String COL_DELIVERY_TIME = "delivery_time";
    public static final String COL_CATEGORY = "category";
    public static final String COL_IS_FAVOURITE = "is_favourite";

    // orders columns
    public static final String COL_USER_ID = "user_id";
    public static final String COL_ORDER_DATE_TIME = "order_date_time";
    public static final String COL_SUBTOTAL_BDT = "subtotal_bdt";
    public static final String COL_TAX_BDT = "tax_bdt";
    public static final String COL_DELIVERY_FEE_BDT = "delivery_fee_bdt";
    public static final String COL_TOTAL_BDT = "total_bdt";
    public static final String COL_PAYMENT_METHOD = "payment_method";
    public static final String COL_STATUS = "status";

    // order_items columns
    public static final String COL_ORDER_ID = "order_id";
    public static final String COL_PRODUCT_ID = "product_id";
    public static final String COL_PRODUCT_NAME = "product_name";
    public static final String COL_QUANTITY = "quantity";
    public static final String COL_SPICY_LEVEL = "spicy_level";
    public static final String COL_UNIT_PRICE_BDT = "unit_price_bdt";
    public static final String COL_LINE_TOTAL_BDT = "line_total_bdt";

    // order_item_customisations columns
    public static final String COL_ORDER_ITEM_ID = "order_item_id";
    public static final String COL_TYPE = "type";
    // COL_NAME and COL_PRICE_BDT are reused for customisations.

    private static final String CREATE_TABLE_USERS =
            "CREATE TABLE " + TABLE_USERS + " ("
                    + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COL_FULL_NAME + " TEXT NOT NULL, "
                    + COL_EMAIL + " TEXT NOT NULL UNIQUE, "
                    + COL_PHONE + " TEXT, "
                    + COL_DELIVERY_ADDRESS + " TEXT, "
                    + COL_PASSWORD + " TEXT NOT NULL, "
                    + COL_CREATED_AT + " TEXT"
                    + ");";

    private static final String CREATE_TABLE_PRODUCTS =
            "CREATE TABLE " + TABLE_PRODUCTS + " ("
                    + COL_ID + " INTEGER PRIMARY KEY, "
                    + COL_NAME + " TEXT, "
                    + COL_DESCRIPTION + " TEXT, "
                    + COL_PRICE_BDT + " INTEGER, "
                    + COL_IMAGE_NAME + " TEXT, "
                    + COL_RATING + " REAL, "
                    + COL_DELIVERY_TIME + " TEXT, "
                    + COL_CATEGORY + " TEXT, "
                    + COL_IS_FAVOURITE + " INTEGER DEFAULT 0"
                    + ");";

    private static final String CREATE_TABLE_ORDERS =
            "CREATE TABLE " + TABLE_ORDERS + " ("
                    + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COL_USER_ID + " INTEGER NOT NULL, "
                    + COL_ORDER_DATE_TIME + " TEXT, "
                    + COL_SUBTOTAL_BDT + " INTEGER, "
                    + COL_TAX_BDT + " INTEGER, "
                    + COL_DELIVERY_FEE_BDT + " INTEGER, "
                    + COL_TOTAL_BDT + " INTEGER, "
                    + COL_PAYMENT_METHOD + " TEXT, "
                    + COL_STATUS + " TEXT DEFAULT 'Placed'"
                    + ");";

    private static final String CREATE_TABLE_ORDER_ITEMS =
            "CREATE TABLE " + TABLE_ORDER_ITEMS + " ("
                    + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COL_ORDER_ID + " INTEGER NOT NULL, "
                    + COL_PRODUCT_ID + " INTEGER NOT NULL, "
                    + COL_PRODUCT_NAME + " TEXT, "
                    + COL_QUANTITY + " INTEGER, "
                    + COL_SPICY_LEVEL + " TEXT, "
                    + COL_UNIT_PRICE_BDT + " INTEGER, "
                    + COL_LINE_TOTAL_BDT + " INTEGER"
                    + ");";

    private static final String CREATE_TABLE_ORDER_ITEM_CUSTOMISATIONS =
            "CREATE TABLE " + TABLE_ORDER_ITEM_CUSTOMISATIONS + " ("
                    + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COL_ORDER_ITEM_ID + " INTEGER NOT NULL, "
                    + COL_TYPE + " TEXT, "
                    + COL_NAME + " TEXT, "
                    + COL_PRICE_BDT + " INTEGER"
                    + ");";

    public DatabaseHelper(Context context) {
        super(context, com.example.fooddelivery.utils.Constants.DATABASE_NAME,
                null, com.example.fooddelivery.utils.Constants.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Deliberately no cart table (no Cart screen), no messages
        // table (Customer Support excluded) and no cards table
        // (payment is a demo label only) - blueprint Section 7.
        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_PRODUCTS);
        db.execSQL(CREATE_TABLE_ORDERS);
        db.execSQL(CREATE_TABLE_ORDER_ITEMS);
        db.execSQL(CREATE_TABLE_ORDER_ITEM_CUSTOMISATIONS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Version 1 is the first schema. When the schema changes in a
        // later phase, drop and recreate the tables here.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDER_ITEM_CUSTOMISATIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDER_ITEMS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    // ---- Minimal queries for the lab-final app ----
    // Every query lives here. No Activity writes SQL.

    /** Insert a new local user. Returns the new id, or -1 on failure. */
    public long insertUser(String fullName, String email, String password) {
        ContentValues values = new ContentValues();
        values.put(COL_FULL_NAME, fullName);
        values.put(COL_EMAIL, email);
        values.put(COL_PASSWORD, password);
        values.put(COL_CREATED_AT, String.valueOf(System.currentTimeMillis()));
        return getWritableDatabase().insert(TABLE_USERS, null, values);
    }

    /** Find a user by email, or null when there is no such account. */
    public User getUserByEmail(String email) {
        Cursor cursor = getReadableDatabase().query(TABLE_USERS, null,
                COL_EMAIL + " = ?", new String[]{email},
                null, null, null);
        try {
            if (cursor.moveToFirst()) {
                return userFromCursor(cursor);
            }
            return null;
        } finally {
            cursor.close();
        }
    }

    public User getUserById(long userId) {
        Cursor cursor = getReadableDatabase().query(TABLE_USERS, null,
                COL_ID + " = ?", new String[]{String.valueOf(userId)},
                null, null, null);
        try {
            if (cursor.moveToFirst()) {
                return userFromCursor(cursor);
            }
            return null;
        } finally {
            cursor.close();
        }
    }

    private User userFromCursor(Cursor cursor) {
        return new User(
                cursor.getLong(cursor.getColumnIndexOrThrow(COL_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_FULL_NAME)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_EMAIL)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_PASSWORD)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_PHONE)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_DELIVERY_ADDRESS)));
    }

    /** Insert one demo product (id is fixed by ProductCatalog). */
    public long insertProduct(Product product) {
        ContentValues values = new ContentValues();
        values.put(COL_ID, product.getId());
        values.put(COL_NAME, product.getName());
        values.put(COL_DESCRIPTION, product.getDescription());
        values.put(COL_PRICE_BDT, product.getPriceBdt());
        values.put(COL_IMAGE_NAME, product.getImageName());
        values.put(COL_RATING, product.getRating());
        values.put(COL_DELIVERY_TIME, product.getDeliveryTime());
        values.put(COL_CATEGORY, product.getCategory());
        values.put(COL_IS_FAVOURITE, product.isFavourite() ? 1 : 0);
        return getWritableDatabase().insert(TABLE_PRODUCTS, null, values);
    }

    /** All products, in id order. */
    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        Cursor cursor = getReadableDatabase().query(TABLE_PRODUCTS, null,
                null, null, null, null, COL_ID + " ASC");
        try {
            while (cursor.moveToNext()) {
                products.add(productFromCursor(cursor));
            }
        } finally {
            cursor.close();
        }
        return products;
    }

    public Product getProductById(int productId) {
        Cursor cursor = getReadableDatabase().query(TABLE_PRODUCTS, null,
                COL_ID + " = ?", new String[]{String.valueOf(productId)},
                null, null, null);
        try {
            if (cursor.moveToFirst()) {
                return productFromCursor(cursor);
            }
            return null;
        } finally {
            cursor.close();
        }
    }

    private Product productFromCursor(Cursor cursor) {
        return new Product(
                cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_NAME)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_DESCRIPTION)),
                cursor.getInt(cursor.getColumnIndexOrThrow(COL_PRICE_BDT)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_IMAGE_NAME)),
                cursor.getDouble(cursor.getColumnIndexOrThrow(COL_RATING)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_DELIVERY_TIME)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_CATEGORY)),
                cursor.getInt(cursor.getColumnIndexOrThrow(COL_IS_FAVOURITE)) == 1);
    }

    /** Toggle the favourite flag for one product. */
    public void updateFavourite(int productId, boolean isFavourite) {
        ContentValues values = new ContentValues();
        values.put(COL_IS_FAVOURITE, isFavourite ? 1 : 0);
        getWritableDatabase().update(TABLE_PRODUCTS, values,
                COL_ID + " = ?", new String[]{String.valueOf(productId)});
    }

    /** Save a completed order. Returns the order number (id), or -1. */
    public long insertOrder(long userId, int subtotalBdt, int taxBdt,
                            int deliveryFeeBdt, int totalBdt, String paymentMethod) {
        ContentValues values = new ContentValues();
        values.put(COL_USER_ID, userId);
        values.put(COL_ORDER_DATE_TIME, String.valueOf(System.currentTimeMillis()));
        values.put(COL_SUBTOTAL_BDT, subtotalBdt);
        values.put(COL_TAX_BDT, taxBdt);
        values.put(COL_DELIVERY_FEE_BDT, deliveryFeeBdt);
        values.put(COL_TOTAL_BDT, totalBdt);
        values.put(COL_PAYMENT_METHOD, paymentMethod);
        values.put(COL_STATUS, com.example.fooddelivery.utils.Constants.ORDER_STATUS_PLACED);
        return getWritableDatabase().insert(TABLE_ORDERS, null, values);
    }

    /** Save one product line of an order. */
    public long insertOrderItem(long orderId, int productId, String productName,
                                int quantity, String spicyLevel,
                                int unitPriceBdt, int lineTotalBdt) {
        ContentValues values = new ContentValues();
        values.put(COL_ORDER_ID, orderId);
        values.put(COL_PRODUCT_ID, productId);
        values.put(COL_PRODUCT_NAME, productName);
        values.put(COL_QUANTITY, quantity);
        values.put(COL_SPICY_LEVEL, spicyLevel);
        values.put(COL_UNIT_PRICE_BDT, unitPriceBdt);
        values.put(COL_LINE_TOTAL_BDT, lineTotalBdt);
        return getWritableDatabase().insert(TABLE_ORDER_ITEMS, null, values);
    }
}
