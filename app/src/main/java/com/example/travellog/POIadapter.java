package com.example.travellog;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class POIadapter extends ArrayAdapter<POI> {

    ArrayList<POI> lst_poi;
    int resource;
    Context context;
/*
    public POIadapter(Context context, int resource, ArrayList<POI> lst_poi) {
        super(context, resource, lst_poi);
        this.lst_poi=lst_poi;
    }

 */

    public POIadapter(Context context, ArrayList<POI> lst_poi){
        super(context, 0, lst_poi);
    }

    /*
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout poi_lst_view;

        poi_lst_view = new LinearLayout(getContext());
        context = getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View single_row = inflater.inflate(R.layout.poi_list_item, poi_lst_view, true);

        TextView venue_name = (TextView) single_row.findViewById(R.id.venue_name);
        TextView venue_dist = (TextView) single_row.findViewById(R.id.venue_dist);

        venue_name.setText(lst_poi.get(position).getName());
        venue_dist.setText(String.valueOf(lst_poi.get(position).getDistance()) + "m");

        //Log.println(Log.DEBUG, "view", venue_dist.getText().toString());

        return single_row;
    }

     */

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Check if the existing view is being reused, otherwise inflate the view
            View listItemView = convertView;
            if(listItemView == null) {
                listItemView = LayoutInflater.from(getContext()).inflate(
                        R.layout.poi_list_item, parent, false);
            }

            // Get the {@link Dessert} object located at this position in the list
            POI currentpoi = getItem(position);

            TextView venue_name = (TextView) listItemView.findViewById(R.id.venue_name);
            venue_name.setText(currentpoi.getName());

            TextView venue_dist = (TextView) listItemView.findViewById(R.id.venue_dist);
            venue_dist.setText(String.valueOf(currentpoi.getDistance()) + " m");

            // Return the whole list item layout (containing 2 TextViews and an ImageView)
            // so that it can be shown in the ListView
            return listItemView;
        }
    }


