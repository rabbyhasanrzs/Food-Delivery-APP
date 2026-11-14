package com.example.fooddelivery;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.ListenerRegistration;

// Order tracking screen: Firestore er order doc ta live shone.
// Admin (Firebase Console theke) status bodle dile ekhanei update hobe:
// Order Placed -> Preparing -> Out for Delivery -> Delivered
public class OrderTrackingActivity extends AppCompatActivity {

    private static final String[] STEPS = {
            FirestoreHelper.STATUS_PLACED,
            FirestoreHelper.STATUS_PREPARING,
            FirestoreHelper.STATUS_OUT,
            FirestoreHelper.STATUS_DELIVERED
    };
    private final int[] stepTextIds = {R.id.tvStep1, R.id.tvStep2, R.id.tvStep3, R.id.tvStep4};
    private final int[] stepDotIds = {R.id.dot1, R.id.dot2, R.id.dot3, R.id.dot4};

    private ListenerRegistration listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_tracking);

        String orderId = getIntent().getStringExtra("order_id");
        ((TextView) findViewById(R.id.tvOrderId)).setText(orderId != null ? orderId : "-");
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        if (orderId == null) {
            ((TextView) findViewById(R.id.tvStatus)).setText("Order id pawa jay nai");
            return;
        }
        // Live shona: Firestore e status bodle gelei screen update hobe
        listener = FirestoreHelper.listenOrderStatus(this, orderId, new FirestoreHelper.StatusCallback() {
            @Override public void onStatus(String status) { showStatus(status); }
            @Override public void onError(Exception e) {
                ((TextView) findViewById(R.id.tvStatus)).setText(
                        "Live tracking ekhon cholbe na — internet / Firebase setup check koro");
            }
        });
    }

    private void showStatus(String status) {
        int current = 0;
        for (int i = 0; i < STEPS.length; i++) {
            if (STEPS[i].equals(status)) { current = i; break; }
        }
        ((TextView) findViewById(R.id.tvStatus)).setText("Status: " + status);
        for (int i = 0; i < STEPS.length; i++) {
            TextView tv = findViewById(stepTextIds[i]);
            View dot = findViewById(stepDotIds[i]);
            if (i < current) { // hoye geche
                tv.setText("\u2713 " + STEPS[i]);
                tv.setTextColor(0xFF2E7D32);
                dot.setBackgroundResource(R.drawable.bg_circle_red);
            } else if (i == current) { // ekhon eita cholche
                tv.setText("\u25CF " + STEPS[i]);
                tv.setTextColor(0xFFF02D3A);
                dot.setBackgroundResource(R.drawable.bg_circle_red);
            } else { // baki ache
                tv.setText("\u25CB " + STEPS[i]);
                tv.setTextColor(0xFF777777);
                dot.setBackgroundResource(R.drawable.bg_chip_off);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (listener != null) listener.remove(); // listener bondho
    }
}
