package com.example.fooddelivery;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fooddelivery.utils.Constants;
import com.example.fooddelivery.utils.ProductImages;
import com.example.fooddelivery.utils.SessionManager;

/**
 * Phase 1 - Splash screen (blueprint Section 4, screen 1).
 *
 * <p>Shows the RabbY brand on the red-to-pink gradient for about
 * 2 seconds, then routes by login state. The session is read through
 * {@link SessionManager}, as specified in the blueprint.</p>
 *
 * <p>Signed-out users now go to LoginActivity (lab-final flow).
 * Signed-in users go to MainActivity (Home).</p>
 */
public class SplashActivity extends AppCompatActivity {

    private final Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Real burger photo when the JPG is present, placeholder otherwise.
        ProductImages.setProductImage(
                findViewById(R.id.splash_burger_image), "img_splash_burger_photo");

        handler.postDelayed(() -> {
            SessionManager sessionManager = new SessionManager(SplashActivity.this);
            boolean isLoggedIn = sessionManager.isLoggedIn();

            Intent intent;
            if (isLoggedIn) {
                intent = new Intent(SplashActivity.this, MainActivity.class);
                intent.putExtra(Constants.EXTRA_WAS_LOGGED_IN, true);
            } else {
                intent = new Intent(SplashActivity.this, LoginActivity.class);
            }
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
