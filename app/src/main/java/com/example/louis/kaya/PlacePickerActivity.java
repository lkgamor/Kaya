package com.example.louis.kaya;

import android.content.Intent;
import android.location.Location;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

public class PlacePickerActivity extends AppCompatActivity {

    private static int PLACE_PICKER_REQUEST_PICKUP_POINT=1;
    private static int PLACE_PICKER_REQUEST_DESTINATION=2;
    String placeAddress, destinationAddress, deliveryType, OriginLatLong, DestinationLatLong;
    double Origin_Latitude, Origin_Longitude, Destination_Latitude, Destination_Longitude, DISTANCE;
    SessionManagement sessionManagement;

    //DECLARING XML OBJECTS
    TextView pickUpPoint, destination, distance, cost;
    RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_picker);

        //Checking if there is internet connectivity
        sessionManagement = new SessionManagement(this);
        sessionManagement.isNetworkAvailable();

        //DISPLAYING THE BACK ICON
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent getDeliveryType = getIntent();
        deliveryType = getDeliveryType.getStringExtra("DELIVERY_TYPE");

        //INSTANTIATING THE XML OBJECTS
        pickUpPoint = (TextView)findViewById(R.id.pickUpName);
        destination = (TextView)findViewById(R.id.destinationName);
        distance = (TextView)findViewById(R.id.distance);
        cost = (TextView)findViewById(R.id.cost);

        PlacePicker.IntentBuilder placePickerbuilder = new PlacePicker.IntentBuilder();
        Intent placeIntent;

        try {

            placeIntent = placePickerbuilder.build(this);
            startActivityForResult(placeIntent, PLACE_PICKER_REQUEST_PICKUP_POINT);
            Toast.makeText(PlacePickerActivity.this, "Select the location of your product", Toast.LENGTH_LONG).show();

        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {

            e.printStackTrace();

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

    //setting DATA COLLECTED BY PLACEPICKER
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        //WORKING WITH COORDINATED FROM THE SELECTED PICKUP POINT
        if (requestCode==PLACE_PICKER_REQUEST_PICKUP_POINT)
        {
            if (resultCode==RESULT_OK)
            {
                Place place = PlacePicker.getPlace(this, data);
                placeAddress = String.format("%s",place.getName());
                pickUpPoint.setText(placeAddress);
                pickUpPoint.setTextColor(getResources().getColor(R.color.colorPrimary));

                OriginLatLong = String.format("%s", place.getLatLng());

                if (destination.getText().toString().equals(""))
                {
                    String[] Splitter1 = OriginLatLong.split(":");
                    String LatLongSet1 = Splitter1[1];
                    String[] Splitter2 = LatLongSet1.split(",");
                    String LatitudeTemp = Splitter2[0];
                    String LongitudeTemp = Splitter2[1];

                    Origin_Latitude = Double.parseDouble(LatitudeTemp.substring(2));
                    int longitudeLength = LongitudeTemp.length();
                    Origin_Longitude = Double.parseDouble(LongitudeTemp.substring(0, longitudeLength - 1));

                    distance.setText("Select a destination...");
                }
                else
                {
                    distance.setText("Calculating Distance...");
                    final float results[] = new float[10];
                    Location.distanceBetween(Origin_Latitude, Origin_Longitude, Destination_Latitude, Destination_Longitude, results);

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            distance.setText(results[0] + " Km");
                            distance.setTextColor(getResources().getColor(R.color.colorAccent));

                        }
                    },2000);
                }
                if (OriginLatLong.equals("null"))
                {
                    AlertDialog.Builder builder= new AlertDialog.Builder(this);
                    builder.setIcon(R.drawable.ic_warning_black_24dp);
                    builder.setTitle("Attention");
                    builder.setMessage("No Coordinates Found for this PickUp Point. Please select a more precise location");
                    builder.setPositiveButton("OK", null);
                    builder.show();
                }
            }
        }

        //WORKING WITH COORDINATED FROM THE SELECTED DESTINATION
        if (requestCode==PLACE_PICKER_REQUEST_DESTINATION)
        {
            if (resultCode==RESULT_OK)
            {
                Place place = PlacePicker.getPlace(this, data);
                destinationAddress = String.format("%s", place.getName());
                destination.setText(destinationAddress);
                destination.setTextColor(getResources().getColor(R.color.colorPrimary));

                final float results[] = new float[10];
                DestinationLatLong = String.format("%s", place.getLatLng());

                if (pickUpPoint.getText().toString().equals(""))
                {
                    String[] Splitter1 = DestinationLatLong.split(":");
                    String LatLongSet1 = Splitter1[1];
                    String[] Splitter2 = LatLongSet1.split(",");
                    String LatitudeTemp = Splitter2[0];
                    String LongitudeTemp = Splitter2[1];

                    Destination_Latitude = Double.parseDouble(LatitudeTemp.substring(2));
                    int longitudeLength = LongitudeTemp.length();
                    Destination_Longitude = Double.parseDouble(LongitudeTemp.substring(0, longitudeLength - 1));

                    distance.setText("Select a Pickup Point...");
                }
                else
                {
                    distance.setText("Calculating Distance...");
                    Location.distanceBetween(Origin_Latitude, Origin_Longitude, Destination_Latitude, Destination_Longitude, results);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            distance.setText(results[0] + " Km");
                            distance.setTextColor(getResources().getColor(R.color.colorAccent));

                        }
                    },2000);
                }
                if (DestinationLatLong.equals("null"))
                {
                    AlertDialog.Builder builder= new AlertDialog.Builder(this);
                    builder.setIcon(R.drawable.ic_warning_black_24dp);
                    builder.setTitle("Attention");
                    builder.setMessage("No Coordinates Found for this Destination. Please select a more precise location");
                    builder.setPositiveButton("OK", null);
                }
            }
        }
    }

    public void DESTINATION_PICKER(View view) {

        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            Intent startDestinationPicker = builder.build(this);
            startActivityForResult(startDestinationPicker, PLACE_PICKER_REQUEST_DESTINATION);
            Toast.makeText(PlacePickerActivity.this, "Select the destination of your request", Toast.LENGTH_LONG).show();
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            Toast.makeText(PlacePickerActivity.this, "Sorry, Google Play Service Not Available", Toast.LENGTH_SHORT).show();
        }
    }

    public void PICK_UP_POINT_PICKER(View view) {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

        try {

            Intent startPickUpPointPicker = builder.build(this);
            startActivityForResult(startPickUpPointPicker, PLACE_PICKER_REQUEST_PICKUP_POINT);
            Toast.makeText(PlacePickerActivity.this, "Select the location of your product", Toast.LENGTH_LONG).show();

        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            Toast.makeText(PlacePickerActivity.this, "Sorry, Google Play Service Not Available", Toast.LENGTH_SHORT).show();
        }

    }

    public void GOTO_DELIVERY(View view) {

        if (deliveryType.equals("1"))
        {
            Intent gotoDelivery = new Intent(PlacePickerActivity.this, VanActivity.class);
            gotoDelivery.putExtra("PICKUP_POINT", placeAddress);
            gotoDelivery.putExtra("DESTINATION", destinationAddress);
            startActivity(gotoDelivery);
            overridePendingTransition(R.anim.slide_in_right, android.R.anim.slide_out_right);
            finish();
        }
        if (deliveryType.equals("2"))
        {
            Intent gotoDelivery = new Intent(PlacePickerActivity.this, MotorActivity.class);
            gotoDelivery.putExtra("PICKUP_POINT", placeAddress);
            gotoDelivery.putExtra("DESTINATION", destinationAddress);
            startActivity(gotoDelivery);
            overridePendingTransition(R.anim.slide_in_right, android.R.anim.slide_out_right);
            finish();
        }
        if (deliveryType.equals("3"))
        {
            Intent gotoDelivery = new Intent(PlacePickerActivity.this, TruckActivity.class);
            gotoDelivery.putExtra("PICKUP_POINT", placeAddress);
            gotoDelivery.putExtra("DESTINATION", destinationAddress);
            startActivity(gotoDelivery);
            overridePendingTransition(R.anim.slide_in_right, android.R.anim.slide_out_right);
            finish();
        }

    }

    public void GO_HOME(View view) {
        startActivity(new Intent(this, MainActivity.class));
        this.finish();
    }
}
