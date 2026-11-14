package com.example.fooddelivery;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class PaymentActivity extends AppCompatActivity {

    // DEMO: tax 5% of order (rounded) + delivery 30 Taka. Total = order + tax + delivery.

    private DatabaseHelper db;
    private Food food;
    private int portion;
    private int total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        db = new DatabaseHelper(this);
        food = db.getFood(getIntent().getIntExtra("food_id", -1));
        portion = getIntent().getIntExtra("portion", 1);
        if (food == null) { finish(); return; }

        // Hisab (BDT, integer Taka): order + tax (5%) + delivery (30)
        int order = food.getPrice() * portion;
        int tax = (int) Math.round(order * 0.05);
        int delivery = 30;
        total = order + tax + delivery;

        ((TextView) findViewById(R.id.tvOrder)).setText(money(order));
        ((TextView) findViewById(R.id.tvTax)).setText(money(tax));
        ((TextView) findViewById(R.id.tvDelivery)).setText(money(delivery));
        ((TextView) findViewById(R.id.tvTotal)).setText(money(total));
        ((TextView) findViewById(R.id.tvTotalPrice)).setText(money(total));

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        findViewById(R.id.btnPay).setOnClickListener(v -> pay());
    }

    private void pay() {
        String email = new SessionManager(this).getEmail();
        // Order SQLite e save
        if (db.insertOrder(email, food.getName() + " " + food.getSubtitle(), portion, total)) {
            showSuccessDialog();
        } else {
            Toast.makeText(this, "Payment failed, abar try koro", Toast.LENGTH_SHORT).show();
        }
    }

    private void showSuccessDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_success);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button btnGoBack = dialog.findViewById(R.id.btnGoBack);
        btnGoBack.setOnClickListener(v -> {
            dialog.dismiss();
            Intent i = new Intent(this, HomeActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            finish();
        });
        dialog.show();
    }

    private String money(int v) {
        return "\u09f3" + v; // Taka sign ৳ + integer Taka
    }
}
