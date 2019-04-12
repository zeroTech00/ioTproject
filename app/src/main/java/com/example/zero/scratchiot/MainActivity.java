package com.example.zero.scratchiot;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;

import com.example.zero.scratchiot.menu.AboutFragment;
import com.example.zero.scratchiot.menu.ChartFragment;
import com.example.zero.scratchiot.menu.StreamingFragment;
import com.github.mikephil.charting.charts.LineChart;

public class MainActivity extends AppCompatActivity {
    private LineChart chart;
    boolean TimerStart = false;
    Button Tombol1, Tombol2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById (R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener (navListener);

        getSupportFragmentManager ().beginTransaction ().replace (R.id.fragment_container,
                new ChartFragment()).commit ();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener () {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selectedFragment = null;

                    switch (menuItem.getItemId ()) {
                        case R.id.nav_chart:
                            selectedFragment = new ChartFragment();
                            break;
                        case R.id.nav_streaming:
                            selectedFragment = new StreamingFragment();
                            break;
                        case R.id.nav_about:
                            selectedFragment = new AboutFragment();
                            break;
                    }

                    getSupportFragmentManager ().beginTransaction ().replace (R.id.fragment_container,
                            selectedFragment).commit ();

                    return true;
                }
            };
}
