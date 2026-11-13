package com.example.fooddelivery;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fooddelivery.database.DatabaseHelper;
import com.example.fooddelivery.models.Product;
import com.example.fooddelivery.models.User;
import com.example.fooddelivery.utils.Constants;
import com.example.fooddelivery.utils.CurrencyFormatter;
import com.example.fooddelivery.utils.PricingConfig;
import com.example.fooddelivery.utils.ProductImages;
import com.example.fooddelivery.utils.SessionManager;

/**
 * Minimal Checkout and Payment screen (blueprint Section 4, screen 6).
 * Direct checkout - no Cart screen. Demo payment only: the user
 * picks a payment label, no real card number is typed, checked,
 * stored or sent anywhere.
 */
public class CheckoutActivity extends AppCompatActivity {

    private Product product;
    private int quantity;
    private String spicyLevel;
    private int subtotalBdt;
    private int taxBdt;
    private int totalBdt;
    private EditText nameInput;
    private EditText addressInput;
    private RadioGroup paymentGroup;
    private DatabaseHelper db;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        db = new DatabaseHelper(this);
        sessionManager = new SessionManager(this);

        int productId = getIntent().getIntExtra(Constants.EXTRA_PRODUCT_ID, -1);
        quantity = getIntent().getIntExtra(Constants.EXTRA_QUANTITY, 1);
        spicyLevel = getIntent().getStringExtra(Constants.EXTRA_SPICY_LEVEL);
        if (spicyLevel == null) {
            spicyLevel = "Medium";
        }
        product = db.getProductById(productId);
        if (product == null) {
            Toast.makeText(this, "Product not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // subtotal = price x quantity; tax and total from PricingConfig.
        subtotalBdt = product.getPriceBdt() * quantity;
        taxBdt = PricingConfig.calculateTax(subtotalBdt);
        totalBdt = PricingConfig.calculateTotal(subtotalBdt);

        ProductImages.setProductImage(
                findViewById(R.id.checkout_product_image), product.getImageName());

        ((TextView) findViewById(R.id.checkout_product)).setText(
                getString(R.string.checkout_product_line,
                        product.getName(), quantity, spicyLevel));
        ((TextView) findViewById(R.id.checkout_subtotal)).setText(
                CurrencyFormatter.format(subtotalBdt));
        ((TextView) findViewById(R.id.checkout_tax)).setText(
                CurrencyFormatter.format(taxBdt));
        ((TextView) findViewById(R.id.checkout_delivery)).setText(
                CurrencyFormatter.format(PricingConfig.DELIVERY_FEE_BDT));
        ((TextView) findViewById(R.id.checkout_total)).setText(
                CurrencyFormatter.format(totalBdt));

        nameInput = findViewById(R.id.checkout_name);
        addressInput = findViewById(R.id.checkout_address);
        User user = db.getUserById(sessionManager.getUserId());
        if (user != null) {
            nameInput.setText(user.getFullName());
            if (user.getDeliveryAddress() != null) {
                addressInput.setText(user.getDeliveryAddress());
            }
        }

        paymentGroup = findViewById(R.id.checkout_payment_group);

        Button placeOrderButton = findViewById(R.id.checkout_place_order_button);
        placeOrderButton.setOnClickListener(v -> placeOrder());
    }

    private void placeOrder() {
        String name = nameInput.getText().toString().trim();
        String address = addressInput.getText().toString().trim();
        if (name.isEmpty() || address.isEmpty()) {
            Toast.makeText(this, "Please enter your name and delivery address",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        String paymentMethod = paymentGroup.getCheckedRadioButtonId() == R.id.payment_demo_card
                ? "Demo Card"
                : "Cash on Delivery";

        long orderId = db.insertOrder(sessionManager.getUserId(),
                subtotalBdt, taxBdt, PricingConfig.DELIVERY_FEE_BDT,
                totalBdt, paymentMethod);
        if (orderId == -1) {
            Toast.makeText(this, "Order failed, please try again", Toast.LENGTH_SHORT).show();
            return;
        }
        db.insertOrderItem(orderId, product.getId(), product.getName(),
                quantity, spicyLevel, product.getPriceBdt(), subtotalBdt);

        Intent intent = new Intent(CheckoutActivity.this, OrderSuccessActivity.class);
        intent.putExtra(Constants.EXTRA_ORDER_ID, orderId);
        intent.putExtra(Constants.EXTRA_TOTAL_BDT, totalBdt);
        startActivity(intent);
        finish();
    }
}
