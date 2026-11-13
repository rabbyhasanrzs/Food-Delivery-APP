package com.example.fooddelivery;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fooddelivery.utils.Constants;
import com.example.fooddelivery.utils.CurrencyFormatter;

/**
 * Minimal Order Success screen (blueprint Section 4, screen 7).
 * Shows the order number and the total paid. Going back to Home
 * never returns to Checkout, so an order cannot be paid twice.
 */
public class OrderSuccessActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_success);

        long orderId = getIntent().getLongExtra(Constants.EXTRA_ORDER_ID, -1);
        int totalBdt = getIntent().getIntExtra(Constants.EXTRA_TOTAL_BDT, 0);

        ((TextView) findViewById(R.id.success_order_number)).setText(
                getString(R.string.success_order_number, orderId));
        ((TextView) findViewById(R.id.success_total)).setText(
                CurrencyFormatter.format(totalBdt));

        Button homeButton = findViewById(R.id.success_home_button);
        homeButton.setOnClickListener(v -> {
            Intent intent = new Intent(OrderSuccessActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }
}
