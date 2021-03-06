package com.osnielky.locationdemo;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {


    AdView adView;

    private GoogleMap mMap;

    LocationManager locationManager;
    LocationListener locationListener;
    TextView distancetext;
    LatLng carLocation;
    float[] results = new float[1];
    double carlatitudeimported, carlongitudeimported;
    Marker markerName;
    ImageButton exit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        setTitle("Dude, Where's My Car?");

        adView = findViewById(R.id.adView);
        MobileAds.initialize(this, "ca-app-pub-8699119698511989~7536454877");
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        // interstitialAd = new InterstitialAd(this) ;


        //getting the values from the previous view.
        Intent intent = getIntent();
        carlatitudeimported = intent.getDoubleExtra("carLatitude", 0);
        carlongitudeimported = intent.getDoubleExtra("carLongitude", 0);

        Log.i("----->", String.valueOf(intent.getDoubleExtra("carLatitude", 0)));





        exit = (ImageButton) findViewById(R.id.exitButton);
        exit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(MapsActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("EXIT", true);
                startActivity(intent);
                //return true;
            }
        });
        distancetext = findViewById(R.id.distance);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {


                LatLng myActualLocation = new LatLng(location.getLatitude(), location.getLongitude());

                if (markerName != null) {
                    markerName.remove();
                }
                markerName = mMap.addMarker(new MarkerOptions().position(myActualLocation).title("Marker Miami").icon(BitmapDescriptorFactory.fromResource(R.drawable.iconpersonwalking)));

                Location car = new Location("localizacion 2");
                car.setLatitude(carLocation.latitude);
                car.setLatitude(carLocation.longitude);


                Location.distanceBetween(location.getLatitude(), location.getLongitude(),
                        carLocation.latitude, carLocation.longitude,
                        results);

                distancetext.setText("Distance (meters) : " + String.format("%.2f", results[0]));


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
        markerCar();
    }


    public void markerCar() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        // Add a marker in miami and move the camera
        carLocation = new LatLng(carlatitudeimported, carlongitudeimported);  // position in miami from google maps
        // position in miami from google maps
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setMyLocationEnabled(true);


        //   mMap.addMarker(new MarkerOptions().position(carLocation).title("Marker Miami").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN))); // color and marker
        mMap.addMarker(new MarkerOptions().position(carLocation).title("Marker Miami").icon(BitmapDescriptorFactory.fromResource(R.drawable.iconcar))); // color and marker

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(carLocation, 18));

    }


}