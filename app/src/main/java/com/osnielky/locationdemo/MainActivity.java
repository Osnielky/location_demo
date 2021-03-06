package com.osnielky.locationdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    LocationManager locationManager;
    LocationListener locationListener;
    SharedPreferences.Editor editor;
    TextView infotext;
    Calendar calendar;
    String currenDate;
    double latitudeDouble;
    double longitudeDouble;
    SharedPreferences prefs;
    String oldDateStr;
    Animation scaleup, scaledown;
    double latitude, longitude;
    Button saveLocation, findCar;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 0, 0, locationListener);
            }
        }
    }

    public void getItfromThePreference (){
         prefs = getSharedPreferences("Position Saved", MODE_PRIVATE);

       // if (!prefs.getString("latitude", "").equals("")||!prefs.getString("longitude", "").equals("")){
            Toast.makeText(MainActivity.this, " Position Loaded !! " , Toast.LENGTH_SHORT).show();
            latitudeDouble = Double.parseDouble(prefs.getString("latitude", "0"));
            longitudeDouble = Double.parseDouble(prefs.getString("longitude", "0"));
            oldDateStr = prefs.getString("lastdate", "");


    }
    public void goToMyLocation(View v) {
        Intent intent = new Intent(this, MapsActivity.class);
        getItfromThePreference();
        intent.putExtra("carLongitude", longitudeDouble);
        intent.putExtra("carLatitude", latitudeDouble);
        startActivity(intent);
    }

    public void saveCarLocation() {
        // save latitude y longitude
        currenDate = DateFormat.getDateTimeInstance().format(calendar.getTime());
        prefs = getSharedPreferences(

                "Position Saved", MODE_PRIVATE);
        editor = prefs.edit();
        editor.putString("latitude", Double.toString(latitude));
        editor.putString("longitude", Double.toString(longitude));
        editor.putString("lastdate", currenDate);
        editor.apply();
        editor.commit();

       Toast.makeText(MainActivity.this, " Position Saved !! " , Toast.LENGTH_SHORT).show();
        infotext.setText("Your last saved position: " + "\n" + currenDate);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setTitle("Dude, Where's My Car?");

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        if (getIntent().getBooleanExtra("EXIT", false)) {

            finish();

            return;
        }

        scaledown = AnimationUtils.loadAnimation(this, R.anim.scale_down);
        scaleup = AnimationUtils.loadAnimation(this, R.anim.scale_up);
        getItfromThePreference();

        saveLocation = findViewById(R.id.carLocationButton);
        findCar = findViewById(R.id.showButton);
        infotext = findViewById(R.id.statustextView);
        calendar = Calendar.getInstance();
        currenDate = DateFormat.getDateTimeInstance().format(calendar.getTime());
        infotext.setText("Your last saved position: " + "\n" + oldDateStr);

        saveLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveLocation.startAnimation(scaleup);
                saveCarLocation();
            }
        });

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {


            @Override
            public void onLocationChanged(@NonNull Location location) {

                latitude = location.getLatitude();
                longitude = location.getLongitude();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(@NonNull String provider) {

            }

            @Override
            public void onProviderDisabled(@NonNull String provider) {

            }
        };
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);


        } else {

            locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 0, 0, locationListener);


        }


    }
}