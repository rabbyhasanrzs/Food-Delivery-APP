package com.example.fooddelivery;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fooddelivery.database.DatabaseHelper;
import com.example.fooddelivery.models.User;
import com.example.fooddelivery.utils.ProductImages;
import com.example.fooddelivery.utils.SessionManager;

/**
 * Minimal Login screen - local accounts only, no social login.
 * New design in the RabbY style (blueprint Section 4).
 */
public class LoginActivity extends AppCompatActivity {

    private EditText emailInput;
    private EditText passwordInput;
    private DatabaseHelper db;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = new DatabaseHelper(this);
        sessionManager = new SessionManager(this);

        ProductImages.setProductImage(
                findViewById(R.id.login_hero), "img_splash_burger_photo");

        emailInput = findViewById(R.id.login_email);
        passwordInput = findViewById(R.id.login_password);
        Button loginButton = findViewById(R.id.login_button);
        TextView registerLink = findViewById(R.id.login_register_link);

        loginButton.setOnClickListener(v -> login());
        registerLink.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });
    }

    private void login() {
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!email.contains("@")) {
            Toast.makeText(this, "Please enter a valid email", Toast.LENGTH_SHORT).show();
            return;
        }

        User user = db.getUserByEmail(email);
        if (user == null || !user.getPassword().equals(password)) {
            Toast.makeText(this, "Wrong email or password", Toast.LENGTH_SHORT).show();
            return;
        }

        sessionManager.saveSession(user.getId(), user.getFullName(), user.getEmail());
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }
}
