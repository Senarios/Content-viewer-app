package com.senarios.coneqtliveviewer.Views.OverView;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import androidx.annotation.Nullable;

import androidx.annotation.RequiresApi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.senarios.coneqtliveviewer.Adapter.EventTypeAdapter;

import com.senarios.coneqtliveviewer.Adapter.OverViewAdapter;
import com.senarios.coneqtliveviewer.Model.CreatorFilter;
import com.senarios.coneqtliveviewer.Model.Datum;
import com.senarios.coneqtliveviewer.Model.GetEventTypeDatum;
import com.senarios.coneqtliveviewer.Model.GetEventTypeRes;
import com.senarios.coneqtliveviewer.Model.GetSecretRes;
import com.senarios.coneqtliveviewer.Model.GuestJoinVerify;
import com.senarios.coneqtliveviewer.Model.GuestPay;
import com.senarios.coneqtliveviewer.Model.GuestStartEvent;
import com.senarios.coneqtliveviewer.Model.GuestUser;
import com.senarios.coneqtliveviewer.Model.JoinGuestModel;
import com.senarios.coneqtliveviewer.Model.NotificationCountModel;
import com.senarios.coneqtliveviewer.Model.OverViewFilter;
import com.senarios.coneqtliveviewer.Model.OverViewViewer;
import com.senarios.coneqtliveviewer.R;
import com.senarios.coneqtliveviewer.Views.ApiServices;
import com.senarios.coneqtliveviewer.Views.ConeqtCreator.BroadCastingEventCreatorActivity;
import com.senarios.coneqtliveviewer.Views.ConeqtCreator.CreateEventHistoryActivity;
import com.senarios.coneqtliveviewer.Views.LoginandRegister.LoginActivity;
import com.senarios.coneqtliveviewer.Views.Notification.NotificationActivity;
import com.senarios.coneqtliveviewer.Views.Settings.SettingsActivity;
import com.senarios.coneqtliveviewer.Views.TouchBlackHoleView;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.paymentsheet.PaymentSheet;
import com.stripe.android.paymentsheet.PaymentSheetResult;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OverViewActivity extends AppCompatActivity implements OverViewAdapter.PurchaseTicket, AdapterView.OnItemSelectedListener, EventTypeAdapter.GetEventIds {
    private TextView signUp;
    private ImageView notification;
    private TextView idUserName, nameTxtView, noRecord;
    PaymentSheet paymentSheet;
    int stat;
    private ImageView spinner;
    private ImageView headerImage;
    RelativeLayout idProgressBarRelative;
    LottieAnimationView lottieAnimationView;
    ProgressBar eventProgress;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    PaymentSheet.CustomerConfiguration customerConfig;
    OverViewAdapter overViewAdapter;
    RecyclerView recyclerView, eventRV;
    SwipeRefreshLayout swipeRefreshLayout;
    List<Datum> arrayList = new ArrayList<>();
    private TextView month, firstLetter, month1;
    private LinearLayout linearLayout1, linearLayout2;
    private RelativeLayout analyticsLayout;
    private TextView totalEventTxt, totalPurchaseTxt, totalCancelledTxt, totalRefundTxt;
    int offset = 0;
    String signinValue;
    NestedScrollView scrollView;
    TouchBlackHoleView blackHoleView;
    boolean isSearchfilter = false;
    List<GetEventTypeDatum> list;

    private String guestEventId;
    private boolean pinShown = false;


    String eventTime = "4";
    String time;
    String type;
    private List<String> eventTypeIdList = new ArrayList<String>();
    List<Datum> datumFilter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_over_view);
        paymentSheet = new PaymentSheet(this, this::onPaymentSheetResult);
        initLayout();
        getTypeListInPopUp();

        SharedPreferences preferences = getSharedPreferences("my", MODE_PRIVATE);
        signinValue = preferences.getString("signedInKey", "abc");
        String image = preferences.getString("imageName", "");
        String idUser = preferences.getString("first_name", "abc");
        String LastUser = preferences.getString("last_name", "abc");
        time = preferences.getString("eventTime", "");
        type = preferences.getString("eventType", "");

        if (signinValue.equals("Yes")) {
            getOverViewList(false);
            nameTxtView.setText(idUser);
            idUserName.setText("Hey " + idUser + ".");
            linearLayout1.setVisibility(View.VISIBLE);
            linearLayout2.setVisibility(View.VISIBLE);
            analyticsLayout.setVisibility(View.VISIBLE);

            if (!image.isEmpty() && image != null) {
                Glide.with(this)
                        .load(image) // image url
                        .addListener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                String test = idUser;
                                String test1 = LastUser;
                                String s = test.substring(0, 1).toUpperCase();
                                String s1 = test1.substring(0, 1).toUpperCase();
                                firstLetter.setText(s + s1);
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
                String s = test.substring(0, 1).toUpperCase();
                String s1 = test1.substring(0, 1).toUpperCase();
                firstLetter.setText(s + s1);
                firstLetter.setVisibility(View.VISIBLE);
            }
            getNotificationCount();

        } else {
            getGuestOverViewList(false);
            firstLetter.setVisibility(View.VISIBLE);
            //////// get the id through manifest
            Uri uri = getIntent().getData();
            if (uri != null) {
                String path = uri.getLastPathSegment();
                guestEventId = preferences.getString("guestEventId", path);
                Toast.makeText(OverViewActivity.this, guestEventId, Toast.LENGTH_LONG).show();
            }
        }
    }

    private void initLayout() {
        noRecord = findViewById(R.id.noRecordTxt);
        signUp = findViewById(R.id.createevent);
        notification = findViewById(R.id.notification_image);
        spinner = findViewById(R.id.spinner1);
        month = findViewById(R.id.month);
        scrollView = findViewById(R.id.scroll);
        recyclerView = findViewById(R.id.overView_RV);
        firstLetter = findViewById(R.id.idFirstLetter);
        idUserName = findViewById(R.id.idUserName);
        nameTxtView = findViewById(R.id.nameTextView);
        headerImage = findViewById(R.id.headerImage);

        /// Relative Layout OF LottieAnimation
        idProgressBarRelative = findViewById(R.id.idProgressBarRelative);
        lottieAnimationView = findViewById(R.id.lottie_main);
        linearLayout1 = findViewById(R.id.firstLinear);
        linearLayout2 = findViewById(R.id.secondLinear);
        analyticsLayout = findViewById(R.id.rel);

        totalEventTxt = findViewById(R.id.idEventTxt);
        totalPurchaseTxt = findViewById(R.id.idPurchasedTxt);
        totalCancelledTxt = findViewById(R.id.idCancelledTxt);
        totalRefundTxt = findViewById(R.id.idRefundTxt);
        blackHoleView = findViewById(R.id.blackHole);
        month1 = findViewById(R.id.month2);

        /////

        Spinner spinner = findViewById(R.id.spinner2);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.numbers, R.layout.selected_month);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        spinner.setSelection(4);

        /////

        swipeRefreshLayout = findViewById(R.id.events_swipe);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                offset = 0;
                isSearchfilter = false;
                if (signinValue.equals("Yes")) {
                    getOverViewList(true);
                } else {
                    getGuestOverViewList(true);
                }
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        allClickListener();
    }

    private void allClickListener() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.overview_bottom_nav);

        SharedPreferences preferences = getSharedPreferences("my", MODE_PRIVATE);
        String signinValue = preferences.getString("signedInKey", "abc");
        if (signinValue.equals("Yes")) {
            bottomNavigationView.setVisibility(View.VISIBLE);
            signUp.setVisibility(View.GONE);
            notification.setVisibility(View.VISIBLE);
        } else {
            bottomNavigationView.setVisibility(View.GONE);
            notification.setVisibility(View.GONE);
        }

        bottomNavigationView.setSelectedItemId(R.id.idOverView);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.idOverView:
