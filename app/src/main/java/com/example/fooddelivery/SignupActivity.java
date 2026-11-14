package com.example.fooddelivery;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.util.Patterns;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SignupActivity extends AppCompatActivity {

    private EditText etName, etEmail, etPassword, etConfirm;
    private CheckBox cbTerms;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        db = new DatabaseHelper(this);
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirm = findViewById(R.id.etConfirm);
        cbTerms = findViewById(R.id.cbTerms);
        Button btnSignup = findViewById(R.id.btnSignup);
        TextView tvLogin = findViewById(R.id.tvLogin);

        btnSignup.setOnClickListener(v -> signup());
        tvLogin.setOnClickListener(v -> startActivity(new Intent(this, LoginActivity.class)));

        // Social icons display only (user-er request): tap korle toast,
        // kono Google/Apple/Facebook login nei - local login only.
        View.OnClickListener socialUnavailable = v -> Toast.makeText(this, "Social login is not available in this demo", Toast.LENGTH_SHORT).show();
        findViewById(R.id.social_google).setOnClickListener(socialUnavailable);
        findViewById(R.id.social_apple).setOnClickListener(socialUnavailable);
        findViewById(R.id.social_facebook).setOnClickListener(socialUnavailable);
    }

    private void signup() {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String pass = etPassword.getText().toString();
        String confirm = etConfirm.getText().toString();

        // ---- Validation ----
        if (name.isEmpty()) { etName.setError("Name dao"); return; }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) { etEmail.setError("Valid email dao"); return; }
        if (pass.length() < 6) { etPassword.setError("Minimum 6 character"); return; }
        if (!pass.equals(confirm)) { etConfirm.setError("Password mile nai"); return; }
        if (!cbTerms.isChecked()) {
            Toast.makeText(this, "Terms & Conditions accept korte hobe", Toast.LENGTH_SHORT).show();
            return;
        }

        // ---- Database e save ----
        if (db.registerUser(name, email, pass)) {
            new SessionManager(this).login(name, email);
            Toast.makeText(this, "Account created!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, HomeActivity.class));
            finishAffinity(); // purono sob screen bondho
        } else {
            etEmail.setError("Ei email diye account ache");
        }
    }
}
