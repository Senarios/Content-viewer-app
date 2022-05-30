package com.senarios.coneqtliveviewer.Views.ConeqtCreator;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.senarios.coneqtliveviewer.R;
import com.senarios.coneqtliveviewer.Views.OverView.OverViewActivity;


public class WelcomeEvent extends AppCompatActivity {
    private TextView createEventTxt;
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_event);

        initLayout();

    }
    private void initLayout(){
        createEventTxt = findViewById(R.id.idLoginBtnLogin);
        allClickListener();
    }

    private void allClickListener() {
        createEventTxt.setOnClickListener(v -> {
            startActivity(new Intent(WelcomeEvent.this, OverViewActivity.class));
        });
    }
}