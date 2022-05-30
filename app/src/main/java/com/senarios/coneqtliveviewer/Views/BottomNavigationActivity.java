package com.senarios.coneqtliveviewer.Views;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.senarios.coneqtliveviewer.R;
import com.senarios.coneqtliveviewer.Views.OverView.OverViewActivity;
import com.senarios.coneqtliveviewer.Views.Settings.SettingsActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BottomNavigationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navigation);
        //// initializes and assigned ids:
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);

        ////
        bottomNavigationView.setSelectedItemId(R.id.home);

        bottomNavigationView.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.idOverView:
                        startActivity(new Intent(BottomNavigationActivity.this, OverViewActivity.class));
                        overridePendingTransition(0, 0);
                        return;

                    case R.id.idSettings:
                        startActivity(new Intent(BottomNavigationActivity.this, SettingsActivity.class));
                        overridePendingTransition(0, 0);
                        return;
                }
                return;
            }
        });


    }
}