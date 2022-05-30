package com.senarios.coneqtliveviewer.Views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.senarios.coneqtliveviewer.R;
import com.senarios.coneqtliveviewer.Views.OverView.OverViewActivity;
import com.senarios.coneqtliveviewer.Views.Settings.SettingsActivity;

public class TestScreensActivity extends AppCompatActivity {
    AppCompatButton overview, event, setting, payout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_screens);
        overview = findViewById(R.id.overview);
        event = findViewById(R.id.create_event);
        setting = findViewById(R.id.settings);
        payout = findViewById(R.id.payout);
        overview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TestScreensActivity.this, OverViewActivity.class));

            }
        });
        event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(TestScreensActivity.this, CreateEventActivity.class));

            }
        });
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TestScreensActivity.this, SettingsActivity.class));

            }
        });
    }
}