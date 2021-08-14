package com.example.louis.kaya;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class PaymentSuccess extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_success);
    }

    public void START_NEW_SESION(View view) {
        startActivity(new Intent(this, MotorActivity.class));
        finish();
    }

    public void CLOSE_SESSION(View view) {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
