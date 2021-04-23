package com.example.travellog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.android.volley.RequestQueue;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

public class SuggestFragment extends Fragment implements View.OnClickListener {
    Button btn;
    TextView place;
    Spinner types_spinner;
    String PlaceAPIKey = "AIzaSyAeEBdFIKZ7yXZA1vxWucFL28DuVs2F4ug";
    String baseUrl = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?language=zh-HK&rankby=distance";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_suggest, container, false);
        place = view.findViewById(R.id.place);
        btn = view.findViewById(R.id.recBtn);
        btn.setOnClickListener(this);

        types_spinner = view.findViewById(R.id.type_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                Objects.requireNonNull(getActivity()).getBaseContext(),
                R.array.type_array,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        types_spinner.setAdapter(adapter);

        return view;
    }

    @Override
    public void onClick(View v) {
        RequestQueue queue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()).getBaseContext());
        final Double latit = 22.31554554826026;
        final Double longit = 114.22506642557767;
        final String type = types_spinner.getSelectedItem().toString();
        final String url = formatURL(latit, longit, type);
        System.out.println(url);


        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                object -> {
                    ArrayList<String> names = new ArrayList<>();
                    try {
                        final JSONArray results = object.getJSONArray("results");
                        for (int i = 0; i < object.length(); i++) {
                            final String name = results.getJSONObject(i).getString("name");
                            names.add(name);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    place.setText(names.toString());

                },
                error -> place.setText("Please retry")
        );

        queue.add(request);
    }

    private String formatURL(Double lat, Double lon, String type) {
        return String.format(Locale.TRADITIONAL_CHINESE, "%s&key=%s&location=%f,%f&type=%s", baseUrl, PlaceAPIKey, lat, lon, type);
    }
}
