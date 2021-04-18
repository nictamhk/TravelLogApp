package com.example.travellog;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LocationListActivity extends ListActivity {

    ArrayList<POI> poiArrayList = null;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.poi_list_item);

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

        ListView lstView;

        POIadapter adapter;
        adapter = new POIadapter(this, R.layout.poi_list_item, poiArrayList);

        //SimpleAdapter adapter = new SimpleAdapter(this, venueList, R.layout.poi_list_item,
        //        new String[]{"Name", "Distance"}, new int[]{R.id.venue_name, R.id.venue_dist});
        setListAdapter(adapter);
    }
}
