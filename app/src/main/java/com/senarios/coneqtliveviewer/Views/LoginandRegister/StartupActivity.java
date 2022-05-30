package com.senarios.coneqtliveviewer.Views.LoginandRegister;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.senarios.coneqtliveviewer.R;
import com.senarios.coneqtliveviewer.Views.ConeqtCreator.GuestStartActivity;
import com.senarios.coneqtliveviewer.Views.OverView.OverViewActivity;

public class StartupActivity extends AppCompatActivity {
    private Button startedBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);
        startedBtn= findViewById(R.id.idLoginBtnLogin);

        initLayout();

    }
    private void initLayout() {
        startedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StartupActivity.this,OverViewActivity.class));
            }
        });

    }
}