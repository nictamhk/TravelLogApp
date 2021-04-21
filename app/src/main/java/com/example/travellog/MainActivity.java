package com.example.travellog;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView nav;
    final FragmentManager fm = getSupportFragmentManager();
    final Fragment recFragment = new RecommendationFragment();
    final Fragment checkinFragment = new CheckinFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nav = findViewById(R.id.nav);
        nav.setOnNavigationItemSelectedListener(selectListener);

        fm.beginTransaction().replace(R.id.container, checkinFragment).addToBackStack(null).commit();
    }

    final private BottomNavigationView.OnNavigationItemSelectedListener selectListener = item -> {
        switch (item.getItemId()) {
            case R.id.suggest:
                System.out.println("nav1");
                fm.beginTransaction().replace(R.id.container, recFragment).addToBackStack(null).commit();
                break;
            case R.id.checkin:
                System.out.println("nav1");
                fm.beginTransaction().replace(R.id.container, checkinFragment).addToBackStack(null).commit();
                break;
        }
        System.out.println("hello");
        return true;
    };
}