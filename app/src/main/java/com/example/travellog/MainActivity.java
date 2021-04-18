package com.example.travellog;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;

import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements LocationListener {
    private static final int REQUEST_LOCATION = 1;
    Button btnCheckIn;
    TextView showLocation;
    String latitude, longitude;
    Button navBtn;

    Context context = this;

    // For GPS information;
    LocationManager locationManager;
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

        // Request for GPS permission;
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        showLocation = findViewById(R.id.showLocation);
        btnCheckIn = (Button)findViewById(R.id.btnCheckIn);
        btnCheckIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getLocation();
                connect4sq();
            }
        });

        // Navigator Button
        navBtn = (Button)findViewById(R.id.navBtn);
        navBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(getBaseContext(), RecommendationActivity.class);
                startActivity(intent);
            }
        }
        );
    }


    @Override
    protected void onResume() {
        super.onResume();
        getLocation();
    }


    private void getLocation() {

        // If the app does not have permission for GPS, request for one
        if (ActivityCompat.checkSelfPermission(
                MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        }

        // Otherwise grab location
        else {
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            boolean isGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            boolean isNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            // Determine the provider
            if (isGPS) {
                providerInfo = LocationManager.GPS_PROVIDER;
            }
            else if (isNetwork){
                providerInfo = LocationManager.NETWORK_PROVIDER;
            }

            // If there is a provider, request a location
            if (!providerInfo.isEmpty()) {
                locationManager.requestLocationUpdates(providerInfo, 30000, 10, this);

                if (locationManager != null) {
                    loc = locationManager.getLastKnownLocation(providerInfo);
                    latitude = String.valueOf(loc.getLatitude());
                    longitude = String.valueOf(loc.getLongitude());
                    showLocation.setText("Your Location: " + "\n" + "Latitude: " + latitude + "\n" + "Longitude: " + longitude);
                    //connect4sq();
                }

                // Cannot retrieve location
                else
                    Toast.makeText(context, "Location not found", Toast.LENGTH_SHORT);
            }

            Toast.makeText(context, "No GPS Provider", Toast.LENGTH_SHORT);

        }
    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = String.valueOf(location.getLatitude());
        longitude = String.valueOf(location.getLongitude());
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


    public void connect4sq (){

        getLocation();

        if (latitude != null && !latitude.isEmpty() && longitude != null && !longitude.isEmpty()) {
            final String url = "https://api.foursquare.com/v2/venues/search?client_id="
                    + CLIENT_ID + "&client_secret=" + CLIENT_SECRET + "&v=20210325&ll="
                    + latitude + "," + longitude;

            // Dedicated extra thread for the long-lasting thread to obtain locations;
            ExecutorService executor = Executors.newSingleThreadExecutor();
            final Handler handler = new Handler(Looper.getMainLooper());

            executor.execute(new Runnable()
                {
                    @Override
                    public void run(){
                        try {
                            JSONArray venues_results = getFoursquare(url);
                            handler.post(new Runnable() {
                                @Override
                                public void run(){
                                    extractPOI(venues_results);
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

            );


        }
        else
        {
            Toast.makeText(this, "ERROR connecting to 4sq", Toast.LENGTH_SHORT);
        }
    }

    /*
    public static String readBufferedHTML (Reader rd) throws IOException{
        StringBuilder sb = new StringBuilder();
        int newchar = rd.read();

        // While the character read in is not invalid;
        while (newchar != -1){
            sb.append((char)newchar);
        }

        return sb.toString();
    }

     */

    public String readBufferedHTML (BufferedReader reader, char[] htmlBuffer, int bufSz)
            throws java.io.IOException{
        htmlBuffer[0] = '\0';
        int offset = 0;
        do
            {
            int cnt = reader.read(htmlBuffer, offset, bufSz - offset);
            if (cnt > 0)
                offset += cnt;
            else
                break;
            }
        while (true);

        return new String(htmlBuffer);
    }

    public JSONArray getFoursquare (String url) throws IOException, JSONException {
        URL url_obj = new URL(url);
        InputStream ins = url_obj.openStream();

        final int HTML_BUFFER_SIZE = 2*1024*1024;
        char htmlBuffer[] = new char[HTML_BUFFER_SIZE];

        JSONArray venue_array = new JSONArray();

        try
        {
            BufferedReader reader = new BufferedReader(new InputStreamReader(ins));
            String source = readBufferedHTML(reader, htmlBuffer, HTML_BUFFER_SIZE);
            JSONObject json = new JSONObject(source);

            if (json.has("response")){
                JSONObject json_resp = json.getJSONObject("response");

                if (json_resp.has("venues")){
                    venue_array = json_resp.getJSONArray("venues");
                    Log.println(Log.DEBUG, "getFoursquare", "Venue array was retrieved.");
                }
            }
        }
        catch (Exception e)
        {
            Log.println(Log.DEBUG, "getFoursquare", "Venue array is blank.");
            return new JSONArray();  // Returns a blank object if error encountered
        }
        finally
        {
            ins.close();
            Log.println(Log.DEBUG, "getFoursquare",
                    "Size of Venue array : " + Integer.valueOf(venue_array.length()));
            return venue_array;
        }

    }

    // Does a check if the required information is present before extracting it;
    public static String jsonExtract (String reqComponent, JSONObject place){

        String extracted_result = "";

        try {
            if (place.has(reqComponent))
                extracted_result = place.getString(reqComponent);
        }
        catch (JSONException e){
            e.printStackTrace();
        }

        return extracted_result;
    }

    // For extracting, distance, visits, and been here data.
    public static int jsonExtractSubLevel (String reqComponent, JSONObject place){

        int extracted_result = 0;

        try {
            if(reqComponent == "distance") {
                extracted_result = place.getJSONObject("location").getInt("distance");
            }

            // Placeholder - to be replace by own database
            else if (reqComponent == "visited_count") {
                if (place.has("beenHere")){
                    if (place.getJSONObject("beenHere").has("count"))
                        extracted_result = place.getJSONObject("beenHere").getInt("count");
                }

                else
                    extracted_result = 0;
                Log.println(Log.DEBUG, "Visited Count", "" + extracted_result);
            }

            else if (reqComponent == "here_count"){
                if (place.has("hereNow")){
                    if (place.getJSONObject("hereNow").has("count"))
                        extracted_result = place.getJSONObject("hereNow").getInt("count");
                }

                else
                    extracted_result = 0;
                Log.println(Log.DEBUG, "Here Now Count", "" + extracted_result);
            }

        }
        catch (JSONException e){
            e.printStackTrace();
        }

        return extracted_result;
    }

    // Extracts information from the JSON array to create a list of POI objects;
    public void extractPOI(final JSONArray venue_resp){
        ArrayList lst_locations = new ArrayList();

        ArrayList<String> vnames = new ArrayList<String>();
        ArrayList vdist = new ArrayList();

        try {

            for (int i = 0; i < venue_resp.length(); i++)
            {
                JSONObject obj = venue_resp.getJSONObject(i);
                POI poi = new POI (jsonExtract("name", obj),
                        jsonExtract("address", obj),
                        jsonExtract("city", obj),
                        jsonExtract("country", obj),
                        jsonExtract("longitude", obj),
                        jsonExtract("latitude", obj),
                        jsonExtract("category", obj),
                        jsonExtractSubLevel("distance", obj),
                        jsonExtractSubLevel("visited_count", obj),
                        jsonExtractSubLevel("here_count", obj));

                lst_locations.add(poi);
                //System.out.println("Location Name: " + poi.getName());
            }
        }
        catch (JSONException e){
            e.printStackTrace();
        }


        Intent intent = new Intent(getBaseContext(), LocationListActivity.class);
        intent.putParcelableArrayListExtra("pois", lst_locations);
        startActivity(intent);

    }

/*
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
*/
}