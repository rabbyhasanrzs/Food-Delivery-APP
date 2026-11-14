package com.example.fooddelivery;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // 2 second por next screen e jabe
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            SessionManager session = new SessionManager(this);
            Class<?> next = session.isLoggedIn() ? HomeActivity.class : SignupActivity.class;
            startActivity(new Intent(this, next));
            finish(); // splash e back korle jate na ase
        }, 2000);
    }
}
