package com.example.louis.kaya;

import android.*;
import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Handler handler;
    private String check;
    ProgressDialog progressDialog;
    Intent open;
    SessionManagement sessionManagement;
    public FloatingActionButton fab1, fab2, fab3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Checking if there is internet connectivity
        sessionManagement = new SessionManagement(this);
        sessionManagement.isNetworkAvailable();

        progressDialog = new ProgressDialog(this);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayShowTitleEnabled(true);

        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(getLayoutInflater().inflate(R.layout.main_custom_title, null),
                new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER));
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        fab1 = (FloatingActionButton) findViewById(R.id.fab1);
        fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab3 = (FloatingActionButton) findViewById(R.id.fab3);
        Button toolBarBtn_1 = (Button) findViewById(R.id.button1);
        Button toolBarBtn_2 = (Button) findViewById(R.id.button2);
        Button toolBarBtn_3 = (Button) findViewById(R.id.button3);
        final TextView vanText = (TextView) findViewById(R.id.textViewVan);
        final TextView motorText = (TextView) findViewById(R.id.textViewMotor);
        final TextView truckText = (TextView) findViewById(R.id.textViewTruck);
        Button showBtn = (Button) findViewById(R.id.buttonShow);

        handler = new Handler();

        toolBarBtn_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check = "services";
                Intent openServices = new Intent(MainActivity.this, AppMenu.class);
                openServices.putExtra("checker", check);
                startActivity(openServices);
                finish();
            }
        });

        toolBarBtn_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check = "charges";
                Intent openCharges = new Intent(MainActivity.this, AppMenu.class);
                openCharges.putExtra("checker", check);
                startActivity(openCharges);
                finish();
            }
        });

        toolBarBtn_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check = "about";
                Intent openAbout = new Intent(MainActivity.this, AppMenu.class);
                openAbout.putExtra("checker", check);
                startActivity(openAbout);
                finish();
            }
        });

        //DISPLAYING THE DELIVERY MODE BUTTONS
        Animation show_fab1 = AnimationUtils.loadAnimation(MainActivity.this, R.anim.fab1_show);
        RelativeLayout.LayoutParams layoutParams1 = (RelativeLayout.LayoutParams) fab1.getLayoutParams();
        layoutParams1.leftMargin += fab1.getHeight() * 0.1;
        layoutParams1.rightMargin += fab1.getWidth() * 3.0;
        fab1.setLayoutParams(layoutParams1);
        fab1.setAnimation(show_fab1);
        truckText.setAnimation(show_fab1);
        fab1.setClickable(true);

        Animation show_fab2 = AnimationUtils.loadAnimation(MainActivity.this, R.anim.fab2_show);
        RelativeLayout.LayoutParams layoutParams2 = (RelativeLayout.LayoutParams) fab2.getLayoutParams();
        layoutParams2.leftMargin += fab2.getHeight() * 0.1;
        layoutParams2.rightMargin += fab2.getWidth() * 3.0;
        fab2.setLayoutParams(layoutParams2);
        fab2.setAnimation(show_fab2);
        vanText.setAnimation(show_fab2);
        fab2.setClickable(true);

        Animation show_fab3 = AnimationUtils.loadAnimation(MainActivity.this, R.anim.fab3_show);
        RelativeLayout.LayoutParams layoutParams3 = (RelativeLayout.LayoutParams) fab3.getLayoutParams();
        layoutParams3.leftMargin += fab3.getHeight() * 0.1;
        layoutParams3.rightMargin += fab3.getWidth() * 3.0;
        fab3.setLayoutParams(layoutParams3);
        fab3.setAnimation(show_fab3);
        motorText.setAnimation(show_fab3);
        fab3.setClickable(true);

        //HIDING THE DELIVERY MODE BUTTONS
        showBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                truckText.setVisibility(View.INVISIBLE);
                motorText.setVisibility(View.INVISIBLE);
                vanText.setVisibility(View.INVISIBLE);
                Animation hide_fab1 = AnimationUtils.loadAnimation(MainActivity.this, R.anim.fab1_hide);
                RelativeLayout.LayoutParams layoutParams1 = (RelativeLayout.LayoutParams) fab1.getLayoutParams();
                layoutParams1.leftMargin -= fab1.getHeight() * 0.1;
                layoutParams1.rightMargin -= fab1.getWidth() * 3.0;
                fab1.setLayoutParams(layoutParams1);
                fab1.setAnimation(hide_fab1);
                fab1.setClickable(false);

                Animation hide_fab2 = AnimationUtils.loadAnimation(MainActivity.this, R.anim.fab2_hide);
                RelativeLayout.LayoutParams layoutParams2 = (RelativeLayout.LayoutParams) fab2.getLayoutParams();
                layoutParams2.leftMargin -= fab2.getHeight() * 0.1;
                layoutParams2.rightMargin -= fab2.getWidth() * 3.0;
                fab2.setLayoutParams(layoutParams2);
                fab2.setAnimation(hide_fab2);
                fab2.setClickable(false);

                Animation hide_fab3 = AnimationUtils.loadAnimation(MainActivity.this, R.anim.fab3_hide);
                RelativeLayout.LayoutParams layoutParams3 = (RelativeLayout.LayoutParams) fab3.getLayoutParams();
                layoutParams3.leftMargin -= fab3.getHeight() * 0.1;
                layoutParams3.rightMargin -= fab3.getWidth() * 3.0;
                fab3.setLayoutParams(layoutParams3);
                fab3.setAnimation(hide_fab3);
                fab3.setClickable(false);

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent open = new Intent(MainActivity.this, DemoActivity.class);
                        startActivity(open);
                        overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
                        finish();
                    }
                }, 700);
            }
        });

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        //Checking if USER NETWORK IS AVAILABLE AND LOCATION SERVICE IS ENABLED
        boolean locationEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (!locationEnabled) {
            AlertDialog.Builder alertTurnOnLocation = new AlertDialog.Builder(this);
            alertTurnOnLocation.setIcon(R.drawable.ic_my_location_black_24dp);
            alertTurnOnLocation.setTitle("GPS Permission Required");
            alertTurnOnLocation.setMessage("Turn on your Location Service");
            alertTurnOnLocation.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                }
            });
            alertTurnOnLocation.setNegativeButton("Deny", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(MainActivity.this, "Enable your Location for KAYA", Toast.LENGTH_LONG).show();
                }
            });
            alertTurnOnLocation.show();
        }

    }

    public void TRUCK(View view) {

        open = new Intent(MainActivity.this, TruckActivity.class);
        startActivity(open);
        overridePendingTransition(R.anim.slide_in_right, android.R.anim.slide_out_right);
        finish();
    }

    public void MOTORCYCLE(View view) {

        open = new Intent(MainActivity.this, MotorActivity.class);
        startActivity(open);
        overridePendingTransition(R.anim.slide_in_right, android.R.anim.slide_out_right);
        finish();
    }


    public void VAN(View view) {

        open = new Intent(MainActivity.this, VanActivity.class);
        startActivity(open);
        overridePendingTransition(R.anim.slide_in_right, android.R.anim.slide_out_right);
        finish();

    }
}
