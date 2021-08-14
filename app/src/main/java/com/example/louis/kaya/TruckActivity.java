package com.example.louis.kaya;

import android.*;
import android.Manifest;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.security.Permission;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class
TruckActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    //DECLARING XML OBJECTS
    TextToSpeech textToSpeech;
    ImageView productImage;
    Button deliver_Request;
    CardView find_agent;
    FloatingActionButton takePicBtn;
    EditText productName, productDesc, senderName, senderPhone, receiverName, receiverNumber;
    String[] splitter;
    String prod_Name, prod_desc, category, sender, sender_phone, receiver, receiver_no, pickup, dest, code, finalCode ;
    LinearLayout innerLayout;
    LinearLayout outerLayout;
    Handler handler;
    private BroadcastReceiver broadcastReceiver;
    SessionManagement sessionManagement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_truck);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Checking if there is internet connectivity
        sessionManagement = new SessionManagement(this);
        sessionManagement.isNetworkAvailable();

        //DISPLAYING THE BACK ICON
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        //INITIALIZING TEXT-TO-SPEECH OBJECT
        textToSpeech = new TextToSpeech(this, this);

        //INSTANTIATING THE XML OBJECTS
        productImage = (ImageView)findViewById(R.id.itemImage);
        productName = (EditText) findViewById(R.id.et_prodName);
        productDesc = (EditText) findViewById(R.id.et_prodDesc);
        senderName = (EditText) findViewById(R.id.et_senderName);
        senderPhone = (EditText) findViewById(R.id.et_senderPhone);
        receiverName = (EditText) findViewById(R.id.et_receiverName);
        receiverNumber = (EditText) findViewById(R.id.et_receiverNumber);

        find_agent = (CardView) findViewById(R.id.findAgent);
        deliver_Request = (Button) findViewById(R.id.btn_Request_Delivery);

        innerLayout = (LinearLayout)findViewById(R.id.innerLinearLayout);

        handler = new Handler();

        //Declaring all the floating action buttons
        final FloatingActionButton startBtn = (FloatingActionButton) findViewById(R.id.selectedSitefab1);
        final FloatingActionButton stopBtn = (FloatingActionButton) findViewById(R.id.selectedSitefab2);
        final FloatingActionButton helpBtn = (FloatingActionButton) findViewById(R.id.selectedSitefab3);
        final FloatingActionButton showBtn = (FloatingActionButton) findViewById(R.id.selectedSitefab4);
        final FloatingActionButton hideBtn = (FloatingActionButton) findViewById(R.id.selectedSitefab5);

        startBtn.setVisibility(View.INVISIBLE);
        stopBtn.setVisibility(View.INVISIBLE);
        helpBtn.setVisibility(View.INVISIBLE);
        hideBtn.setVisibility(View.INVISIBLE);

        showBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startBtn.setVisibility(View.VISIBLE);
                stopBtn.setVisibility(View.VISIBLE);
                helpBtn.setVisibility(View.VISIBLE);
                showBtn.setVisibility(View.INVISIBLE);
                hideBtn.setVisibility(View.VISIBLE);

            }
        });

        hideBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startBtn.setVisibility(View.INVISIBLE);
                stopBtn.setVisibility(View.INVISIBLE);
                helpBtn.setVisibility(View.INVISIBLE);
                hideBtn.setVisibility(View.INVISIBLE);
                showBtn.setVisibility(View.VISIBLE);

            }
        });

        //GETTING THE ADDRESSES
        Intent getPickUpAddress = getIntent();
        pickup = getPickUpAddress.getStringExtra("PICKUP_POINT");
        Intent getDestinationAddress = getIntent();
        dest = getDestinationAddress.getStringExtra("DESTINATION");
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (broadcastReceiver==null)
        {
            broadcastReceiver=new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {

                    Toast.makeText(TruckActivity.this, "Cordinates = " + intent.getExtras().get("cordinates"), Toast.LENGTH_SHORT).show();
                    Log.i("CORDINATES = ", String.valueOf(intent.getExtras().get("cordinates")));
                }
            };
        }
        registerReceiver(broadcastReceiver, new IntentFilter("location_data"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (broadcastReceiver!=null)
        {
            unregisterReceiver(broadcastReceiver);
        }
    }

    //Setting the TOP BACK ICON to return user to MAIN ACTIVITY when clicked on
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(this, MainActivity.class));
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //TAKING THE USER BACK TO MAIN ACTIVITY WHEN THEY CLICK ON THE DEFAULT BACK BUTTON
    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
        super.onBackPressed();
        this.finish();
    }

    /*ACTION FOR TAKING PICTURE WITH SYSTEM CAMERA
    public void TAKE_PIC(View view) {

        Intent takePic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePic, CAMERA_CODE);
    }

    //setting imageView TO THE PICTURE TAKEN BY CAMERA
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == CAMERA_CODE && resultCode == RESULT_OK)
        {
            itemImageCardView.setVisibility(View.VISIBLE);
            deletePicBtn.setVisibility(View.VISIBLE);
            Bitmap image_of_item = (Bitmap)data.getExtras().get("data");
            productImage.setImageBitmap(image_of_item);

            Toast.makeText(TruckActivity.this, "Image Successfully Taken", Toast.LENGTH_LONG).show();
        }
    }
*/
    //ACTION FOR INITIATING FINAL DELIVERY REQUEST
    @TargetApi(Build.VERSION_CODES.N)
    public void MAKE_REQUEST(View view) {

        //GETTING VALUES FROM USERS
        sender = senderName.getText().toString();
        prod_Name = productName.getText().toString();
        prod_desc = productDesc.getText().toString();
        //category = spinner.getSelectedItem().toString();
        sender_phone = senderPhone.getText().toString();
        receiver = receiverName.getText().toString();
        receiver_no = receiverNumber.getText().toString();
        code = UUID.randomUUID().toString();
        splitter = code.split("-");
        finalCode = splitter[0];

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyMMdd");
        final String date = dateFormat.format(new Date());

        AlertDialog.Builder alertDelivery = new AlertDialog.Builder(this);
        alertDelivery.setTitle(Html.fromHtml("<font color='#2a3556'><b>CONFIRM DELIVERY</b></font>"));
        alertDelivery.setMessage(Html.fromHtml("<font color='#a97f00'><i><u>Please check that the details provided are correct then proceed</u></i></font><br><br>" +
                "<b>Product Name:   </b>" + prod_Name + "<br><br>" +
                "<b>Product Description:   </b><br>"+ prod_desc + "<br><br>" +
                "<b>Sender Name:   </b>" + sender + "<br><br>" +
                "<b>Receiver Name:   </b>" + receiver + "<br><br>" +
                "<b>Receiver Phone Number:   </b>" + receiver_no + "<br><br>" +
                "<b>Pick up Point:   </b>" + pickup + "<br><br>" +
                "<b>Destination:   </b>" + dest + "<br><br><br>" +
                "<center><b>USER CODE = </b>" + finalCode + date + "</center><br>")
        );

        alertDelivery.setPositiveButton("PROCEED", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (prod_Name.isEmpty()||prod_desc.isEmpty()||category.equals("--select a category--")||sender.isEmpty()||receiver.isEmpty()||pickup.isEmpty()||dest.isEmpty())
                {
                    Toast.makeText(TruckActivity.this, "Please complete all fields", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    final ProgressDialog send = new ProgressDialog(TruckActivity.this);
                    send.setMessage("Sending request details...");
                    send.show();
                    send.setCancelable(true);
                    send.setCanceledOnTouchOutside(true);
                    deliver_Request.setText("Sending...");

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            send.cancel();
                            deliver_Request.setText("REQUEST DELIVERY");
                            takePicBtn.setVisibility(View.GONE);
                            innerLayout.setVisibility(View.GONE);
                            outerLayout.setVisibility(View.VISIBLE);
                        }
                    }, 5000);
                }
            }
        });
        alertDelivery.setNeutralButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(TruckActivity.this, "Request Cancelled", Toast.LENGTH_SHORT).show();
            }
        });
        alertDelivery.show();

    }

    //PROGRESS DIALOG
    public void FIND_AGENT(View view) {
        Intent open = new Intent(TruckActivity.this, PlacePickerActivity.class);
        open.putExtra("DELIVERY_TYPE", "3");
        startActivity(open);
    }

    public void CLOSE_SESSION(View view) {
        startActivity(new Intent(this, MainActivity.class));
        this.finish();
    }

    public void START_NEW_SESION(View view) {
        startActivity(new Intent(this, TruckActivity.class));
        this.finish();
    }

    public void ABOUT_DELIVERY(View view) {
        startActivity(new Intent(this, DeliveryPageInfo.class));
    }

    public void MAKE_PAYMENT(final View view) {

        AlertDialog.Builder alertpayment = new AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();
        View paymentDialog = inflater.inflate(R.layout.payment_page_1, null);
        alertpayment.setView(paymentDialog);
        final EditText networkProvider = (EditText)paymentDialog.findViewById(R.id.network_provider);

        alertpayment.setIcon(R.drawable.ic_network_check_black_24dp);
        alertpayment.setTitle(Html.fromHtml("<font color='#2a3556'>Select A Network Provider</font color>"));
        alertpayment.setMessage("\n1. MTN \n2. TIGO \n3. VODAFONE");
        alertpayment.setNeutralButton("Cancel", null);
        alertpayment.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                Log.i("CALL INTENT 1", "Condition started");
                if (networkProvider.getText().toString().equals("1")) {

                    Log.i("CALL INTENT 2", "Condition is likely to be met");
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse(Uri.parse("tel:" + "*170") + Uri.encode("#")));
                    {
                        startActivity(intent);
                    }
                    Log.i("CALL INTENT 3", "Ussd call was placed");

                } else if (networkProvider.getText().toString().equals("2")) {
                    Snackbar.make(view, "TIGO has been selected", Snackbar.LENGTH_SHORT).show();

                    Log.i("CALL INTENT 2", "Condition is likely to be met");
                    Intent intent1 = new Intent(Intent.ACTION_CALL);

                    Log.i("CALL INTENT 3", "Ussd call was placed");
                    intent1.setData(Uri.parse(Uri.parse("tel:" + "*170") + Uri.encode("#")));
                    {
                        if (ActivityCompat.checkSelfPermission(TruckActivity.this,
                                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        startActivity(intent1);
                    }
                    Log.i("CALL INTENT 3", "Ussd call was placed");

                } else if (networkProvider.getText().toString().equals("3")) {
                    Snackbar.make(view, "VODAFONE has been selected", Snackbar.LENGTH_SHORT).show();
                } else {
                    Snackbar.make(view, "Wrong Choice", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
        alertpayment.show();
    }

    @Override
    public void onInit(int status) {

        if (status == TextToSpeech.SUCCESS) {
            int result = textToSpeech.setLanguage(Locale.ENGLISH);
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Language Data Error");
                builder.setMessage("No English TTS language data. Please download a Language Pack");
                builder.setNegativeButton("Ignore", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent openSetting = new Intent(Settings.ACTION_LOCALE_SETTINGS);
                        startActivity(openSetting);
                    }
                });

                builder.show();
            }
        }
    }

    public void START_SPEAKING(View view) {
        textToSpeech.speak("Welcome, to our truck delivery service! Please provide your delivery information in the available fields. \n" +
                "Scroll down to add the pick-up point and destination of your delivery. \n" +
                "Finally, select a payment type to pay for your delivery transaction. \n" +
                "You can select the 'HELP' button for a visual explanation", TextToSpeech.QUEUE_ADD, null);
    }

    public void STOP_SPEAKING(View view) {
        textToSpeech.stop();
    }
}
