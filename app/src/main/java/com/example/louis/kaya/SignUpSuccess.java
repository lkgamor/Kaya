package com.example.louis.kaya;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */

public class SignUpSuccess extends AppCompatActivity {

    String Username, Email;
    TextView welcomeText;
    public static int checker = 0;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_success);

        Intent getUsername = getIntent();
        Intent getEmail = getIntent();
        Username = getUsername.getStringExtra("USERNAME");
        Email = getEmail.getStringExtra("EMAIL");
        welcomeText = (TextView) findViewById(R.id.welcomeText);
        Button startSession = (Button) findViewById(R.id.startKaya);

        Username = Username.trim();
        welcomeText.setText("Hello " + Username);

        Intent sendEmail = new Intent(Intent.ACTION_SEND);
        sendEmail.setData(Uri.parse("mailto:"));
        sendEmail.setType("text/plain");
        sendEmail.putExtra(Intent.EXTRA_EMAIL, Email);
        sendEmail.putExtra(Intent.EXTRA_SUBJECT, "YOUR KAYA USER CODE");
        startActivity(sendEmail);

        sharedPreferences = getSharedPreferences("SIGNED_UP", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        checker = sharedPreferences.getInt("SIGNED_UP_STATUS", 1);
        editor.apply();

        startSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(SignUpSuccess.this, MainActivity.class);

                // Closing all the Activities || Add new Flag to start new Activity
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, android.R.anim.slide_out_right);
                finish();
            }
        });
    }
}
