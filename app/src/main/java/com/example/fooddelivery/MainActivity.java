package com.example.fooddelivery;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fooddelivery.adapters.ProductAdapter;
import com.example.fooddelivery.data.ProductCatalog;
import com.example.fooddelivery.database.DatabaseHelper;
import com.example.fooddelivery.models.Product;
import com.example.fooddelivery.utils.Constants;
import com.example.fooddelivery.utils.SessionManager;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Home screen, styled like the original design: RabbY brand with a
 * round avatar, search bar with a red filter button, category chips
 * (All / Combos / Sliders / Burgers), a 2-column product grid, and a
 * red bottom navigation bar.
 *
 * <p>Category mapping is demo data: Combos = Cheeseburger + Fried
 * Chicken, Sliders = Veggie + Chicken, Burgers = all four. The red
 * filter button shows favourites only. The central + button opens
 * the featured burger (Cheeseburger Wendy's). There is no chat icon
 * and no Profile screen - both are excluded / deferred.</p>
 */
public class MainActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private SessionManager sessionManager;
    private ProductAdapter adapter;
    private RecyclerView recyclerView;
    private EditText searchInput;
    private List<Product> allProducts = new ArrayList<>();
    private String currentCategory = "All";
    private boolean favouritesOnly = false;

    private static final Set<Integer> COMBOS = new HashSet<>(Arrays.asList(1, 4));
    private static final Set<Integer> SLIDERS = new HashSet<>(Arrays.asList(2, 3));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        db = new DatabaseHelper(this);
        sessionManager = new SessionManager(this);
        ProductCatalog.seedIfEmpty(db);
        allProducts = db.getAllProducts();

        String name = sessionManager.getUserName();
        TextView greetingText = findViewById(R.id.home_greeting);
        greetingText.setText(name.isEmpty()
                ? getString(R.string.home_greeting_guest)
                : getString(R.string.home_greeting, name));

        // Avatar: first letter of the user's name, as in the design.
        TextView avatarText = findViewById(R.id.home_avatar);
        avatarText.setText(name.isEmpty() ? "R" : name.substring(0, 1).toUpperCase());

        recyclerView = findViewById(R.id.home_product_list);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new ProductAdapter(new ArrayList<>(allProducts),
                product -> {
                    Intent intent = new Intent(MainActivity.this, ProductDetailActivity.class);
                    intent.putExtra(Constants.EXTRA_PRODUCT_ID, product.getId());
                    startActivity(intent);
                },
                product -> {
                    boolean newFavourite = !product.isFavourite();
                    product.setFavourite(newFavourite);
                    db.updateFavourite(product.getId(), newFavourite);
                    applyFilters();
                });
        recyclerView.setAdapter(adapter);

        searchInput = findViewById(R.id.home_search);
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                applyFilters();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        findViewById(R.id.home_filter_button).setOnClickListener(v -> {
            favouritesOnly = !favouritesOnly;
            applyFilters();
        });

        setupCategoryChips();
        setupBottomNav();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh favourites after returning from Product Details.
        allProducts = db.getAllProducts();
        applyFilters();
    }

    private void setupCategoryChips() {
        MaterialButton chipAll = findViewById(R.id.chip_all);
        MaterialButton chipCombos = findViewById(R.id.chip_combos);
        MaterialButton chipSliders = findViewById(R.id.chip_sliders);
        MaterialButton chipBurgers = findViewById(R.id.chip_burgers);
        List<MaterialButton> chips = Arrays.asList(
                chipAll, chipCombos, chipSliders, chipBurgers);
        String[] categories = {"All", "Combos", "Sliders", "Burgers"};
        for (int i = 0; i < chips.size(); i++) {
            final String category = categories[i];
            chips.get(i).setOnClickListener(v -> {
                currentCategory = category;
                paintChips(chips, category);
                applyFilters();
            });
        }
        paintChips(chips, currentCategory);
    }

    private void paintChips(List<MaterialButton> chips, String selected) {
        String[] categories = {"All", "Combos", "Sliders", "Burgers"};
        for (int i = 0; i < chips.size(); i++) {
            boolean isSelected = categories[i].equals(selected);
            chips.get(i).setBackgroundTintList(ColorStateList.valueOf(
                    getColor(isSelected ? R.color.colorPrimary : R.color.colorDivider)));
            chips.get(i).setTextColor(getColor(
                    isSelected ? R.color.colorOnPrimary : R.color.colorTextPrimary));
        }
    }

    private void setupBottomNav() {
        findViewById(R.id.nav_home).setOnClickListener(v -> {
            clearFilters();
            recyclerView.smoothScrollToPosition(0);
        });
        findViewById(R.id.nav_favourites).setOnClickListener(v -> {
            favouritesOnly = true;
            applyFilters();
        });
        // Central + : open the featured burger, as a quick order.
        findViewById(R.id.nav_add).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ProductDetailActivity.class);
            intent.putExtra(Constants.EXTRA_PRODUCT_ID, 1);
            startActivity(intent);
        });
        // Menu icon: clear every filter and show the full menu.
        findViewById(R.id.nav_orders).setOnClickListener(v -> clearFilters());
        findViewById(R.id.nav_logout).setOnClickListener(v -> logout());
        findViewById(R.id.home_logout_button).setOnClickListener(v -> logout());
    }

    private void clearFilters() {
        currentCategory = "All";
        favouritesOnly = false;
        searchInput.setText("");
        setupCategoryChips();
        applyFilters();
    }

    private void logout() {
        sessionManager.clearSession();
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        finish();
    }

    private void applyFilters() {
        String query = searchInput.getText().toString().toLowerCase().trim();
        List<Product> filtered = new ArrayList<>();
        for (Product product : allProducts) {
            if (!query.isEmpty()
                    && !product.getName().toLowerCase().contains(query)) {
                continue;
            }
            if ("Combos".equals(currentCategory) && !COMBOS.contains(product.getId())) {
                continue;
            }
            if ("Sliders".equals(currentCategory) && !SLIDERS.contains(product.getId())) {
                continue;
            }
            if (favouritesOnly && !product.isFavourite()) {
                continue;
            }
            filtered.add(product);
        }
        adapter.updateProducts(filtered);
    }
}
