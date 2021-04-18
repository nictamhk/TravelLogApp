package com.example.travellog;

import android.content.Context;
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

    public POIadapter(Context context, int resource, ArrayList<POI> lst_poi) {
        super(context, resource, lst_poi);
        this.lst_poi=lst_poi;
    }

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

        return single_row;
    }

}
