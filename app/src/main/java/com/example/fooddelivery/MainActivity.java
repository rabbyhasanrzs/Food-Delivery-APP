package com.example.fooddelivery;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
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

import java.util.ArrayList;
import java.util.List;

/**
 * Home screen (blueprint Section 4, screen 4).
 * This is the existing MainActivity file, kept and repurposed as
 * the Home host. It shows the demo menu in a 2-column grid, with
 * search and favourite hearts. Tapping a card opens Product Details.
 */
public class MainActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private SessionManager sessionManager;
    private ProductAdapter adapter;
    private List<Product> allProducts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        db = new DatabaseHelper(this);
        sessionManager = new SessionManager(this);
        ProductCatalog.seedIfEmpty(db);
        allProducts = db.getAllProducts();

        TextView greetingText = findViewById(R.id.home_greeting);
        String name = sessionManager.getUserName();
        greetingText.setText(name.isEmpty()
                ? getString(R.string.home_greeting_guest)
                : getString(R.string.home_greeting, name));

        RecyclerView recyclerView = findViewById(R.id.home_product_list);
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
                    adapter.notifyDataSetChanged();
                });
        recyclerView.setAdapter(adapter);

        EditText searchInput = findViewById(R.id.home_search);
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterProducts(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        Button logoutButton = findViewById(R.id.home_logout_button);
        logoutButton.setOnClickListener(v -> {
            sessionManager.clearSession();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh favourites after returning from Product Details.
        allProducts = db.getAllProducts();
        adapter.updateProducts(new ArrayList<>(allProducts));
    }

    private void filterProducts(String query) {
        String lowerQuery = query.toLowerCase().trim();
        List<Product> filtered = new ArrayList<>();
        for (Product product : allProducts) {
            if (product.getName().toLowerCase().contains(lowerQuery)) {
                filtered.add(product);
            }
        }
        adapter.updateProducts(filtered);
    }
}
