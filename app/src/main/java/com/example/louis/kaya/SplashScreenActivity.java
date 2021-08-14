package com.example.louis.kaya;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

public class SplashScreenActivity extends AppCompatActivity {

    boolean already_launched;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);

        this.setTitle("     The Ultimate Courier Platform");
        SharedPreferences sharedPreferences = getSharedPreferences("StartActivity", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        already_launched = sharedPreferences.getBoolean("launched", true);

        if (SignUpSuccess.checker == 0) {

            editor.putBoolean("launched", false);

            Thread startApp = new Thread() {
                @Override
                public void run() {
                    try {
                        sleep(3000);
                        startActivity(new Intent(getApplicationContext(), IntroActivity.class));
                        finish();
                    } catch (InterruptedException e) {
                        Toast.makeText(SplashScreenActivity.this, "Failed to start application", Toast.LENGTH_SHORT).show();
                    }
                }
            };

            startApp.start();
            editor.apply();

        }
        else if (SignUpSuccess.checker == 1)
        {
            Thread startApp = new Thread() {
                @Override
                public void run() {
                    try {
                        sleep(800);
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                    } catch (InterruptedException e) {
                        Toast.makeText(SplashScreenActivity.this, "Failed to start application", Toast.LENGTH_SHORT).show();
                    }
                }
            };

            startApp.start();
        }
    }
}
