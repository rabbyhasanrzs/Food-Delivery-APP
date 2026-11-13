package com.example.fooddelivery;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fooddelivery.database.DatabaseHelper;
import com.example.fooddelivery.models.Product;
import com.example.fooddelivery.utils.Constants;
import com.example.fooddelivery.utils.CurrencyFormatter;
import com.example.fooddelivery.utils.ProductImages;

/**
 * Minimal Product Details screen - one reusable screen for every
 * burger (blueprint Section 4, screen 5). Quantity stepper and
 * Spicy slider are included; toppings and side options are deferred
 * until after the lab final, to keep this build simple and stable.
 */
public class ProductDetailActivity extends AppCompatActivity {

    private Product product;
    private int quantity = 1;
    private String spicyLevel = "Medium";
    private TextView quantityText;
    private TextView totalText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        int productId = getIntent().getIntExtra(Constants.EXTRA_PRODUCT_ID, -1);
        product = new DatabaseHelper(this).getProductById(productId);
        if (product == null) {
            Toast.makeText(this, "Product not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        ImageView imageView = findViewById(R.id.detail_image);
        ProductImages.setProductImage(imageView, product.getImageName());

        ((TextView) findViewById(R.id.detail_name)).setText(product.getName());
        ((TextView) findViewById(R.id.detail_description)).setText(product.getDescription());
        ((TextView) findViewById(R.id.detail_rating)).setText(
                getString(R.string.detail_rating_time, product.getRating(), product.getDeliveryTime()));
        ((TextView) findViewById(R.id.detail_price)).setText(
                CurrencyFormatter.format(product.getPriceBdt()));

        quantityText = findViewById(R.id.detail_quantity);
        totalText = findViewById(R.id.detail_total);
        updateTotal();

        findViewById(R.id.detail_back).setOnClickListener(v -> finish());
        findViewById(R.id.detail_search).setOnClickListener(v -> finish());

        Button minusButton = findViewById(R.id.detail_minus);
        Button plusButton = findViewById(R.id.detail_plus);
        minusButton.setOnClickListener(v -> {
            if (quantity > 1) {
                quantity--;
                updateTotal();
            }
        });
        plusButton.setOnClickListener(v -> {
            if (quantity < 10) {
                quantity++;
                updateTotal();
            }
        });

        TextView spicyLabel = findViewById(R.id.detail_spicy_label);
        SeekBar spicyBar = findViewById(R.id.detail_spicy);
        spicyBar.setMax(2);
        spicyBar.setProgress(1);
        spicyBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress == 0) {
                    spicyLevel = "Mild";
                } else if (progress == 1) {
                    spicyLevel = "Medium";
                } else {
                    spicyLevel = "Hot";
                }
                spicyLabel.setText(getString(R.string.detail_spicy, spicyLevel));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        Button orderButton = findViewById(R.id.detail_order_button);
        orderButton.setOnClickListener(v -> {
            Intent intent = new Intent(ProductDetailActivity.this, CheckoutActivity.class);
            intent.putExtra(Constants.EXTRA_PRODUCT_ID, product.getId());
            intent.putExtra(Constants.EXTRA_QUANTITY, quantity);
            intent.putExtra(Constants.EXTRA_SPICY_LEVEL, spicyLevel);
            startActivity(intent);
        });
    }

    private void updateTotal() {
        quantityText.setText(String.valueOf(quantity));
        // The red price pill shows the live total (price x portion),
        // as in the design; the small line shows the unit price.
        TextView priceText = findViewById(R.id.detail_price);
        priceText.setText(CurrencyFormatter.format(product.getPriceBdt() * quantity));
        totalText.setText(CurrencyFormatter.format(product.getPriceBdt()) + " × " + quantity);
    }
}
