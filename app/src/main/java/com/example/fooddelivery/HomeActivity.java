package com.example.fooddelivery;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {

    private final String[] categories = {"All", "Combos", "Sliders", "Classic"};
    private String selectedCategory = "All";
    private boolean onlyFav = false;

    private DatabaseHelper db;
    private List<Food> cloudFoods = null; // Firestore theke menu ashle ekhane thake
    private boolean cloudTried = false; // Firestore ekbar try korlei hobe
    private FoodAdapter adapter;
    private EditText etSearch;
    private LinearLayout chipContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        db = new DatabaseHelper(this);
        etSearch = findViewById(R.id.etSearch);
        chipContainer = findViewById(R.id.chipContainer);

        // RecyclerView setup: 2 column grid
        RecyclerView rv = findViewById(R.id.rvFoods);
        rv.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new FoodAdapter(this, new ArrayList<>(), food -> {
            Intent i = new Intent(this, ProductDetailActivity.class);
            i.putExtra("food_id", food.getId()); // next screen e id pathai
            startActivity(i);
        });
        rv.setAdapter(adapter);

        // Welcome subtitle
        String name = new SessionManager(this).getName();
        ((TextView) findViewById(R.id.tvTagline)).setText("Hi " + name + ", order your favourite food!");

        buildChips();
        loadFoods();

        // Search: user type korle sathe sathe list update
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int a, int b, int c) {}
            @Override public void onTextChanged(CharSequence s, int a, int b, int c) { loadFoods(); }
            @Override public void afterTextChanged(Editable s) {}
        });

        // Bottom bar
        findViewById(R.id.navHome).setOnClickListener(v -> {
            onlyFav = false; selectedCategory = "All"; etSearch.setText("");
            buildChips(); loadFoods();
        });
        findViewById(R.id.navFav).setOnClickListener(v -> { onlyFav = true; loadFoods(); });
        findViewById(R.id.navProfile).setOnClickListener(v -> showLogoutDialog());
    }

    // Category chip gulo code diye banano
    private void buildChips() {
        chipContainer.removeAllViews();
        for (String cat : categories) {
            TextView chip = new TextView(this);
            chip.setText(cat);
            chip.setGravity(Gravity.CENTER);
            chip.setPadding(dp(18), dp(8), dp(18), dp(8));
            boolean selected = cat.equals(selectedCategory);
            chip.setBackgroundResource(selected ? R.drawable.bg_chip_on : R.drawable.bg_chip_off);
            chip.setTextColor(selected ? 0xFFFFFFFF : 0xFF555555);

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMarginEnd(dp(8));
            chip.setLayoutParams(lp);

            chip.setOnClickListener(v -> {
                selectedCategory = cat;
                onlyFav = false;
                buildChips();
                loadFoods();
            });
            chipContainer.addView(chip);
        }
    }

    // Menu ane: age Firestore (cloud) try koro, na parle SQLite
    private void loadFoods() {
        if (cloudFoods != null) { // cloud menu paile oita thekei filter
            refreshFavorites(cloudFoods);
            adapter.setData(filterFoods(cloudFoods));
            return;
        }
        if (cloudTried) { // Firestore ekbar try hoye geche — SQLite diye cholbe
            adapter.setData(db.getFoods(selectedCategory, etSearch.getText().toString(), onlyFav));
            return;
        }
        cloudTried = true;
        FirestoreHelper.fetchMenu(this, new FirestoreHelper.MenuCallback() {
            @Override public void onMenu(List<Food> foods) {
                if (foods == null || foods.isEmpty()) {
                    adapter.setData(db.getFoods(selectedCategory, etSearch.getText().toString(), onlyFav));
                    return;
                }
                cloudFoods = foods;
                refreshFavorites(cloudFoods);
                adapter.setData(filterFoods(cloudFoods));
            }
            @Override public void onError(Exception e) {
                adapter.setData(db.getFoods(selectedCategory, etSearch.getText().toString(), onlyFav));
            }
        });
    }

    // Favourite SQLite e thake — cloud list er sathe miliye dao
    private void refreshFavorites(List<Food> foods) {
        Map<Integer, Boolean> fav = new HashMap<>();
        for (Food f : db.getFoods("All", "", false)) fav.put(f.getId(), f.isFavorite());
        for (Food f : foods) {
            Boolean v = fav.get(f.getId());
            if (v != null) f.setFavorite(v);
        }
    }

    // Cloud list theke category / search / favourite filter (SQLite er motoi)
    private List<Food> filterFoods(List<Food> foods) {
        List<Food> out = new ArrayList<>();
        String q = etSearch.getText().toString().trim().toLowerCase();
        for (Food f : foods) {
            if (!selectedCategory.equals("All") && !selectedCategory.equals(f.getCategory())) continue;
            if (!q.isEmpty() && !(f.getName() + " " + f.getSubtitle()).toLowerCase().contains(q)) continue;
            if (onlyFav && !f.isFavorite()) continue;
            out.add(f);
        }
        return out;
    }

    private void showLogoutDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Apni ki logout korte chan?")
                .setPositiveButton("Yes", (d, w) -> {
                    new SessionManager(this).logout();
                    startActivity(new Intent(this, SignupActivity.class));
                    finishAffinity();
                })
                .setNegativeButton("No", null)
                .show();
    }

    private int dp(int value) {
        return Math.round(value * getResources().getDisplayMetrics().density);
    }
}
