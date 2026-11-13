package com.example.fooddelivery.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
}
