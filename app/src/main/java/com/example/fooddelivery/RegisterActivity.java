package com.example.fooddelivery;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fooddelivery.database.DatabaseHelper;
import com.example.fooddelivery.utils.Constants;
import com.example.fooddelivery.utils.ProductImages;
import com.example.fooddelivery.utils.SessionManager;

/**
 * Minimal Registration screen - local accounts only.
 * As designed, minus the social buttons (blueprint Section 4).
 */
public class RegisterActivity extends AppCompatActivity {

    private EditText nameInput;
    private EditText emailInput;
    private EditText passwordInput;
    private EditText confirmPasswordInput;
    private CheckBox termsCheckbox;
    private DatabaseHelper db;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        db = new DatabaseHelper(this);
        sessionManager = new SessionManager(this);

        ProductImages.setProductImage(
                findViewById(R.id.register_hero), "img_splash_burger_photo");

        nameInput = findViewById(R.id.register_name);
        emailInput = findViewById(R.id.register_email);
        passwordInput = findViewById(R.id.register_password);
        confirmPasswordInput = findViewById(R.id.register_confirm_password);
        termsCheckbox = findViewById(R.id.register_terms);
        Button signUpButton = findViewById(R.id.register_button);
        TextView loginLink = findViewById(R.id.register_login_link);

        signUpButton.setOnClickListener(v -> register());
        loginLink.setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();
        });
    }

    private void register() {
        String name = nameInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString();
        String confirmPassword = confirmPasswordInput.getText().toString();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!email.contains("@")) {
            Toast.makeText(this, "Please enter a valid email", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.length() < Constants.MIN_PASSWORD_LENGTH) {
            Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!termsCheckbox.isChecked()) {
            Toast.makeText(this, "Please accept the Terms and Conditions", Toast.LENGTH_SHORT).show();
            return;
        }
        if (db.getUserByEmail(email) != null) {
            Toast.makeText(this, "An account with this email already exists", Toast.LENGTH_SHORT).show();
            return;
        }

        long userId = db.insertUser(name, email, password);
        if (userId == -1) {
            Toast.makeText(this, "Registration failed, please try again", Toast.LENGTH_SHORT).show();
            return;
        }

        sessionManager.saveSession(userId, name, email);
        Toast.makeText(this, "Account created", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(RegisterActivity.this, MainActivity.class));
        finish();
    }
}
