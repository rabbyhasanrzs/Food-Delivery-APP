package com.example.fooddelivery;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        DatabaseHelper db = new DatabaseHelper(this);
        EditText etEmail = findViewById(R.id.etEmail);
        EditText etPassword = findViewById(R.id.etPassword);

        findViewById(R.id.btnLogin).setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String pass = etPassword.getText().toString();

            if (email.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Email ar password dao", Toast.LENGTH_SHORT).show();
                return;
            }
            String name = db.loginUser(email, pass);
            if (name != null) {
                new SessionManager(this).login(name, email);
                startActivity(new Intent(this, HomeActivity.class));
                finishAffinity();
            } else {
                Toast.makeText(this, "Email ba password vul", Toast.LENGTH_SHORT).show();
            }
        });

        ((TextView) findViewById(R.id.tvSignup)).setOnClickListener(v -> finish());
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
    }
}
