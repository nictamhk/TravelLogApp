package com.example.travellog;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView nav;
    final FragmentManager fm = getSupportFragmentManager();
    final Fragment recFragment = new SuggestFragment();
    final Fragment checkinFragment = new CheckinFragment();
    final Fragment CasesFragment = new CasesFragment();
    final Fragment StatsFragment = new StatsFragment();

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
                fm.beginTransaction().replace(R.id.container, recFragment).addToBackStack(null).commit();
                break;
            case R.id.checkin:
                fm.beginTransaction().replace(R.id.container, checkinFragment).addToBackStack(null).commit();
                break;
            case R.id.covid:
                fm.beginTransaction().replace(R.id.container, CasesFragment).addToBackStack(null).commit();
                break;
            case R.id.stats:
                fm.beginTransaction().replace(R.id.container, StatsFragment).addToBackStack(null).commit();
                break;
        }

        return true;
    };
}