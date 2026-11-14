package com.example.fooddelivery;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class ProductDetailActivity extends AppCompatActivity {

    private Food food;
    private int portion = 1;
    private TextView tvPortion, tvPrice;
    private SeekBar seekSpicy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        // Home theke Intent e food data thakle oita use koro (cloud dam thik thake);
        // na thakle id diye DB theke food ane
        food = Food.fromIntent(getIntent());
        if (food == null) {
            int id = getIntent().getIntExtra("food_id", -1);
            food = new DatabaseHelper(this).getFood(id);
        }
        if (food == null) { finish(); return; }

        ImageView img = findViewById(R.id.imgFood);
        TextView tvName = findViewById(R.id.tvName);
        TextView tvRating = findViewById(R.id.tvRating);
        TextView tvDesc = findViewById(R.id.tvDesc);
        tvPortion = findViewById(R.id.tvPortion);
        tvPrice = findViewById(R.id.tvPrice);
        seekSpicy = findViewById(R.id.seekSpicy);

        int res = getResources().getIdentifier(food.getImage(), "drawable", getPackageName());
        img.setImageResource(res != 0 ? res : R.drawable.ic_burger);
        tvName.setText(food.getName() + " " + food.getSubtitle());
        tvRating.setText("★ " + food.getRating() + "  —  " + food.getTime() + " mins");
        tvDesc.setText(food.getDescription());
        updatePrice();

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        // Portion + / -
        findViewById(R.id.btnPlus).setOnClickListener(v -> {
            if (portion < 10) { portion++; updatePrice(); }
        });
        findViewById(R.id.btnMinus).setOnClickListener(v -> {
            if (portion > 1) { portion--; updatePrice(); }
            else Toast.makeText(this, "Minimum 1 portion", Toast.LENGTH_SHORT).show();
        });

        // Order Now -> Payment screen e data niye jay
        findViewById(R.id.btnOrder).setOnClickListener(v -> {
            Intent i = new Intent(this, PaymentActivity.class);
            Food.putInIntent(i, food);
            i.putExtra("portion", portion);
            i.putExtra("spicy", seekSpicy.getProgress());
            startActivity(i);
        });
    }

    // Portion bodlaleo price auto update
    private void updatePrice() {
        tvPortion.setText(String.valueOf(portion));
        tvPrice.setText("\u09f3" + (food.getPrice() * portion)); // ৳ + integer Taka
    }
}
