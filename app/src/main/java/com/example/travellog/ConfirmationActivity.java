package com.example.travellog;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ConfirmationActivity extends AppCompatActivity {

    POI checked_in_place = null;

    TextView confirmed, stats, news;
    Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);

        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }

        Intent intent = this.getIntent();
        POI checked_in_place = intent.getParcelableExtra("checked_in_place");

        int visited_count = checked_in_place.getVisitedTimes();
        boolean visited_before = checked_in_place.getVisitedBefore();
        int herenow_count = checked_in_place.getHereNow();

        // WRITE TO DATABASE MODULE - TO BE COMPLETED;

        // Message confirming checked in;
        confirmed = findViewById(R.id.confirmed);
        confirmed.setText("Big thank you for checking in and staying safe.");

        // Displaying interesting stats;
        stats = findViewById(R.id.stats);
        if (visited_before)
            stats.setText("Welcome back! You have been here " + visited_count + " times!" +
                    "\nThere are " + herenow_count + " other people here right now.");
        else
            stats.setText("It is your first time here. Have fun exploring!" +
                    "\nThere are " + herenow_count + " other people here right now.");

        // CONNECT WITH COVID-19 MODULE;
        news = findViewById(R.id.news);
        news.setText("There are no COVID-19 confirmed cases around you."); // Placeholder

        // Back Button
        btnBack = (Button)findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                startActivity(intent);
            }

        });
    }


}