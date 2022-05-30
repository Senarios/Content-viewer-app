package com.senarios.coneqtliveviewer.Views.ConeqtCreator;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.senarios.coneqtliveviewer.Adapter.ParticipantsAdapter;
import com.senarios.coneqtliveviewer.Model.ParticipantsModel;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.senarios.coneqtliveviewer.R;

import java.util.ArrayList;

import io.alterac.blurkit.BlurLayout;

public class ParticipantsActivity extends AppCompatActivity {
    BlurLayout blurLayout;
    RecyclerView recyclerView;
    ImageView crossBtn;
    ParticipantsAdapter adapter;
    private ArrayList<ParticipantsModel> arrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participants);


        blurLayout = findViewById(R.id.blurLayout);
        blurLayout.setVisibility(View.VISIBLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        crossBtn = findViewById(R.id.crossBtn);
        crossBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        recyclerView=findViewById(R.id.show_participants_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<ParticipantsModel> options =
                new FirebaseRecyclerOptions.Builder<ParticipantsModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("streams")
                                .child("987654").child("participents"), ParticipantsModel.class).build();

        adapter=new ParticipantsAdapter(options);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        adapter.startListening();
        adapter.notifyDataSetChanged();

    }

    @Override
    protected void onStart() {
        super.onStart();
        blurLayout.startBlur();
        blurLayout.lockView();
    }

    @Override
    protected void onPause() {
        blurLayout.pauseBlur();
        super.onPause();
    }
}