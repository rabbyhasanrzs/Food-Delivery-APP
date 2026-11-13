package com.example.fooddelivery;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fooddelivery.utils.Constants;
import com.example.fooddelivery.utils.SessionManager;

/**
 * Phase 1 - Splash screen (blueprint Section 4, screen 1).
 *
 * <p>Shows the RabbY brand on the red-to-pink gradient for about
 * 2 seconds, then routes by login state. The session is read through
 * {@link SessionManager}, as specified in the blueprint.</p>
 *
 * <p>Phase 2 will route signed-out users to LoginActivity. Until then,
 * MainActivity (Home) is the destination for every user, so the app
 * still runs end-to-end after Phase 1. Nothing is merged to main
 * without review.</p>
 */
public class SplashActivity extends AppCompatActivity {

    private final Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        handler.postDelayed(() -> {
            SessionManager sessionManager = new SessionManager(SplashActivity.this);
            // The login state is read here on purpose, even though both
            // routes currently lead to MainActivity (Home).
            // Phase 2: if (!sessionManager.isLoggedIn()) start LoginActivity.
            boolean isLoggedIn = sessionManager.isLoggedIn();

            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            intent.putExtra(Constants.EXTRA_WAS_LOGGED_IN, isLoggedIn);
            startActivity(intent);
            finish();
        }, Constants.SPLASH_DELAY_MS);
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}
