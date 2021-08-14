package com.example.louis.kaya;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.StrictMode;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserSignUp extends AppCompatActivity {

    ProgressDialog progressDialog;
    EditText username, phone, email;
    LinearLayout outerLinearLayout, innerRelativeLayout;
    CardView cardViewHide, signInButton, skipButton;
    String Username, Email, Phone;
    InputStream inputStream;
    SessionManagement sessionManagement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_user_sign_up);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        sessionManagement = new SessionManagement(this);

        //Initializing objects

        cardViewHide = (CardView) findViewById(R.id.cardviewHide);
        username = (EditText) findViewById(R.id.username);
        email = (EditText) findViewById(R.id.email);
        phone = (EditText) findViewById(R.id.phone);
        innerRelativeLayout = (LinearLayout) findViewById(R.id.innerRelativeLayout);

        progressDialog = new ProgressDialog(UserSignUp.this);

        signInButton = (CardView) findViewById(R.id.sign_in_button);
        skipButton = (CardView) findViewById(R.id.skip_button);

    }

    private void PROGRESS_STOP() {
        progressDialog.cancel();
    }

    @TargetApi(Build.VERSION_CODES.N)
    public void REGISTER_USER(View view) {

        if (sessionManagement.isNetworkAvailable() == 1) {

            Username = username.getText().toString().trim();
            Email = email.getText().toString().trim();
            Phone = phone.getText().toString();
            //Checking for user inputs
            if (Username.isEmpty() || Email.isEmpty() || Phone.isEmpty()) {
                Snackbar.make(view, "Please fill out all fields", Snackbar.LENGTH_SHORT)
                        .setAction("OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                username.setText("");
                                phone.setText("");
                                email.setText("");
                            }
                        })
                        .setActionTextColor(getResources().getColor(R.color.colorPrimary))
                        .show();
            } else if (Phone.length() < 10) {
                phone.setError("Phone Number too short");
            } else if (Email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {
                email.setError("Invalid Email Address Format");
            } else {
                if (Phone.isEmpty() || Phone.startsWith("024") || Phone.startsWith("054") || Phone.startsWith("055") || Phone.startsWith("027") || Phone.startsWith("057") || Phone.startsWith("028") || Phone.startsWith("020") || Phone.startsWith("050") || Phone.startsWith("026") || Phone.startsWith("056") || Phone.startsWith("023")) {

                    ProgressDialog myProgressDialog = ProgressDialog.show(UserSignUp.this, "", "Processing sign up request...");
                    myProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

                    new Thread() {

                        public void run() {
                            try {

                                Thread.sleep(1000);
                            } catch (Exception e) {
                                Toast.makeText(UserSignUp.this, "Sign Up Failed", Toast.LENGTH_SHORT).show();
                            }

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    sessionManagement.createSignUpSession(Username, Email);

                                    innerRelativeLayout.setVisibility(View.GONE);
                                    cardViewHide.setVisibility(View.GONE);
                                    signInButton.setVisibility(View.GONE);
                                    skipButton.setVisibility(View.GONE);

                                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyy-MM-dd hh:mm:ss");
                                    final String date = dateFormat.format(new Date());

                                    List<NameValuePair> userDetails = new ArrayList<>();
                                    userDetails.add(new BasicNameValuePair("Username", Username));
                                    userDetails.add(new BasicNameValuePair("Phone", Phone));
                                    userDetails.add(new BasicNameValuePair("Email", Email));
                                    userDetails.add(new BasicNameValuePair("dateTime", date));

                                    HttpClient httpClient = new DefaultHttpClient();
                                    HttpPost httpPost = new HttpPost("https://travelghana.000webhostapp.com/KayaDBAccess/kaya_connect.php");
                                    try {
                                        httpPost.setEntity(new UrlEncodedFormEntity(userDetails));
                                    } catch (UnsupportedEncodingException e) {
                                        e.printStackTrace();
                                    }
                                    HttpResponse httpResponse = null;
                                    try {
                                        httpResponse = httpClient.execute(httpPost);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    HttpEntity httpEntity = null;
                                    if (httpResponse != null) {
                                        httpEntity = httpResponse.getEntity();
                                    }

                                    try {
                                        if (httpEntity != null) {
                                            inputStream = httpEntity.getContent();
                                        }
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                    try {
                                        if (inputStream != null) {

                                            //Saving user signup data in shared preferences
                                            inputStream.close();
                                            Intent i = new Intent(UserSignUp.this, SignUpSuccess.class);
                                            i.putExtra("USERNAME", Username);
                                            i.putExtra("EMAIL", Email);
                                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(i);
                                            PROGRESS_STOP();
                                            overridePendingTransition(R.anim.slide_in_right, android.R.anim.slide_out_right);
                                            finish();

                                        }
                                    } catch (IOException e) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(UserSignUp.this);
                                        builder.setIcon(R.drawable.kaya_logo);
                                        builder.setTitle("Sign up failure!");
                                        builder.setMessage("Sorry, your sign up failed. Please ensure that your device is connected to the internet then try again");
                                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                startActivity(new Intent(UserSignUp.this, UserSignUp.class));
                                                finish();
                                            }
                                        });
                                        builder.show();
                                    }
                                }

                            });
                            PROGRESS_STOP();
                        }
                    }.start();

                } else {

                    phone.setError("Invalid Phone Number Format");
                }
            }
        }
    }

    public void SKIP(View view) {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
