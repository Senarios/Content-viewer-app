package com.senarios.coneqtliveviewer.Views.ConeqtCreator;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.senarios.coneqtliveviewer.Adapter.CreateEventHistoryAdapter;
import com.senarios.coneqtliveviewer.Adapter.CreateEventHistoryAdapterPast;
import com.senarios.coneqtliveviewer.Model.CreateEventHistoryData;
import com.senarios.coneqtliveviewer.Model.EventCompletedModel;
import com.senarios.coneqtliveviewer.Model.NotificationCountModel;
import com.senarios.coneqtliveviewer.Model.Past;
import com.senarios.coneqtliveviewer.Model.RatingModel;
import com.senarios.coneqtliveviewer.Model.RefundModel;
import com.senarios.coneqtliveviewer.Model.StartEventModel;
import com.senarios.coneqtliveviewer.Model.Upcoming;
import com.senarios.coneqtliveviewer.R;
import com.senarios.coneqtliveviewer.Views.ApiServices;
import com.senarios.coneqtliveviewer.Views.Notification.NotificationActivity;
import com.senarios.coneqtliveviewer.Views.OverView.OverViewActivity;
import com.senarios.coneqtliveviewer.Views.Settings.SettingsActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.senarios.coneqtliveviewer.Views.TouchBlackHoleView;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CreateEventHistoryActivity extends AppCompatActivity implements CreateEventHistoryAdapter.StartEvent {

    private Button upcoming_event, past_event;
    private ImageView notification, imgShare, imgSecondShare;
    private LinearLayout errorMessageLayout, pastLayout, upComingLayout;
    private TextView pastTextView, upComingTextView, startBtn, cancelBtnFirst, cancelBtnSecond , firstLetter;
    private LinearLayout firstLayout, SecondLayout;
    RelativeLayout idProgressBarRelative;
    LottieAnimationView lottieAnimationView;
    private CreateEventHistoryAdapter createEventHistoryAdapter;
    private CreateEventHistoryAdapterPast createEventHistoryAdapterPast;
    RecyclerView recyclerView;
    private TextView nameTxtView , eventcancelTxt;
    private ImageView headerImage;
    SwipeRefreshLayout swipeRefreshLayout;
    boolean upcoming = true;
    TouchBlackHoleView blackHoleView;
    private TextView displayTxt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event_history);

        initLayout();
        getCreateEventHistory();

        SharedPreferences preferences = getSharedPreferences("my", MODE_PRIVATE);
        String signinValue = preferences.getString("signedInKey", "abc");
        String image = preferences.getString("imageName", "");
        String idUser = preferences.getString("first_name", "abc");
        String LastUser = preferences.getString("last_name", "abc");

        if (signinValue.equals("Yes")) {
            nameTxtView.setText(idUser);

            if(!image.isEmpty() && image !=null) {
                Glide.with(this)
                        .load(image) // image url
                        .addListener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                String test = idUser;
                                String test1 = LastUser;
                                String s=test.substring(0,1).toUpperCase();
                                String s1=test1.substring(0,1).toUpperCase();
                                firstLetter.setText(s+ s1);
                                firstLetter.setVisibility(View.VISIBLE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                return false;
                            }
                        })
                        .into(headerImage);
                firstLetter.setVisibility(View.GONE);
            } else {
                String test = idUser;
                String test1 = LastUser;
                String s=test.substring(0,1).toUpperCase();
                String s1=test1.substring(0,1).toUpperCase();
                firstLetter.setText(s+ s1);
                firstLetter.setVisibility(View.VISIBLE);
            }
        }
        getNotificationCount();
    }

    private void initLayout() {
        notification = findViewById(R.id.notification_image);
        errorMessageLayout = findViewById(R.id.eventCancelLayout);
        upcoming_event = findViewById(R.id.tabbar_upcoming);
        past_event = findViewById(R.id.tabbar_past);
        firstLetter = findViewById(R.id.idFirstLetter);
        nameTxtView = findViewById(R.id.nameTextView);
        eventcancelTxt = findViewById(R.id.eventcancelTxt);
        headerImage = findViewById(R.id.headerImage);
        swipeRefreshLayout = findViewById(R.id.events_swipe);
        displayTxt = findViewById(R.id.idDisplay);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(upcoming) {
                    getCreateEventHistory();
                } else {
                    getCreateEventHistoryPast();
                }
//                swipeRefreshLayout.setRefreshing(false);
            }
        });

        /// Relative Layout OF LottieAnimation
        idProgressBarRelative = findViewById(R.id.idProgressBarRelative);
        lottieAnimationView = findViewById(R.id.lottie_main);
        blackHoleView = findViewById(R.id.blackHole);

        recyclerView = findViewById(R.id.CreateEventHistoryRV);


        allClickListener();
    }

    private void allClickListener() {

        upcoming_event.setOnClickListener(v -> {
            upcoming = true;
            upcoming_event.setTextColor(getResources().getColor(R.color.white));
            upcoming_event.setBackground(getResources().getDrawable(R.drawable.upcoming_selected));
            past_event.setTextColor(getResources().getColor(R.color.black));
            past_event.setBackground(getResources().getDrawable(R.drawable.past_unselected));
            getCreateEventHistory();

        });
        past_event.setOnClickListener(v -> {
            upcoming = false;
            past_event.setTextColor(getResources().getColor(R.color.white));
            past_event.setBackground(getResources().getDrawable(R.drawable.past_selected));
            upcoming_event.setTextColor(getResources().getColor(R.color.black));
            upcoming_event.setBackground(getResources().getDrawable(R.drawable.upcoming_unselected));
            getCreateEventHistoryPast();
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.eventHistoryBottom);

        bottomNavigationView.setSelectedItemId(R.id.idCreatteEvent);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.idOverView:
                        startActivity(new Intent(CreateEventHistoryActivity.this, OverViewActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.idCreatteEvent:
//                        startActivity(new Intent(CreateEventHistoryActivity.this, CreateEventHistoryActivity.class));
//                        overridePendingTransition(0,0);
                        return true;
                    case R.id.idSettings:
                        startActivity(new Intent(CreateEventHistoryActivity.this, SettingsActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });


        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateEventHistoryActivity.this, NotificationActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right , R.anim.slide_out_left);
            }
        });
    }

    private void getCreateEventHistory() {
        idProgressBarRelative.setVisibility(View.VISIBLE);
        lottieAnimationView.setVisibility(View.VISIBLE);
        blackHoleView.setVisibility(View.VISIBLE);
        blackHoleView.disableTouch(true);
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://conneqt.senarios.co/content_viewer/")
                .addConverterFactory(GsonConverterFactory.create(
                        gson)).build();
        ApiServices apiPost = retrofit.create(ApiServices.class);
        SharedPreferences preferences = getSharedPreferences("my", MODE_PRIVATE);
        String signinValue = preferences.getString("apiToken","");
        Call<CreateEventHistoryData> call = apiPost.getEventHistory("Bearer " + signinValue, 56.2 , 1, "dwed", "2023-11-24 11:13:59","2 hours",30, "1");
        call.enqueue(new retrofit2.Callback<CreateEventHistoryData>() {
            @Override
            public void onResponse(Call<CreateEventHistoryData> call, Response<CreateEventHistoryData> response) {
                if (response.isSuccessful()) {

                    List<Upcoming> list = response.body().getData().getUpcoming();

                    if (list != null && !list.isEmpty()) {
                        idProgressBarRelative.setVisibility(View.GONE);
                        lottieAnimationView.setVisibility(View.GONE);
                        blackHoleView.setVisibility(View.GONE);
                        blackHoleView.disableTouch(false);
                        recyclerView.setVisibility(View.VISIBLE);
                        displayTxt.setVisibility(View.GONE);
                        createEventHistoryAdapter = new CreateEventHistoryAdapter(list,getApplicationContext(),CreateEventHistoryActivity.this);
                        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setAdapter(createEventHistoryAdapter);
                        createEventHistoryAdapter.notifyDataSetChanged();
                        swipeRefreshLayout.setRefreshing(false);

                    } else {
                        displayTxt.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
//                        Toast.makeText(getApplicationContext(), "No Items in Recyclerview", Toast.LENGTH_SHORT).show();
                        idProgressBarRelative.setVisibility(View.GONE);
                        lottieAnimationView.setVisibility(View.GONE);
                        blackHoleView.setVisibility(View.GONE);
                        blackHoleView.disableTouch(false);
                    }
                } else {
                    recyclerView.setVisibility(View.GONE);
//                    Toast.makeText(getApplicationContext(), "No Items in Recyclerview", Toast.LENGTH_SHORT).show();
                    idProgressBarRelative.setVisibility(View.GONE);
                    lottieAnimationView.setVisibility(View.GONE);
                    blackHoleView.setVisibility(View.GONE);
                    blackHoleView.disableTouch(false);
                }
            }

            @Override
            public void onFailure(Call<CreateEventHistoryData> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(CreateEventHistoryActivity.this, "Time out, Please try again", Toast.LENGTH_SHORT).show();
                } else if (t instanceof IOException) {
                    Toast.makeText(CreateEventHistoryActivity.this, "Check you internet connection", Toast.LENGTH_SHORT).show();
                }
                idProgressBarRelative.setVisibility(View.GONE);
                lottieAnimationView.setVisibility(View.GONE);
                blackHoleView.setVisibility(View.GONE);
                blackHoleView.disableTouch(false);
            }
        });
    }

    private void getCreateEventHistoryPast() {
        idProgressBarRelative.setVisibility(View.VISIBLE);
        lottieAnimationView.setVisibility(View.VISIBLE);
        blackHoleView.setVisibility(View.VISIBLE);
        blackHoleView.disableTouch(true);
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://conneqt.senarios.co/content_viewer/")
                .addConverterFactory(GsonConverterFactory.create(
                        gson)).build();
        ApiServices apiPost = retrofit.create(ApiServices.class);
        SharedPreferences preferences = getSharedPreferences("my", MODE_PRIVATE);
        String signinValue = preferences.getString("apiToken","");
        Call<CreateEventHistoryData> call = apiPost.getEventHistory("Bearer " + signinValue, 56.2 , 1, "dwed", "2023-11-24 11:13:59","2 hours",30, "1");
        call.enqueue(new retrofit2.Callback<CreateEventHistoryData>() {
            @Override
            public void onResponse(Call<CreateEventHistoryData> call, Response<CreateEventHistoryData> response) {
                if (response.isSuccessful()) {
                    List<Past> list = response.body().getData().getPast();

                    if (list != null && !list.isEmpty()) {
                        idProgressBarRelative.setVisibility(View.GONE);
                        lottieAnimationView.setVisibility(View.GONE);
                        blackHoleView.setVisibility(View.GONE);
                        blackHoleView.disableTouch(false);
                        recyclerView.setVisibility(View.VISIBLE);
                        displayTxt.setVisibility(View.GONE);
                        createEventHistoryAdapterPast = new CreateEventHistoryAdapterPast(list,getApplicationContext(),CreateEventHistoryActivity.this);
                        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setAdapter(createEventHistoryAdapterPast);
                        createEventHistoryAdapterPast.notifyDataSetChanged();
                        swipeRefreshLayout.setRefreshing(false);
                    } else {
                        displayTxt.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
//                        Toast.makeText(getApplicationContext(), "No Items in Recyclerview", Toast.LENGTH_SHORT).show();
                        idProgressBarRelative.setVisibility(View.GONE);
                        lottieAnimationView.setVisibility(View.GONE);
                        blackHoleView.setVisibility(View.GONE);
                        blackHoleView.disableTouch(false);
                    }
                } else {
                    recyclerView.setVisibility(View.GONE);
//                  Toast.makeText(getApplicationContext(), "No Items in Recyclerview", Toast.LENGTH_SHORT).show();
                    idProgressBarRelative.setVisibility(View.GONE);
                    lottieAnimationView.setVisibility(View.GONE);
                    blackHoleView.setVisibility(View.GONE);
                    blackHoleView.disableTouch(false);
                }
            }

            @Override
            public void onFailure(Call<CreateEventHistoryData> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(CreateEventHistoryActivity.this, "Time out, Please try again", Toast.LENGTH_SHORT).show();
                } else if (t instanceof IOException) {
                    Toast.makeText(CreateEventHistoryActivity.this, "Check you internet connection", Toast.LENGTH_SHORT).show();
                }
                idProgressBarRelative.setVisibility(View.GONE);
                lottieAnimationView.setVisibility(View.GONE);
                blackHoleView.setVisibility(View.GONE);
                blackHoleView.disableTouch(false);
            }
        });
    }

    private void CancelStream(String Id ,String name) {
        idProgressBarRelative.setVisibility(View.VISIBLE);
        lottieAnimationView.setVisibility(View.VISIBLE);
        blackHoleView.setVisibility(View.VISIBLE);
        blackHoleView.disableTouch(true);
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://conneqt.senarios.co/content_viewer/stripe/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        ApiServices apiPost = retrofit.create(ApiServices.class);
        SharedPreferences preferences = getSharedPreferences("my", MODE_PRIVATE);
        String signinValue = preferences.getString("apiToken", "abc");
        Call<RefundModel> call = apiPost.getCancelEvent("Bearer " + signinValue , Id);
        call.enqueue(new retrofit2.Callback<RefundModel>() {
            @Override
            public void onResponse(Call<RefundModel> call, Response<RefundModel> response) {
                if (response.isSuccessful()) {
                    RefundModel resObj = response.body();

                    if (resObj.getSuccess() == true) {

                        Log.e("CancelStream", new Gson().toJson(response.body()));
                        idProgressBarRelative.setVisibility(View.GONE);
                        lottieAnimationView.setVisibility(View.GONE);
                        blackHoleView.setVisibility(View.GONE);
                        blackHoleView.disableTouch(false);
                        errorMessageLayout.setVisibility(View.VISIBLE);
                        eventcancelTxt.setText("Event"+ " " + name + " " + "was canceled");
                    } else {
                        idProgressBarRelative.setVisibility(View.GONE);
                        lottieAnimationView.setVisibility(View.GONE);
                        blackHoleView.setVisibility(View.GONE);
                        blackHoleView.disableTouch(false);
                        Toast.makeText(CreateEventHistoryActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    idProgressBarRelative.setVisibility(View.GONE);
                    lottieAnimationView.setVisibility(View.GONE);
                    blackHoleView.setVisibility(View.GONE);
                    blackHoleView.disableTouch(false);
                    Toast.makeText(getApplicationContext(), "Process Failed", Toast.LENGTH_SHORT).show();          }
            }
            @Override
            public void onFailure(Call<RefundModel> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(CreateEventHistoryActivity.this, "Time out, Please try again", Toast.LENGTH_SHORT).show();
                } else if (t instanceof IOException) {
                    Toast.makeText(CreateEventHistoryActivity.this, "Check you internet connection", Toast.LENGTH_SHORT).show();
                }
                idProgressBarRelative.setVisibility(View.GONE);
                lottieAnimationView.setVisibility(View.GONE);
                blackHoleView.setVisibility(View.GONE);
                blackHoleView.disableTouch(false);
            }
        });
    }

    @Override
    public void doStart(String id) {
        getJoinEvent(id);
    }

    @Override
    public void doCancel(String Id , String name) {
        CancelStream(Id , name);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                errorMessageLayout.setVisibility(View.GONE);
                eventcancelTxt.setText(name);
            }
        }, 5000);

    }

    private void getJoinEvent(String id) {
        idProgressBarRelative.setVisibility(View.VISIBLE);
        lottieAnimationView.setVisibility(View.VISIBLE);
        blackHoleView.setVisibility(View.VISIBLE);
        blackHoleView.disableTouch(true);
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://conneqt.senarios.co/content_viewer/")
                .addConverterFactory(GsonConverterFactory.create(
                        gson)).build();
        ApiServices apiPost = retrofit.create(ApiServices.class);
        SharedPreferences preferences = getSharedPreferences("my", MODE_PRIVATE);
        String signinValue = preferences.getString("apiToken", "abc");

        Call<StartEventModel> call = apiPost.getStartEvent("Bearer " + signinValue, Integer.valueOf(id));
        call.enqueue(new retrofit2.Callback<StartEventModel>() {
            @Override
            public void onResponse(Call<StartEventModel> call, Response<StartEventModel> response) {
                if (response.isSuccessful()) {
                    StartEventModel resObj = response.body();

                    if (resObj.getSuccess() == true) {
                        idProgressBarRelative.setVisibility(View.GONE);
                        lottieAnimationView.setVisibility(View.GONE);
                        blackHoleView.setVisibility(View.GONE);
                        blackHoleView.disableTouch(false);
                        SharedPreferences.Editor editor = getSharedPreferences("my", MODE_PRIVATE).edit();
                        editor.putString("CreatorName", response.body().getData().getCreatorFirstName());
                        editor.putString("CreatorLastName", response.body().getData().getCreatorLastName());
                        editor.putString("CreatorImage", response.body().getData().getCreatorImage());
                        editor.putInt("CreatorId", response.body().getData().getContentCreatorId());
                        editor.putInt("StreamInteraction", response.body().getData().getStreamInteraction());
                        editor.apply();
                        SharedPreferences preferences = getSharedPreferences("my", MODE_PRIVATE);
                        String signinValue = preferences.getString("signedInKey", "abc");
                        Intent intent = new Intent(CreateEventHistoryActivity.this, BroadCastingEventCreatorActivity.class);
                        intent.putExtra("Id", resObj.getData().getId().toString());
                        intent.putExtra("Agora_Token", resObj.getData().getAgoraToken().toString());
                        intent.putExtra("isGuest", !signinValue.equals("Yes"));
                        startActivity(intent);
//                      Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    } else {
                        idProgressBarRelative.setVisibility(View.GONE);
                        lottieAnimationView.setVisibility(View.GONE);
                        blackHoleView.setVisibility(View.GONE);
                        blackHoleView.disableTouch(false);
                        Toast.makeText(CreateEventHistoryActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    idProgressBarRelative.setVisibility(View.GONE);
                    lottieAnimationView.setVisibility(View.GONE);
                    blackHoleView.setVisibility(View.GONE);
                    blackHoleView.disableTouch(false);
                    Toast.makeText(CreateEventHistoryActivity.this, "Process Failed ", Toast.LENGTH_SHORT).show();
                }

            }
            @Override
            public void onFailure(Call<StartEventModel> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(CreateEventHistoryActivity.this, "Time out, Please try again", Toast.LENGTH_SHORT).show();
                } else if (t instanceof IOException) {
                    Toast.makeText(CreateEventHistoryActivity.this, "Check you internet connection", Toast.LENGTH_SHORT).show();
                }
                idProgressBarRelative.setVisibility(View.GONE);
                lottieAnimationView.setVisibility(View.GONE);
                blackHoleView.setVisibility(View.GONE);
                blackHoleView.disableTouch(false);
            }
        });

    }

    private void CompletedDialog() {
        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View inflatedView = layoutInflater.inflate(R.layout.completedpopup, null, false);
        DisplayMetrics displayMetrics = getApplicationContext().getResources().getDisplayMetrics();
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        final PopupWindow filterPopup = new PopupWindow(inflatedView, width, height, true);
        filterPopup.setHeight(WindowManager.LayoutParams.MATCH_PARENT);
        filterPopup.setOutsideTouchable(false);
        filterPopup.setOutsideTouchable(true);
        filterPopup.showAtLocation(inflatedView, Gravity.CENTER, 0, 0);
        filterPopup.getContentView().setFocusableInTouchMode(true);
        filterPopup.getContentView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    filterPopup.dismiss();
                    return true;
                }
                return false;
            }
        });

        TextView yes = inflatedView.findViewById(R.id.idEndTxt);
        TextView personName  = inflatedView.findViewById(R.id.idPersonName);
        RatingBar ratingBar = inflatedView.findViewById(R.id.rating);
        SharedPreferences preferences = getSharedPreferences("my", MODE_PRIVATE);
        String creatorName = preferences.getString("CreatorName", "abc");
        personName.setText(creatorName);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RatingStream(Double.parseDouble(String.valueOf(ratingBar.getRating())));
                filterPopup.dismiss();
            }
        });
    }

    private void CompletedStream() {
        idProgressBarRelative.setVisibility(View.VISIBLE);
        lottieAnimationView.setVisibility(View.VISIBLE);
        blackHoleView.setVisibility(View.VISIBLE);
        blackHoleView.disableTouch(true);
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://conneqt.senarios.co/content_viewer/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        ApiServices apiPost = retrofit.create(ApiServices.class);
        SharedPreferences preferences = getSharedPreferences("my", MODE_PRIVATE);
        Integer eventId = preferences.getInt("getEventsId", 0);
        Call<EventCompletedModel> call = apiPost.getCompletedEvent(eventId);
        call.enqueue(new retrofit2.Callback<EventCompletedModel>() {
            @Override
            public void onResponse(Call<EventCompletedModel> call, Response<EventCompletedModel> response) {
                if (response.isSuccessful()) {
                    EventCompletedModel resObj = response.body();

                    if (resObj.getSuccess() == true) {
                        Log.e("ReportedStream", new Gson().toJson(response.body()));
                        idProgressBarRelative.setVisibility(View.GONE);
                        lottieAnimationView.setVisibility(View.GONE);
                        blackHoleView.setVisibility(View.GONE);
                        blackHoleView.disableTouch(false);
                        CompletedDialog();
                        Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        idProgressBarRelative.setVisibility(View.GONE);
                        lottieAnimationView.setVisibility(View.GONE);
                        blackHoleView.setVisibility(View.GONE);
                        blackHoleView.disableTouch(false);
                        Toast.makeText(CreateEventHistoryActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    idProgressBarRelative.setVisibility(View.GONE);
                    lottieAnimationView.setVisibility(View.GONE);
                    blackHoleView.setVisibility(View.GONE);
                    blackHoleView.disableTouch(false);
                    Toast.makeText(getApplicationContext(), "Process Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<EventCompletedModel> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(CreateEventHistoryActivity.this, "Time out, Please try again", Toast.LENGTH_SHORT).show();
                } else if (t instanceof IOException) {
                    Toast.makeText(CreateEventHistoryActivity.this, "Check you internet connection", Toast.LENGTH_SHORT).show();
                }
                idProgressBarRelative.setVisibility(View.GONE);
                lottieAnimationView.setVisibility(View.GONE);
                blackHoleView.setVisibility(View.GONE);
                blackHoleView.disableTouch(false);
            }
        });
    }

    private void getNotificationCount() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://conneqt.senarios.co/content_viewer/notification/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        ApiServices apiServices = retrofit.create(ApiServices.class);
        SharedPreferences preferences = getSharedPreferences("my", MODE_PRIVATE);
        String signinValue = preferences.getString("apiToken", "abc");
        Call<NotificationCountModel> call = apiServices.getNotificationCount("Bearer " + signinValue);
        call.enqueue(new Callback<NotificationCountModel>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<NotificationCountModel> call, Response<NotificationCountModel> response) {
                if (response.isSuccessful()) {
                    NotificationCountModel resObj = response.body();

                    if(resObj.getSuccess()==true) {
                        if(resObj.getCount()>0) {
                            notification.setImageDrawable(getResources().getDrawable(R.drawable.ic_notificationnew));
                        } else {
                            notification.setImageDrawable(getResources().getDrawable(R.drawable.ic_notification));
                        }
                    } else {
                        notification.setImageDrawable(getResources().getDrawable(R.drawable.ic_notification));
                    }
                } else {
                    Toast.makeText(CreateEventHistoryActivity.this, "Process Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<NotificationCountModel> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(CreateEventHistoryActivity.this, "Time out, Please try again", Toast.LENGTH_SHORT).show();
                } else if (t instanceof IOException) {
                    Toast.makeText(CreateEventHistoryActivity.this, "Check you internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void RatingStream(double rank) {
        idProgressBarRelative.setVisibility(View.VISIBLE);
        lottieAnimationView.setVisibility(View.VISIBLE);
        blackHoleView.setVisibility(View.VISIBLE);
        blackHoleView.disableTouch(true);
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://conneqt.senarios.co/content_viewer/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        ApiServices apiPost = retrofit.create(ApiServices.class);
        SharedPreferences preferences = getSharedPreferences("my", MODE_PRIVATE);
        Integer ViewerId = preferences.getInt("IdUser", 0);
        Integer eventId = preferences.getInt("getEventsId", 0);
        Call<RatingModel> call = apiPost.getRating(rank, ViewerId, eventId);
        call.enqueue(new retrofit2.Callback<RatingModel>() {
            @Override
            public void onResponse(Call<RatingModel> call, Response<RatingModel> response) {
                if (response.isSuccessful()) {
                    RatingModel resObj = response.body();

                    if (resObj.getSuccess() == true) {
                        Log.e("ReportedStream", new Gson().toJson(response.body()));
                        idProgressBarRelative.setVisibility(View.GONE);
                        lottieAnimationView.setVisibility(View.GONE);
                        blackHoleView.setVisibility(View.GONE);
                        blackHoleView.disableTouch(false);
                        Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        idProgressBarRelative.setVisibility(View.GONE);
                        lottieAnimationView.setVisibility(View.GONE);
                        blackHoleView.setVisibility(View.GONE);
                        blackHoleView.disableTouch(false);
                        Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    idProgressBarRelative.setVisibility(View.GONE);
                    lottieAnimationView.setVisibility(View.GONE);
                    blackHoleView.setVisibility(View.GONE);
                    blackHoleView.disableTouch(false);
                    Toast.makeText(getApplicationContext(), "Process Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RatingModel> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(CreateEventHistoryActivity.this, "Time out, Please try again", Toast.LENGTH_SHORT).show();
                } else if (t instanceof IOException) {
                    Toast.makeText(CreateEventHistoryActivity.this, "Check you internet connection", Toast.LENGTH_SHORT).show();
                }
                idProgressBarRelative.setVisibility(View.GONE);
                lottieAnimationView.setVisibility(View.GONE);
                blackHoleView.setVisibility(View.GONE);
                blackHoleView.disableTouch(false);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences preferences = getSharedPreferences("my", MODE_PRIVATE);
        String streamEnd = preferences.getString("streamEnd", "");
         if(streamEnd!=null && !streamEnd.isEmpty() && streamEnd.equals("Yes")){
             if(upcoming) {
                 getCreateEventHistory();
             } else {
                 getCreateEventHistoryPast();
             }
//             CompletedStream();
             SharedPreferences.Editor e = getSharedPreferences("my", MODE_PRIVATE).edit();
             e.putString("streamEnd", "No");
             e.apply();

        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(CreateEventHistoryActivity.this, OverViewActivity.class));
        overridePendingTransition(0, 0);
    }
}