//                        startActivity(new Intent(OverViewActivity.this, OverViewActivity.class));
//                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.idCreatteEvent:
                        startActivity(new Intent(OverViewActivity.this, CreateEventHistoryActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.idSettings:
                        startActivity(new Intent(OverViewActivity.this, SettingsActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });

        signUp.setOnClickListener(v -> startActivity(new Intent(OverViewActivity.this, LoginActivity.class)));

        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OverViewActivity.this, NotificationActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

//        spinner.setOnClickListener(v -> {
//            filterPopUp();
//        });
        month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterPopUp();
            }
        });
        scrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (v.getChildAt(v.getChildCount() - 1) != null) {
                if ((scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight())) &&
                        scrollY > oldScrollY) {
                    if (signinValue.equals("Yes") && !isSearchfilter) {
                        getOverViewList(false);
                    } else if (signinValue.equals("Yes") && isSearchfilter) {
                        getOverViewList(true);
                    } else if (isSearchfilter) {
                        getGuestOverViewList(true);
                    } else {
                        getGuestOverViewList(false);
                    }
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void getSelectedEvent(Integer ids) {
        eventTypeIdList.add(String.valueOf(ids));
        Log.wtf("eventArray", eventTypeIdList.toString());
    }

    private void getOverviewFilterToday() {
        idProgressBarRelative.setVisibility(View.VISIBLE);
        lottieAnimationView.setVisibility(View.VISIBLE);
        blackHoleView.setVisibility(View.VISIBLE);
        blackHoleView.disableTouch(true);
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://conneqt.senarios.co/content_viewer/overview/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        ApiServices apiPost = retrofit.create(ApiServices.class);
        SharedPreferences preferences = getSharedPreferences("my", MODE_PRIVATE);
        String signinValue = preferences.getString("apiToken", "abc");
        time = preferences.getString("eventTime", "");
        type = preferences.getString("eventType", "");
        offset = 0;
        if (time.isEmpty()) {
            time = eventTime;
        }
        if(type.isEmpty()) {
            type = eventTypeIdList.toString();
        }
        retrofit2.Call<OverViewFilter> call = apiPost.getOverViewFilter("Bearer " + signinValue,Integer.valueOf(time), type, offset);
        call.enqueue(new Callback<OverViewFilter>() {

            @Override
            public void onResponse(retrofit2.Call<OverViewFilter> call, Response<OverViewFilter> response) {
                Log.e("Success", new Gson().toJson(response.body()));
                if (response.isSuccessful()) {
                    OverViewFilter overViewFilter = response.body();
                    offset = overViewFilter.getOffset();
                    eventTypeIdList.clear();
                    isSearchfilter = true;
                    if (overViewFilter.getSuccess().toString().equals("true")) {
                        idProgressBarRelative.setVisibility(View.GONE);
                        lottieAnimationView.setVisibility(View.GONE);
                        blackHoleView.setVisibility(View.GONE);
                        blackHoleView.disableTouch(false);
                        if (overViewFilter.getData() != null && !overViewFilter.getData().isEmpty()) {
                            recyclerView.setVisibility(View.VISIBLE);
                            noRecord.setVisibility(View.GONE);
                            datumFilter = overViewFilter.getData();
                            getGuestOverViewList(true);


                        } else {
                            idProgressBarRelative.setVisibility(View.GONE);
                            lottieAnimationView.setVisibility(View.GONE);
                            blackHoleView.setVisibility(View.GONE);
                            blackHoleView.disableTouch(false);
                            recyclerView.setVisibility(View.GONE);
                            noRecord.setVisibility(View.VISIBLE);
//                            getGuestOverViewList(false);
                        }
                    } else {
                        idProgressBarRelative.setVisibility(View.GONE);
                        lottieAnimationView.setVisibility(View.GONE);
                        blackHoleView.setVisibility(View.GONE);
                        blackHoleView.disableTouch(false);
                        Toast.makeText(OverViewActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    eventTypeIdList.clear();
                    idProgressBarRelative.setVisibility(View.GONE);
                    lottieAnimationView.setVisibility(View.GONE);
                    blackHoleView.setVisibility(View.GONE);
                    blackHoleView.disableTouch(false);
                    Toast.makeText(OverViewActivity.this, "Process Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<OverViewFilter> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(OverViewActivity.this, "Time out, Please try again", Toast.LENGTH_SHORT).show();
                } else if (t instanceof IOException) {
                    Toast.makeText(OverViewActivity.this, "Check you internet connection", Toast.LENGTH_SHORT).show();
                }
                idProgressBarRelative.setVisibility(View.GONE);
                lottieAnimationView.setVisibility(View.GONE);
                blackHoleView.setVisibility(View.GONE);
                blackHoleView.disableTouch(false);
            }
        });
    }

    private void getGuestOverviewFilterToday() {
        idProgressBarRelative.setVisibility(View.VISIBLE);
        lottieAnimationView.setVisibility(View.VISIBLE);
        blackHoleView.setVisibility(View.VISIBLE);
        blackHoleView.disableTouch(true);
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://conneqt.senarios.co/content_viewer/guest/overview/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        ApiServices apiPost = retrofit.create(ApiServices.class);
        offset = 0;
        SharedPreferences preferences = getSharedPreferences("my", MODE_PRIVATE);
        time = preferences.getString("eventTime", "");
        type = preferences.getString("eventType", "");
        offset = 0;
        if (time.isEmpty()) {
            time = eventTime;
        }
        if(type.isEmpty()) {
            type = eventTypeIdList.toString();
        }
        retrofit2.Call<OverViewFilter> call = apiPost.getGuestOverViewFilter(Integer.valueOf(time), type, offset);
        call.enqueue(new Callback<OverViewFilter>() {

            @Override
            public void onResponse(retrofit2.Call<OverViewFilter> call, Response<OverViewFilter> response) {
                Log.e("Success", new Gson().toJson(response.body()));
                if (response.isSuccessful()) {
                    OverViewFilter overViewFilter = response.body();
                    offset = overViewFilter.getOffset();
                    eventTypeIdList.clear();
                    isSearchfilter = true;
                    if (overViewFilter.getSuccess().toString().equals("true")) {
                        idProgressBarRelative.setVisibility(View.GONE);
                        lottieAnimationView.setVisibility(View.GONE);
                        blackHoleView.setVisibility(View.GONE);
                        blackHoleView.disableTouch(false);
                        if (overViewFilter.getData() != null && !overViewFilter.getData().isEmpty()) {
                            recyclerView.setVisibility(View.VISIBLE);
                            noRecord.setVisibility(View.GONE);
                            datumFilter = overViewFilter.getData();
                            getGuestOverViewList(true);
                        } else {
                            idProgressBarRelative.setVisibility(View.GONE);
                            lottieAnimationView.setVisibility(View.GONE);
                            blackHoleView.setVisibility(View.GONE);
                            blackHoleView.disableTouch(false);
                            recyclerView.setVisibility(View.GONE);
                            noRecord.setVisibility(View.VISIBLE);
//                            getGuestOverViewList(false);
                        }
                    } else {
                        idProgressBarRelative.setVisibility(View.GONE);
                        lottieAnimationView.setVisibility(View.GONE);
                        blackHoleView.setVisibility(View.GONE);
                        blackHoleView.disableTouch(false);
                        Toast.makeText(OverViewActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    eventTypeIdList.clear();
                    idProgressBarRelative.setVisibility(View.GONE);
                    lottieAnimationView.setVisibility(View.GONE);
                    blackHoleView.setVisibility(View.GONE);
                    blackHoleView.disableTouch(false);
                    Toast.makeText(OverViewActivity.this, "Process Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<OverViewFilter> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(OverViewActivity.this, "Time out, Please try again", Toast.LENGTH_SHORT).show();
                } else if (t instanceof IOException) {
                    Toast.makeText(OverViewActivity.this, "Check you internet connection", Toast.LENGTH_SHORT).show();
                }
                idProgressBarRelative.setVisibility(View.GONE);
                lottieAnimationView.setVisibility(View.GONE);
                blackHoleView.setVisibility(View.GONE);
                blackHoleView.disableTouch(false);
            }
        });
    }

    private void getTypeListInPopUp() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://conneqt.senarios.co/api/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        ApiServices apiServices = retrofit.create(ApiServices.class);
        Call<GetEventTypeRes> call = apiServices.getEventType();
        call.enqueue(new Callback<GetEventTypeRes>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<GetEventTypeRes> call, Response<GetEventTypeRes> response) {
                if (response.isSuccessful()) {
                    GetEventTypeRes resObj = response.body();
                    list = resObj.getData();

                    if (resObj.getSuccess() == true) {

                    } else {
                        Toast.makeText(OverViewActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(OverViewActivity.this, "Process Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetEventTypeRes> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(OverViewActivity.this, "Time out, Please try again", Toast.LENGTH_SHORT).show();
                } else if (t instanceof IOException) {
                    Toast.makeText(OverViewActivity.this, "Check you internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void purchasePopUp() {
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View inflatedView = layoutInflater.inflate(R.layout.purchase_ticket_popup, null, false);
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
        Button purchaseTicketBtn;
        RadioButton priceRadioBtn, amountRadioBtn;
        purchaseTicketBtn = inflatedView.findViewById(R.id.purchaseTicketBtn);
        priceRadioBtn = inflatedView.findViewById(R.id.priceRadioBtn);
        amountRadioBtn = inflatedView.findViewById(R.id.amountRadioBtn);
        amountRadioBtn.setChecked(false);
        purchaseTicketBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OverViewActivity.this, LoginActivity.class));
            }
        });
        priceRadioBtn.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                amountRadioBtn.setChecked(false);
                purchaseTicketBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(OverViewActivity.this, LoginActivity.class));
                    }
                });
            }
        });

        amountRadioBtn.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                priceRadioBtn.setChecked(false);
                purchaseTicketBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        guestPopup();
                        filterPopup.dismiss();
                    }
                });
            }
        });


        ImageView cancelBtn = inflatedView.findViewById(R.id.close);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterPopup.dismiss();
            }
        });

    }

    private void filterPopUp() {
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View inflatedView = layoutInflater.inflate(R.layout.filter_popup, null, false);
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
        Button saveFilter;
        saveFilter = inflatedView.findViewById(R.id.saveFilterBtn);
        RadioButton todayRadioBtn, thisWeekRadioBtn, thisMonthRadioBtn, idAll;
        eventRV = inflatedView.findViewById(R.id.type_RV);
        eventProgress = inflatedView.findViewById(R.id.eventTypeProgress);
        todayRadioBtn = inflatedView.findViewById(R.id.idTodayRadioBtn);
        thisWeekRadioBtn = inflatedView.findViewById(R.id.idThisWeekRadioBtn);
        thisMonthRadioBtn = inflatedView.findViewById(R.id.idThisMonthRadioBtn);
        idAll = inflatedView.findViewById(R.id.idAll);

        ///////////
        EventTypeAdapter eventTypeAdapter = new EventTypeAdapter((ArrayList<GetEventTypeDatum>) list, OverViewActivity.this, OverViewActivity.this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(OverViewActivity.this, RecyclerView.VERTICAL, false);
        eventRV.setLayoutManager(layoutManager);
        eventRV.setAdapter(eventTypeAdapter);
        eventTypeAdapter.notifyDataSetChanged();

        ////////////
       if(eventTime != null) {
           if(eventTime.equals("1")) {
               todayRadioBtn.setChecked(true);
           } else if(eventTime.equals("2")) {
               thisWeekRadioBtn.setChecked(true);
           } else if(eventTime.equals("3")) {
               thisMonthRadioBtn.setChecked(true);
           } else {
               idAll.setChecked(true);
           }
       } else {
           idAll.setChecked(true);
       }

        todayRadioBtn.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                idAll.setChecked(false);
                thisWeekRadioBtn.setChecked(false);
                thisMonthRadioBtn.setChecked(false);
                eventTime = "1";
            }
        });

        thisWeekRadioBtn.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                idAll.setChecked(false);
                todayRadioBtn.setChecked(false);
                thisMonthRadioBtn.setChecked(false);
                eventTime = "2";
            }
        });

        thisMonthRadioBtn.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                idAll.setChecked(false);
                thisWeekRadioBtn.setChecked(false);
                todayRadioBtn.setChecked(false);
                eventTime = "3";
            }
        });

        idAll.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                thisWeekRadioBtn.setChecked(false);
                todayRadioBtn.setChecked(false);
                thisMonthRadioBtn.setChecked(false);
                eventTime = "4";
            }
        });


        saveFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences.Editor e = getSharedPreferences("my", MODE_PRIVATE).edit();
                e.putString("eventTime", eventTime);
                e.putString("eventType", String.valueOf(eventTypeIdList));
                e.apply();
                if (signinValue.equals("Yes")) {
                    getOverviewFilterToday();
                } else {
                    getGuestOverviewFilterToday();
                }
                filterPopup.dismiss();
            }
        });
        ImageView cancelBtn = inflatedView.findViewById(R.id.close);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterPopup.dismiss();
            }
        });
    }

    void onPaymentSheetResult(final PaymentSheetResult paymentSheetResult) {
        // implemented in the next steps
        if (paymentSheetResult instanceof PaymentSheetResult.Canceled) {
            Log.d("Canceled", "cancel");
        } else if (paymentSheetResult instanceof PaymentSheetResult.Failed) {
            Log.e("App", "Got error: ");
        } else if (paymentSheetResult instanceof PaymentSheetResult.Completed) {

            getGuestPay();
            Log.d("Completed", "complete");
        }
    }

    private void getSecretKey(String customerId, int ticPrice) {
        idProgressBarRelative.setVisibility(View.VISIBLE);
        lottieAnimationView.setVisibility(View.VISIBLE);
        blackHoleView.setVisibility(View.VISIBLE);
        blackHoleView.disableTouch(true);

        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://conneqt.senarios.co/api/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        ApiServices apiPost = retrofit.create(ApiServices.class);
        SharedPreferences preferences = getSharedPreferences("my", MODE_PRIVATE);
        String signinValue = preferences.getString("apiToken", "abc");

        retrofit2.Call<GetSecretRes> call = apiPost.getSecret("Bearer " + signinValue, String.valueOf(ticPrice), customerId);
        call.enqueue(new Callback<GetSecretRes>() {

            @Override
            public void onResponse(retrofit2.Call<GetSecretRes> call, Response<GetSecretRes> response) {
                Log.e("Success", new Gson().toJson(response.body()));
                if (response.isSuccessful()) {
                    idProgressBarRelative.setVisibility(View.GONE);
                    lottieAnimationView.setVisibility(View.GONE);
                    blackHoleView.setVisibility(View.GONE);
                    blackHoleView.disableTouch(false);
                    SharedPreferences.Editor e = getSharedPreferences("my", MODE_PRIVATE).edit();
                    e.putString("getId", response.body().getId());
                    e.apply();
                    GetSecretRes secretRes = response.body();

                    if (secretRes.getMessage() == true) {
                        customerConfig = new PaymentSheet.CustomerConfiguration(
                                customerId,
                                response.body().getEphemeralKeySecret()
                        );
                        PaymentConfiguration.init(getApplicationContext(), "");
                        final PaymentSheet.Configuration configuration = new PaymentSheet.Configuration.Builder("Example, Inc.")
                                .customer(customerConfig).allowsDelayedPaymentMethods(true)
                                .build();

                        paymentSheet.presentWithPaymentIntent(
                                response.body().getSecret(), configuration
                        );
                    } else {
                        idProgressBarRelative.setVisibility(View.GONE);
                        lottieAnimationView.setVisibility(View.GONE);
                        blackHoleView.setVisibility(View.GONE);
                        blackHoleView.disableTouch(false);
                        Toast.makeText(OverViewActivity.this, "Process Failed ", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    idProgressBarRelative.setVisibility(View.GONE);
                    lottieAnimationView.setVisibility(View.GONE);
                    blackHoleView.setVisibility(View.GONE);
                    blackHoleView.disableTouch(false);
                    Toast.makeText(OverViewActivity.this, "Process Failed ", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetSecretRes> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(OverViewActivity.this, "Time out, Please try again", Toast.LENGTH_SHORT).show();
                } else if (t instanceof IOException) {
                    Toast.makeText(OverViewActivity.this, "Check you internet connection", Toast.LENGTH_SHORT).show();
                }
                idProgressBarRelative.setVisibility(View.GONE);
                lottieAnimationView.setVisibility(View.GONE);
                blackHoleView.disableTouch(false);
            }
        });
    }

    private void guestPopup() {
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View inflatedView = layoutInflater.inflate(R.layout.guest_popup, null, false);
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
        EditText firstname = inflatedView.findViewById(R.id.idFirstNameEdit);
        EditText lastname = inflatedView.findViewById(R.id.idLastNameEdit);
        EditText email = inflatedView.findViewById(R.id.idEmailLogin);

        Button submitBtn;
        submitBtn = inflatedView.findViewById(R.id.submitBtn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ValidateData();

            }

            private void ValidateData() {
                String FirstName = firstname.getText().toString().trim();
                if (FirstName.isEmpty()) {
                    Toast.makeText(OverViewActivity.this, "Please Enter your First Name", Toast.LENGTH_SHORT).show();
                    return;
                }
                String LastName = lastname.getText().toString().trim();
                if (LastName.isEmpty()) {
                    Toast.makeText(OverViewActivity.this, "Please Enter your Last Name", Toast.LENGTH_SHORT).show();
                    return;
                }
                String Email = email.getText().toString().trim();
                if (Email.isEmpty()) {
                    Toast.makeText(OverViewActivity.this, "Please Enter your Email Address", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!Email.matches(emailPattern)) {
                    Toast.makeText(OverViewActivity.this, "invalid email", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    SharedPreferences.Editor e = getSharedPreferences("my", MODE_PRIVATE).edit();
                    e.putString("user_email_sheet", Email);
                    e.apply();
                    SharedPreferences preferences = getSharedPreferences("my", MODE_PRIVATE);
                    String event_Id_user = preferences.getString("event_id", "");
                    getGuestUser(FirstName, LastName, Email, event_Id_user);
                    filterPopup.dismiss();
                }
            }
        });
        ImageView cancelBtn = inflatedView.findViewById(R.id.close);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterPopup.dismiss();
            }
        });

    }

    private void getGuestUser(String firstname, String lastname, String email, String eventid) {
        idProgressBarRelative.setVisibility(View.VISIBLE);
        lottieAnimationView.setVisibility(View.VISIBLE);
        blackHoleView.setVisibility(View.VISIBLE);
        blackHoleView.disableTouch(true);
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://conneqt.senarios.co/content_viewer/guest/stripe/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        ApiServices apiPost = retrofit.create(ApiServices.class);
        retrofit2.Call<GuestUser> call = apiPost.getGuestUser(firstname, lastname, email, eventid);
        call.enqueue(new Callback<GuestUser>() {

            @Override
            public void onResponse(retrofit2.Call<GuestUser> call, Response<GuestUser> response) {
                Log.e("Success", new Gson().toJson(response.body()));
                if (response.isSuccessful()) {

                    GuestUser secretRes = response.body();

                    if (secretRes.getSuccess() == true) {
                        idProgressBarRelative.setVisibility(View.GONE);
                        lottieAnimationView.setVisibility(View.GONE);
                        blackHoleView.setVisibility(View.GONE);
                        blackHoleView.disableTouch(false);

                        SharedPreferences preference = getSharedPreferences("my", MODE_PRIVATE);
                        int ticprice = preference.getInt("ticPrice", 0);
                        if (response.body() != null && response.body().getPurchaseStatus() != 1)
                            getSecretKey(response.body().getId(), ticprice);
                        Toast.makeText(OverViewActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        idProgressBarRelative.setVisibility(View.GONE);
                        lottieAnimationView.setVisibility(View.GONE);
                        blackHoleView.setVisibility(View.GONE);
                        blackHoleView.disableTouch(false);
                        Toast.makeText(OverViewActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
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
            public void onFailure(Call<GuestUser> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(OverViewActivity.this, "Time out, Please try again", Toast.LENGTH_SHORT).show();
                } else if (t instanceof IOException) {
                    Toast.makeText(OverViewActivity.this, "Check you internet connection", Toast.LENGTH_SHORT).show();
                }
                idProgressBarRelative.setVisibility(View.GONE);
                lottieAnimationView.setVisibility(View.GONE);
                blackHoleView.setVisibility(View.GONE);
                blackHoleView.disableTouch(false);
            }
        });
    }

    private void getGuestPay() {
        idProgressBarRelative.setVisibility(View.VISIBLE);
        lottieAnimationView.setVisibility(View.VISIBLE);
        blackHoleView.setVisibility(View.VISIBLE);
        blackHoleView.disableTouch(true);
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://conneqt.senarios.co/content_viewer/stripe/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        ApiServices apiPost = retrofit.create(ApiServices.class);
        SharedPreferences editor = getSharedPreferences("my", MODE_PRIVATE);
        String user_email = editor.getString("user_email_sheet", "");
        if (user_email == null || user_email.isEmpty()) {
            user_email = editor.getString("googleEmail", "");
        }
        String payment_intent = editor.getString("getId", "");
        String event_Id_user = editor.getString("event_id", "");
        retrofit2.Call<GuestPay> call = apiPost.getGuestPay(user_email, event_Id_user, payment_intent);
        call.enqueue(new Callback<GuestPay>() {

            @Override
            public void onResponse(retrofit2.Call<GuestPay> call, Response<GuestPay> response) {
                Log.e("Success", new Gson().toJson(response.body()));
                if (response.isSuccessful()) {

                    GuestPay secretRes = response.body();

                    if (secretRes.getSuccess() == true) {
                        idProgressBarRelative.setVisibility(View.GONE);
                        lottieAnimationView.setVisibility(View.GONE);
                        blackHoleView.setVisibility(View.GONE);
                        blackHoleView.disableTouch(false);
                        Toast.makeText(OverViewActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        offset = 0;
                        isSearchfilter = false;
                        if (signinValue.equals("Yes")) {
                            getOverViewList(true);
                        } else {
                            getGuestOverViewList(true);
                        }
                    } else {
                        idProgressBarRelative.setVisibility(View.GONE);
                        lottieAnimationView.setVisibility(View.GONE);
                        blackHoleView.setVisibility(View.GONE);
                        blackHoleView.disableTouch(false);
                        Toast.makeText(OverViewActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
//                    SharedPreferences.Editor e = getSharedPreferences("my", MODE_PRIVATE).edit();
//                    e.putString("user_email_sheet", "");
//                    e.apply();
                } else {
                    idProgressBarRelative.setVisibility(View.GONE);
                    lottieAnimationView.setVisibility(View.GONE);
                    blackHoleView.setVisibility(View.GONE);
                    blackHoleView.disableTouch(false);
                    Toast.makeText(getApplicationContext(), "Process Failed", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<GuestPay> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(OverViewActivity.this, "Time out, Please try again", Toast.LENGTH_SHORT).show();
                } else if (t instanceof IOException) {
                    Toast.makeText(OverViewActivity.this, "Check you internet connection", Toast.LENGTH_SHORT).show();
                }
                idProgressBarRelative.setVisibility(View.GONE);
                lottieAnimationView.setVisibility(View.GONE);
                blackHoleView.setVisibility(View.GONE);
                blackHoleView.disableTouch(false);
            }
        });
    }

    private void getEvents() {
        if (arrayList != null) {
            recyclerView.setVisibility(View.VISIBLE);
            noRecord.setVisibility(View.GONE);
            overViewAdapter = new OverViewAdapter((ArrayList<Datum>) arrayList, getApplicationContext(), OverViewActivity.this);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(overViewAdapter);
            overViewAdapter.notifyDataSetChanged();
        } else {
            recyclerView.setVisibility(View.GONE);
            noRecord.setVisibility(View.VISIBLE);
        }
    }

    private void getGuestEvents() {
//        if (overViewAdapter==null) {

        if (arrayList != null) {
            recyclerView.setVisibility(View.VISIBLE);
            noRecord.setVisibility(View.GONE);
            overViewAdapter = new OverViewAdapter((ArrayList<Datum>) arrayList, getApplicationContext(), OverViewActivity.this);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(overViewAdapter);
            overViewAdapter.notifyDataSetChanged();
        } else {
            recyclerView.setVisibility(View.GONE);
            noRecord.setVisibility(View.VISIBLE);
        }
    }

    private void getOverViewList(boolean remove) {
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
        Call<OverViewViewer> call = apiPost.CreateOverViewRes("Bearer " + signinValue, offset);
        call.enqueue(new retrofit2.Callback<OverViewViewer>() {
            @Override
            public void onResponse(Call<OverViewViewer> call, Response<OverViewViewer> response) {
                if (response.isSuccessful()) {
                    List<Datum> list = response.body().getData();

                    if (list != null) {
                        Log.e("CreateEvent", new Gson().toJson(response.body()));
                        response.body().getOffset();
                        Log.wtf("offset", response.body().getOffset().toString());
                        idProgressBarRelative.setVisibility(View.GONE);
                        lottieAnimationView.setVisibility(View.GONE);
                        blackHoleView.setVisibility(View.GONE);
                        blackHoleView.disableTouch(false);
                        totalEventTxt.setText(response.body().getTotalEvents().toString());
                        totalPurchaseTxt.setText(response.body().getPurchased().toString());
                        totalCancelledTxt.setText(response.body().getCancelEvents().toString().trim());
                        totalRefundTxt.setText("£" + response.body().getRefund());
                        if (remove) {
                            arrayList.clear();
                        }
                        if ((list.isEmpty() || list.size() > 0) && !isSearchfilter) {
                            offset = response.body().getOffset() + 1;
                            arrayList.addAll(list);
                            getEvents();
                        } else if (datumFilter != null && !datumFilter.isEmpty() && isSearchfilter) {
                            offset = response.body().getOffset() + 1;
                            arrayList.addAll(datumFilter);
                            getEvents();
                        }

                    } else {
                        recyclerView.setVisibility(View.GONE);
                        idProgressBarRelative.setVisibility(View.GONE);
                        lottieAnimationView.setVisibility(View.GONE);
                        blackHoleView.setVisibility(View.GONE);
                        blackHoleView.disableTouch(false);
                        Toast.makeText(getApplicationContext(), "No Items in RecyclerView", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    recyclerView.setVisibility(View.GONE);
                    idProgressBarRelative.setVisibility(View.GONE);
                    lottieAnimationView.setVisibility(View.GONE);
                    blackHoleView.setVisibility(View.GONE);
                    blackHoleView.disableTouch(false);
                    Toast.makeText(getApplicationContext(), "Process Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<OverViewViewer> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(OverViewActivity.this, "Time out, Please try again", Toast.LENGTH_SHORT).show();
                } else if (t instanceof IOException) {
                    Toast.makeText(OverViewActivity.this, "Check you internet connection", Toast.LENGTH_SHORT).show();
                }
                idProgressBarRelative.setVisibility(View.GONE);
                lottieAnimationView.setVisibility(View.GONE);
                blackHoleView.setVisibility(View.GONE);
                blackHoleView.disableTouch(false);
            }
        });
    }

    private void getGuestOverViewList(boolean remove) {
        idProgressBarRelative.setVisibility(View.VISIBLE);
        lottieAnimationView.setVisibility(View.VISIBLE);
        blackHoleView.setVisibility(View.VISIBLE);
        blackHoleView.disableTouch(true);
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://conneqt.senarios.co/content_viewer/guest/")
                .addConverterFactory(GsonConverterFactory.create(
                        gson)).build();
        ApiServices apiPost = retrofit.create(ApiServices.class);
        Call<OverViewViewer> call = apiPost.GuestCreateOverView(offset);
        call.enqueue(new retrofit2.Callback<OverViewViewer>() {
            @Override
            public void onResponse(Call<OverViewViewer> call, Response<OverViewViewer> response) {
                if (response.isSuccessful()) {
                    List<Datum> list = response.body().getData();

                    if (list != null) {
                        Log.e("OverViewGuest", new Gson().toJson(response.body()));
                        response.body().getOffset();
                        Log.wtf("offset", response.body().getOffset().toString());
                        idProgressBarRelative.setVisibility(View.GONE);
                        lottieAnimationView.setVisibility(View.GONE);
                        blackHoleView.setVisibility(View.GONE);
                        blackHoleView.disableTouch(false);
                        if (remove) {
                            arrayList.clear();
                        }
                        if ((list.isEmpty() || list.size() > 0) && !isSearchfilter) {
                            offset = response.body().getOffset() + 1;
                            arrayList.addAll(list);
                            getGuestEvents();
                        } else if (datumFilter != null && !datumFilter.isEmpty() && isSearchfilter) {
                            offset = response.body().getOffset();
                            arrayList.addAll(datumFilter);
                            getGuestEvents();
                        }
                    } else {
                        recyclerView.setVisibility(View.GONE);
                        idProgressBarRelative.setVisibility(View.GONE);
                        lottieAnimationView.setVisibility(View.GONE);
                        blackHoleView.setVisibility(View.GONE);
                        blackHoleView.disableTouch(false);
                        Toast.makeText(getApplicationContext(), "No Items in RecyclerView", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    recyclerView.setVisibility(View.GONE);
                    idProgressBarRelative.setVisibility(View.GONE);
                    lottieAnimationView.setVisibility(View.GONE);
                    blackHoleView.setVisibility(View.GONE);
                    blackHoleView.disableTouch(false);
                    Toast.makeText(getApplicationContext(), "Process Failed", Toast.LENGTH_SHORT).show();
                }

                if (guestEventId != null && !guestEventId.isEmpty() && !pinShown) {
                    getJoinEventPopup();
                    pinShown = true;
                }
            }

            @Override
            public void onFailure(Call<OverViewViewer> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(OverViewActivity.this, "Time out, Please try again", Toast.LENGTH_SHORT).show();
                } else if (t instanceof IOException) {
                    Toast.makeText(OverViewActivity.this, "Check you internet connection", Toast.LENGTH_SHORT).show();
                }
                idProgressBarRelative.setVisibility(View.GONE);
                lottieAnimationView.setVisibility(View.GONE);
                blackHoleView.setVisibility(View.GONE);
                blackHoleView.disableTouch(false);

                if (guestEventId != null && !guestEventId.isEmpty() && !pinShown) {
                    getJoinEventPopup();
                    pinShown = true;
                }
            }
        });
    }

    private void getJoinEventPopup() {
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View inflatedView = layoutInflater.inflate(R.layout.guestjoinpopup, null, false);
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
        RelativeLayout errorLayout = inflatedView.findViewById(R.id.errorLayout);
        EditText editText1 = inflatedView.findViewById(R.id.editCode);
        EditText editText2 = inflatedView.findViewById(R.id.editCode1);
        EditText editText3 = inflatedView.findViewById(R.id.editCode2);
        EditText editText4 = inflatedView.findViewById(R.id.editCode3);


        editText1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count == 1) {
                    editText2.requestFocus(View.FOCUS_DOWN);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editText2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                editText3.requestFocus();
                if (count == 0) {
                    editText1.requestFocus(View.FOCUS_DOWN);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editText3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                editText4.requestFocus();
                if (count == 0) {
                    editText2.requestFocus(View.FOCUS_DOWN);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editText4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                editText4.requestFocus();
                if (count == 0) {
                    editText3.requestFocus(View.FOCUS_DOWN);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Button joinBtn = inflatedView.findViewById(R.id.idJoinBtn);

        joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String EditText = editText1.getText().toString().trim();
                Log.wtf("OverViewActivity", "" + EditText);

                if (EditText.isEmpty()) {
                    Toast.makeText(OverViewActivity.this, "Enter number", Toast.LENGTH_SHORT).show();
                    return;
                }

                String EditText1 = editText2.getText().toString().trim();
                Log.wtf("OverViewActivity", "" + EditText1);

                if (EditText1.isEmpty()) {
                    Toast.makeText(OverViewActivity.this, "Enter number", Toast.LENGTH_SHORT).show();
                    return;
                }

                String EditText2 = editText3.getText().toString().trim();
                Log.wtf("OverViewActivity", "" + EditText2);

                if (EditText2.isEmpty()) {
                    Toast.makeText(OverViewActivity.this, "Enter number", Toast.LENGTH_SHORT).show();
                    return;
                }

                String EditText3 = editText4.getText().toString().trim();
                Log.wtf("OverViewActivity", "" + EditText3);

                if (EditText3.isEmpty()) {
                    Toast.makeText(OverViewActivity.this, "Enter number", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    if (guestEventId != null && !guestEventId.isEmpty())
                        getJoinOtpVerify(EditText + EditText1 + EditText2 + EditText3);
                    else
                        joinEvents(EditText + EditText1 + EditText2 + EditText3);
                }
            }
        });


        ImageView cancelBtn = inflatedView.findViewById(R.id.close);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterPopup.dismiss();
            }
        });

    }

    private void joinEvents(String otp) {
        idProgressBarRelative.setVisibility(View.VISIBLE);
        lottieAnimationView.setVisibility(View.VISIBLE);
        blackHoleView.setVisibility(View.VISIBLE);
        blackHoleView.disableTouch(true);
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://conneqt.senarios.co/content_viewer/guest/verify/otp/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        ApiServices apiPost = retrofit.create(ApiServices.class);
        SharedPreferences editor = getSharedPreferences("my", MODE_PRIVATE);
        String event_Id_user = editor.getString("event_id", "");
        retrofit2.Call<JoinGuestModel> call = apiPost.joinEvents(event_Id_user, otp);
        call.enqueue(new Callback<JoinGuestModel>() {

            @Override
            public void onResponse(retrofit2.Call<JoinGuestModel> call, Response<JoinGuestModel> response) {
                Log.e("Success", new Gson().toJson(response.body()));
                if (response.isSuccessful()) {
                    idProgressBarRelative.setVisibility(View.GONE);
                    lottieAnimationView.setVisibility(View.GONE);
                    blackHoleView.setVisibility(View.GONE);
                    blackHoleView.disableTouch(false);
                    JoinGuestModel joinGuestModel = response.body();
                    if (joinGuestModel.getSuccess() == true) {

                        getGuestStart(Integer.parseInt(response.body().getData().getEventId().toString()), Integer.parseInt(response.body().getData().getContentViewerId().toString()));
//                      Toast.makeText(OverViewActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        idProgressBarRelative.setVisibility(View.GONE);
                        lottieAnimationView.setVisibility(View.GONE);
                        blackHoleView.disableTouch(false);
                    } else {
                        Toast.makeText(OverViewActivity.this, "Please enter valid Verification no", Toast.LENGTH_SHORT).show();
                        idProgressBarRelative.setVisibility(View.GONE);
                        lottieAnimationView.setVisibility(View.GONE);
                        blackHoleView.disableTouch(false);
                    }
                } else {
                    idProgressBarRelative.setVisibility(View.GONE);
                    lottieAnimationView.setVisibility(View.GONE);
                    blackHoleView.disableTouch(false);
                    Toast.makeText(getApplicationContext(), "Process Failed", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<JoinGuestModel> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(OverViewActivity.this, "Time out, Please try again", Toast.LENGTH_SHORT).show();
                } else if (t instanceof IOException) {
                    Toast.makeText(OverViewActivity.this, "Check you internet connection", Toast.LENGTH_SHORT).show();
                }
                idProgressBarRelative.setVisibility(View.GONE);
                lottieAnimationView.setVisibility(View.GONE);
                blackHoleView.setVisibility(View.GONE);
                blackHoleView.disableTouch(false);
            }
        });
    }

    @Override
    public void doPurchase(String id, int ticketPrice, boolean purchase) {
        SharedPreferences.Editor e = getSharedPreferences("my", MODE_PRIVATE).edit();
        e.putString("event_id", id);
        e.putInt("ticPrice", ticketPrice);
        e.apply();
        if (purchase) {
//            e.putString("event_id", id);
//            e.putInt("ticPrice", ticketPrice);
//            e.apply();
            SharedPreferences preferences = getSharedPreferences("my", MODE_PRIVATE);
            String signinValue = preferences.getString("signedInKey", "abc");
            String cus_id = preferences.getString("stripe_cus_id_key", "");

            if (signinValue.equals("Yes")) {
                getSecretKey(cus_id, ticketPrice);
            } else {
                purchasePopUp();
            }
        } else {
            getJoinEventPopup();
        }

    }

    private void getJoinOtpVerify(String otp) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://conneqt.senarios.co/content_viewer/guest/verify/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        ApiServices apiPost = retrofit.create(ApiServices.class);
        SharedPreferences editor = getSharedPreferences("my", MODE_PRIVATE);
        String event_Id_user = editor.getString("guestEventId", guestEventId);
        retrofit2.Call<GuestJoinVerify> call = apiPost.getGuestJoinVerify(event_Id_user, otp);
        call.enqueue(new Callback<GuestJoinVerify>() {

            @Override
            public void onResponse(retrofit2.Call<GuestJoinVerify> call, Response<GuestJoinVerify> response) {
                Log.e("Success", new Gson().toJson(response.body()));
                if (response.isSuccessful()) {

                    GuestJoinVerify joinGuestModel = response.body();

                    if (joinGuestModel.getSuccess() == true) {

                        SharedPreferences preference = getSharedPreferences("my", MODE_PRIVATE);
                        int eventId = preference.getInt("guestId", response.body().getData().getEventId());
                        int viewerId = preference.getInt("ViewerId", response.body().getData().getContentViewerId());
                        getGuestStart(eventId, viewerId);
//                        Toast.makeText(OverViewActivity.this, eventId + viewerId, Toast.LENGTH_SHORT).show();
//                        Toast.makeText(OverViewActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(OverViewActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Process Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GuestJoinVerify> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(OverViewActivity.this, "Time out, Please try again", Toast.LENGTH_SHORT).show();
                } else if (t instanceof IOException) {
                    Toast.makeText(OverViewActivity.this, "Check you internet connection", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void getGuestStart(Integer eventId, Integer contentId) {
        idProgressBarRelative.setVisibility(View.VISIBLE);
        lottieAnimationView.setVisibility(View.VISIBLE);
        blackHoleView.setVisibility(View.VISIBLE);
        blackHoleView.disableTouch(true);
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://conneqt.senarios.co/content_viewer/start_event/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        ApiServices apiPost = retrofit.create(ApiServices.class);
        SharedPreferences editor = getSharedPreferences("my", MODE_PRIVATE);
        retrofit2.Call<GuestStartEvent> call = apiPost.getGuestStart(eventId, contentId);
        call.enqueue(new Callback<GuestStartEvent>() {

            @Override
            public void onResponse(retrofit2.Call<GuestStartEvent> call, Response<GuestStartEvent> response) {
                Log.e("Success", new Gson().toJson(response.body()));
                if (response.isSuccessful()) {

                    GuestStartEvent joinGuestModel = response.body();

                    if (joinGuestModel.getSuccess() == true) {
                        idProgressBarRelative.setVisibility(View.GONE);
                        lottieAnimationView.setVisibility(View.GONE);
                        blackHoleView.setVisibility(View.GONE);
                        blackHoleView.disableTouch(false);
                        SharedPreferences.Editor editor = getSharedPreferences("my", MODE_PRIVATE).edit();
                        editor.putString("CreatorName", response.body().getData().getEvent().getCreatorFirstName());
                        editor.putString("CreatorLastName", response.body().getData().getEvent().getCreatorLastName());
                        editor.putString("CreatorImage", response.body().getData().getEvent().getCreatorImage());
                        editor.putInt("CreatorId", response.body().getData().getEvent().getContentCreatorId());
                        editor.putString("namePerson", response.body().getData().getEvent().getName());
                        editor.putString("nameDes", response.body().getData().getEvent().getDescription());
                        editor.putString("first_name", response.body().getData().getUser().getFirstName());
                        editor.putString("last_name", response.body().getData().getUser().getLastName());
                        editor.putInt("IdUser", contentId);
                        editor.apply();
                        SharedPreferences preferences = getSharedPreferences("my", MODE_PRIVATE);
                        String signinValue = preferences.getString("signedInKey", "abc");
//                        if (signinValue.equals("Yes"))
                        Intent intent = new Intent(OverViewActivity.this, BroadCastingEventCreatorActivity.class);
                        intent.putExtra("Id", joinGuestModel.getData().getEvent().getId().toString());
                        intent.putExtra("Agora_Token", joinGuestModel.getData().getEvent().getAgoraToken().toString());
                        intent.putExtra("isGuest", !signinValue.equals("Yes"));
                        startActivity(intent);
//                        Toast.makeText(OverViewActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        idProgressBarRelative.setVisibility(View.GONE);
                        lottieAnimationView.setVisibility(View.GONE);
                        blackHoleView.setVisibility(View.GONE);
                        blackHoleView.disableTouch(false);
                        Toast.makeText(OverViewActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
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
            public void onFailure(Call<GuestStartEvent> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(OverViewActivity.this, "Time out, Please try again", Toast.LENGTH_SHORT).show();
                } else if (t instanceof IOException) {
                    Toast.makeText(OverViewActivity.this, "Check you internet connection", Toast.LENGTH_SHORT).show();
                }
                idProgressBarRelative.setVisibility(View.GONE);
                lottieAnimationView.setVisibility(View.GONE);
                blackHoleView.setVisibility(View.GONE);
                blackHoleView.disableTouch(false);
            }
        });
    }

    private void getFilter() {
        idProgressBarRelative.setVisibility(View.VISIBLE);
        lottieAnimationView.setVisibility(View.VISIBLE);
        blackHoleView.setVisibility(View.VISIBLE);
        blackHoleView.disableTouch(true);
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://conneqt.senarios.co/content_viewer/overview/event/stats/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        ApiServices apiPost = retrofit.create(ApiServices.class);
        SharedPreferences preferences = getSharedPreferences("my", MODE_PRIVATE);
        String signinValue = preferences.getString("apiToken", "abc");
        String stating = preferences.getString("filtering", "abc");
        if (stating.equals("Today")) {
            stat = 1;
        } else if (stating.equals("This week")) {
            stat = 2;
        } else if (stating.equals("This Month")) {
            stat = 3;
        } else if (stating.equals("This Year")) {
            stat = 4;
        } else if (stating.equals("All")) {
            stat = 5;
        }

        Call<CreatorFilter> call = apiPost.getFilterList("Bearer " + signinValue, stat);
        call.enqueue(new retrofit2.Callback<CreatorFilter>() {
            @Override
            public void onResponse(Call<CreatorFilter> call, Response<CreatorFilter> response) {
                if (response.isSuccessful()) {
                    CreatorFilter resObj = response.body();
                    if (resObj.getSuccess() == true) {
                        idProgressBarRelative.setVisibility(View.GONE);
                        lottieAnimationView.setVisibility(View.GONE);
                        blackHoleView.setVisibility(View.GONE);
                        blackHoleView.disableTouch(false);
                        Log.e("FilterList", new Gson().toJson(response.body()));

                        totalEventTxt.setText(response.body().getTotalEvents().toString());
                        totalPurchaseTxt.setText("£" + response.body().getPurchased());
                        totalCancelledTxt.setText(response.body().getCancelEvents().toString());
                        totalRefundTxt.setText("£" + response.body().getRefund());
//                        Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        idProgressBarRelative.setVisibility(View.GONE);
                        lottieAnimationView.setVisibility(View.GONE);
                        blackHoleView.setVisibility(View.GONE);
                        blackHoleView.disableTouch(false);
                        Toast.makeText(OverViewActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
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
            public void onFailure(Call<CreatorFilter> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(OverViewActivity.this, "Time out, Please try again", Toast.LENGTH_SHORT).show();
                } else if (t instanceof IOException) {
                    Toast.makeText(OverViewActivity.this, "Check you internet connection", Toast.LENGTH_SHORT).show();
                }
                idProgressBarRelative.setVisibility(View.GONE);
                lottieAnimationView.setVisibility(View.GONE);
                blackHoleView.setVisibility(View.GONE);
                blackHoleView.disableTouch(false);
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        month1.setText(text);
        SharedPreferences.Editor editor = getSharedPreferences("my", MODE_PRIVATE).edit();
        editor.putString("filtering", text);
        editor.apply();
        getFilter();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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

                    if (resObj.getSuccess() == true) {
                        if (resObj.getCount() > 0) {
                            notification.setImageDrawable(getResources().getDrawable(R.drawable.ic_notificationnew));
                        } else {
                            notification.setImageDrawable(getResources().getDrawable(R.drawable.ic_notification));
                        }
                    } else {
                        notification.setImageDrawable(getResources().getDrawable(R.drawable.ic_notification));
                    }
                } else {
                    Toast.makeText(OverViewActivity.this, "Process Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<NotificationCountModel> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(OverViewActivity.this, "Time out, Please try again", Toast.LENGTH_SHORT).show();
                } else if (t instanceof IOException) {
                    Toast.makeText(OverViewActivity.this, "Check you internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        moveTaskToBack(true);
    }
}
