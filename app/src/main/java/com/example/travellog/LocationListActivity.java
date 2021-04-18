package com.example.travellog;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LocationListActivity extends Activity {

    ArrayList<POI> poiArrayList = null;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_listview);

        Intent intent = this.getIntent();
        poiArrayList = intent.getParcelableArrayListExtra("pois");

        if (poiArrayList.size() == 0)
            Log.println(Log.DEBUG, "D", "NO LOCATIONS");

        ArrayList<Map<String, Object>> venueList = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < poiArrayList.size(); i++){
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("Name", poiArrayList.get(i).getName());
            map.put("Distance", poiArrayList.get(i).getDistance());
            venueList.add(map);
        }

        POIadapter adapter = new POIadapter(this, poiArrayList);
        //adapter = new POIadapter(this, R.layout.poi_list_item, poiArrayList);

        // Get a reference to the ListView, and attach the adapter to the listView.
        ListView lstView = (ListView) findViewById(R.id.listview_location);
        lstView.setAdapter(adapter);

        lstView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                POI checked_in_place = poiArrayList.get(i);

                if (checked_in_place.getVisitedBefore()){
                    Toast.makeText(getApplicationContext(), "Checked-in! Welcome back!", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(getApplicationContext(),
                            "Checked-in! First time here, have fun exploring!", Toast.LENGTH_SHORT).show();

                Log.println(Log.DEBUG, "LocationListActivity", "Click recognised");

                Intent confirm = new Intent(LocationListActivity.this, ConfirmationActivity.class);
                confirm.putExtra("checked_in_place", checked_in_place);
                startActivity(confirm);
                }
        });

        //SimpleAdapter adapter = new SimpleAdapter(this, venueList, R.layout.poi_list_item,
        //        new String[]{"Name", "Distance"}, new int[]{R.id.venue_name, R.id.venue_dist});
        //setListAdapter(adapter);
    }
}
