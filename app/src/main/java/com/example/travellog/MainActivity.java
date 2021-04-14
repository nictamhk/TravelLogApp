package com.example.travellog;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.os.IBinder;
import android.provider.Settings;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity implements LocationListener {
    private static final int REQUEST_LOCATION = 1;
    Button btnCheckIn;
    TextView showLocation;
    LocationManager locationManager;
    String latitude, longitude;

    // For GPS information;
    GPS gps;
    boolean isGPS, isNetwork;
    Location loc;
    String providerInfo;

    // Foursquare API credentials;
    public static final String CLIENT_ID = "YJFBIKOBOE5DP3C0H53SKDOD2HK240BR0WM30Y02MPW1VHSG";
    public static final String CLIENT_SECRET = "BQCYXYVYB4X5KTS1EKPHWUG5ZI1MCOXZOHIYZ3KXZZ0PGT2W";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        GPS gps = new GPS(this);

        showLocation = findViewById(R.id.showLocation);

        if (gps.getIsGPSTrackingEnabled())
        {
            showLocation.setText(String.valueOf(gps.latitude) + ", " + String.valueOf(gps.longitude));
        }

        else
        {
            showLocation.setText("Location not found");
        }

        //showLocation = findViewById(R.id.showLocation);
        btnCheckIn = findViewById(R.id.btnCheckIn);
        btnCheckIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Location loc = getLastKnownLocation();
                /*
                //Location loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                if (loc != null) {
                    latitude = String.valueOf(loc.getLatitude());
                    showLocation.setText("Latitude" + latitude);
                }

                else{

                    if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {
                        showLocation.setText("No GPS Permission granted");

                        //ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
                    }

                    else
                        showLocation.setText("Something else");
                    return ;
                }


                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
                }
                Location loc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                if (loc != null) {
                    latitude = String.valueOf(loc.getLatitude());
                    showLocation.setText("Latitude" + latitude);
                }

                else {

                    if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {
                        showLocation.setText("No GPS Permission granted");

                        //ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
                    } else
                        showLocation.setText("Something else");
                }
*/

                getLocation();


                //getLocation();
                //if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                //    OnGPS();
                //} else {
                //getLocation();
                //}
            }
        });
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(
                MainActivity.this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            boolean isGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            boolean isNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (isGPS) {
                providerInfo = LocationManager.GPS_PROVIDER;
            }
            else if (isNetwork){
                providerInfo = LocationManager.NETWORK_PROVIDER;
            }

            if (!providerInfo.isEmpty()) {
                locationManager.requestLocationUpdates(providerInfo, 300000, 10, this);

                if (locationManager != null) {
                    loc = locationManager.getLastKnownLocation(providerInfo);
                    latitude = String.valueOf(loc.getLatitude());
                    longitude = String.valueOf(loc.getLongitude());
                    showLocation.setText("Your Location: " + "\n" + "Latitude: " + latitude + "\n" + "Longitude: " + longitude);
                }
            }


/*
            if (locationGPS != null) {
                double lat = locationGPS.getLatitude();
                double longi = locationGPS.getLongitude();
                latitude = String.valueOf(lat);
                longitude = String.valueOf(longi);
                System.out.println("Execution succeeded");
                showLocation.setText("Your Location: " + "\n" + "Latitude: " + latitude + "\n" + "Longitude: " + longitude);
            } else {
                Toast.makeText(this, "Unable to find location.", Toast.LENGTH_SHORT).show();
            }

 */
        }
    }

    private Location getLastKnownLocation() {
        Location l=null;
        LocationManager mLocationManager = (LocationManager)getApplicationContext().getSystemService(LOCATION_SERVICE);
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED) {
                l = mLocationManager.getLastKnownLocation(provider);
            }
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                bestLocation = l;
            }
        }
        return bestLocation;
    }

    @Override
    public void onLocationChanged(Location location) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    private class foursquare extends AsyncTask<View, Void, String> {

    }


/*
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
*/
}