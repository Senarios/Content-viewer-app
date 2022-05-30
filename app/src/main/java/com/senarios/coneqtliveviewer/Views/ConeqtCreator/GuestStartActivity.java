package com.senarios.coneqtliveviewer.Views.ConeqtCreator;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;
import com.senarios.coneqtliveviewer.Model.GetSecretRes;
import com.senarios.coneqtliveviewer.Model.GuestEventModel;
import com.senarios.coneqtliveviewer.Model.GuestEventResponse;
import com.senarios.coneqtliveviewer.Model.GuestPay;
import com.senarios.coneqtliveviewer.Model.GuestStartEvent;
import com.senarios.coneqtliveviewer.Model.GuestUser;
import com.senarios.coneqtliveviewer.Model.JoinGuestModel;
import com.senarios.coneqtliveviewer.R;
import com.senarios.coneqtliveviewer.Views.ApiServices;
import com.senarios.coneqtliveviewer.Views.LoginandRegister.LoginActivity;
import com.senarios.coneqtliveviewer.Views.OverView.OverViewActivity;
import com.senarios.coneqtliveviewer.Views.TouchBlackHoleView;
import com.squareup.picasso.Picasso;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.paymentsheet.PaymentSheet;
import com.stripe.android.paymentsheet.PaymentSheetResult;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GuestStartActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView signUp, creatorName, creatorTotalEvents,
            eventTitle, purchaseTicketBtn, idTravelnew, timeTV,
            idEventHistoryName, idEventHistoryTime, idEventHistoryTimeDuration,
            idEventHistoryDescription, idSoldTicketTxt, idEventHistoryCost,
            firstLetter, idRevenueHistory, idJoinTxt , pastSoldTextView , creatorName1 , ratingTxt ;
    private ImageView creatorImage, eventImage;
    RelativeLayout idProgressBarRelative;
    LottieAnimationView lottieAnimationView;
    TouchBlackHoleView blackHoleView;
    private GuestEventModel eventModel;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    PaymentSheet.CustomerConfiguration customerConfig;
    PaymentSheet paymentSheet;
    String EventTime;
    String conv;
    RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_start);
        initLayout();
        setListeners();
        paymentSheet = new PaymentSheet(this, this::onPaymentSheetResult);
        Uri uri = getIntent().getData();
        if (uri != null) {
            String path = uri.getLastPathSegment();
            List<String> list = uri.getPathSegments();
//            guestEventId = preferences.getString("guestEventId", path);
            Log.e("none", "onCreate: " + path);
            Log.e("none", "onCreate:list " + list.toString());
            getGuestEvent(list.get(list.size() - 2), list.get(list.size() - 1));
        }
    }

    private void initLayout() {
        signUp = findViewById(R.id.createevent);
        creatorName = findViewById(R.id.idTravel);
        creatorName1 = findViewById(R.id.creatorName);
        creatorTotalEvents = findViewById(R.id.idTotalEventsprice);
        creatorImage = findViewById(R.id.idimage);
        idProgressBarRelative = findViewById(R.id.idProgressBarRelative);
        eventImage = findViewById(R.id.idcardImage);
        eventTitle = findViewById(R.id.eventTitle);
        purchaseTicketBtn = findViewById(R.id.purchaseTicketBtn);
        idTravelnew = findViewById(R.id.idTravelnew);
        timeTV = findViewById(R.id.timeTV);
        idEventHistoryName = findViewById(R.id.idEventHistoryName);
        idEventHistoryTime = findViewById(R.id.idEventHistoryTime);
        idEventHistoryTimeDuration = findViewById(R.id.idEventHistoryTimeDuration);
        idEventHistoryDescription = findViewById(R.id.idEventHistoryDescription);
        idSoldTicketTxt = findViewById(R.id.idSoldTicketTxt);
        idEventHistoryCost = findViewById(R.id.idEventHistoryCost);
        idRevenueHistory = findViewById(R.id.idRevenueHistory);
        lottieAnimationView = findViewById(R.id.lottie_main);
        blackHoleView = findViewById(R.id.blackHole);
        idJoinTxt = findViewById(R.id.idJoinTxt);
        pastSoldTextView = findViewById(R.id.pastSoldTextView);
        firstLetter = findViewById(R.id.idFirstLetter);
        ratingTxt = findViewById(R.id.ratingTxt);
        ratingBar = findViewById(R.id.rating);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences preferences = getSharedPreferences("my", MODE_PRIVATE);
        String signinValue = preferences.getString("signedInKey", "abc");
        if (signinValue.equals("Yes"))
            signUp.setVisibility(View.GONE);
    }

    private void setListeners() {
        signUp.setOnClickListener(this);
        purchaseTicketBtn.setOnClickListener(this);
        idJoinTxt.setOnClickListener(this);
    }

    private void getGuestEvent(String eventName, String creatorName) {
        idProgressBarRelative.setVisibility(View.VISIBLE);
        lottieAnimationView.setVisibility(View.VISIBLE);
        blackHoleView.setVisibility(View.VISIBLE);
        blackHoleView.disableTouch(true);
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://conneqt.senarios.co/content_creator/open/event/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        ApiServices apiPost = retrofit.create(ApiServices.class);
        retrofit2.Call<GuestEventResponse> call = apiPost.getGuestEvent(eventName, creatorName);
        call.enqueue(new Callback<GuestEventResponse>() {

            @Override
            public void onResponse(retrofit2.Call<GuestEventResponse> call, Response<GuestEventResponse> response) {
                Log.e("Success", new Gson().toJson(response.body()));
                if (response.isSuccessful()) {

                    GuestEventResponse eventResponse = response.body();

                    if (eventResponse.getSuccess()) {
                        idProgressBarRelative.setVisibility(View.GONE);
                        lottieAnimationView.setVisibility(View.GONE);
                        blackHoleView.setVisibility(View.GONE);
                        blackHoleView.disableTouch(false);
                        eventModel = eventResponse.getEventData();
                        handleResult(eventResponse);
//                        SharedPreferences preference = getSharedPreferences("my", MODE_PRIVATE);
//                        int ticprice = preference.getInt("ticPrice", 0);
//                        getSecretKey(response.body().getId(), ticprice);
//                        Toast.makeText(OverViewActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        idProgressBarRelative.setVisibility(View.GONE);
                        lottieAnimationView.setVisibility(View.GONE);
                        blackHoleView.setVisibility(View.GONE);
                        blackHoleView.disableTouch(false);
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
            public void onFailure(Call<GuestEventResponse> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(GuestStartActivity.this, "Time out, Please try again", Toast.LENGTH_SHORT).show();
                } else if (t instanceof IOException) {
                    Toast.makeText(GuestStartActivity.this, "Check you internet connection", Toast.LENGTH_SHORT).show();
                }
                idProgressBarRelative.setVisibility(View.GONE);
                lottieAnimationView.setVisibility(View.GONE);
                blackHoleView.setVisibility(View.GONE);
                blackHoleView.disableTouch(false);
            }
        });
    }

    private void handleResult(GuestEventResponse eventResponse) {

        /////////////////
        EventTime = eventResponse.getEventData().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date1 = new Date(System.currentTimeMillis());
        Date date2 = null;
        try {
            date2 = df.parse(EventTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long diff = date2.getTime() - date1.getTime();
        if(diff>0) {
            new CountDownTimer(diff, 1000) {
                public void onTick(long millisUntilFinished) {
                    long seconds = millisUntilFinished / 1000;
                    long minutes = seconds / 60;
                    long hours = minutes / 60;
                    timeTV.setText(String.format("%02d:%02d:%02d ", hours %24 , minutes%60, seconds%60));

                }
                public void onFinish() {
                }
            }.start();
            Log.wtf("message", String.valueOf(diff));
        } else {
            timeTV.setText("00:00:00");
        }
        /////////////

        if (eventResponse.getEventData().getStatus().equalsIgnoreCase("started")) {
            purchaseTicketBtn.setVisibility(View.GONE);
            idJoinTxt.setVisibility(View.VISIBLE);
        } else {
            purchaseTicketBtn.setVisibility(View.VISIBLE);
            idJoinTxt.setVisibility(View.GONE);
        }

        String image = eventResponse.getCreatorData().getImageUrl();
        if (image != null && !image.isEmpty()) {
            Glide.with(this)
                    .load(image) // image url
                    .addListener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            String test = eventResponse.getCreatorData().getFirstName().toString();
                            String test1 = eventResponse.getCreatorData().getLastName().toString();
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
                    .into(creatorImage);
            firstLetter.setVisibility(View.GONE);
        } else {
            String test = eventResponse.getCreatorData().getFirstName().toString();
            String test1 = eventResponse.getCreatorData().getLastName().toString();
            String s = test.substring(0, 1).toUpperCase();
            String s1 = test1.substring(0, 1).toUpperCase();
            firstLetter.setText(s + s1);
            firstLetter.setVisibility(View.VISIBLE);
        }

        Picasso.with(this)
                .load(eventResponse.getEventData().getImage1S3())
                .centerCrop()
                .placeholder(R.drawable.img)
                .resize(1800, 1800)
                .into(eventImage);
        eventImage.setVisibility(View.VISIBLE);

//        Glide.with(this)
//                .load(eventResponse.getEventData().getImage1S3())
//                .centerCrop()
//                .placeholder(R.drawable.img)
//                .resize(1800, 1800)
//                .into(eventImage);
        creatorName.setText(eventResponse.getCreatorData().getFirstName() + " " + eventResponse.getCreatorData().getLastName());
        creatorName1.setText(eventResponse.getCreatorData().getFirstName());
        creatorTotalEvents.setText(" "+ eventResponse.getCreatorData().getTotalCreatedEvents().toString());
        eventTitle.setText(eventResponse.getEventData().getName());
        idTravelnew.setText(eventResponse.getEventData().getType());
        idEventHistoryName.setText(eventResponse.getEventData().getName());
        pastSoldTextView.setText("("+ eventResponse.getEventData().getEventPurchasedLastHour().toString() + " in the past hr)");
        idEventHistoryDescription.setText(eventResponse.getEventData().getDescription());
        idSoldTicketTxt.setText(eventResponse.getEventData().getTotalTicketPurchased().toString());
        idEventHistoryCost.setText("£" + eventResponse.getEventData().getTicketPrice());
        ratingTxt.setText(("(" +  eventResponse.getEventData().getAvg_rating() + ")"));
        ratingBar.setRating((float) eventResponse.getEventData().getAvg_rating());

        SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date newDate;
        try {
            newDate = spf.parse(eventResponse.getEventData().getTime());
            idEventHistoryTime.setText("Time: " + getFormattedDate(newDate));

            Long time = Long.parseLong(eventResponse.getEventData().getTimeDuration().toString()) / 60;
            Long mints, hours;

            hours = TimeUnit.MINUTES.toHours(Long.valueOf(time));
            mints = time - TimeUnit.HOURS.toMinutes(hours);
            if (hours <= 0 && mints >= 1) {
                idEventHistoryTimeDuration.setText(" | " + mints + " min");
            } else if (mints <= 0 && hours >= 1) {
                idEventHistoryTimeDuration.setText(" | " + hours + " h ");
            } else if (mints <= 0 && hours >= 1) {
                idEventHistoryTimeDuration.setText(" | " + hours + " h ");
            } else {
                idEventHistoryTimeDuration.setText(" | " + hours + " h " + mints + " min");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        if (v != null) {
            if (v.getId() == R.id.createevent) {
                startActivity(new Intent(GuestStartActivity.this, LoginActivity.class));
            } else if (v.getId() == R.id.purchaseTicketBtn) {
                int amount = (int) (eventModel.getTicketPrice() * 100);
                SharedPreferences.Editor e = getSharedPreferences("my", MODE_PRIVATE).edit();
                e.putString("event_id", eventModel.getId().toString());
                e.putInt("ticPrice", amount);
                e.apply();
                SharedPreferences preferences = getSharedPreferences("my", MODE_PRIVATE);
                String signinValue = preferences.getString("signedInKey", "abc");
                String cus_id = preferences.getString("stripe_cus_id_key", "");

                if (signinValue.equals("Yes")) {
                    startActivity(new Intent(GuestStartActivity.this, OverViewActivity.class));
                    finish();
//                    getSecretKey(cus_id, amount);


//                    signUp.setVisibility(View.GONE);
//                    String idUser = preferences.getString("first_name", "abc");
//                    String LastUser = preferences.getString("last_name", "abc");
//                    String email = preferences.getString("user_email_sheet", "abc");
//                    String eventId = eventModel.getId().toString();
//                    checkTicketStatus(idUser, LastUser, email, eventId, cus_id, amount);
                } else {
                    purchasePopUp();
                }
            } else {
                SharedPreferences preferences = getSharedPreferences("my", MODE_PRIVATE);
                String signinValue = preferences.getString("signedInKey", "abc");
                if (signinValue.equals("Yes")) {
                    startActivity(new Intent(GuestStartActivity.this, CreateEventHistoryActivity.class));
                    finish();
                } else {
                    getJoinEventPopup();
                }
            }
        }
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
                    Toast.makeText(GuestStartActivity.this, "Enter number", Toast.LENGTH_SHORT).show();
                    return;
                }

                String EditText1 = editText2.getText().toString().trim();
                Log.wtf("OverViewActivity", "" + EditText1);

                if (EditText1.isEmpty()) {
                    Toast.makeText(GuestStartActivity.this, "Enter number", Toast.LENGTH_SHORT).show();
                    return;
                }

                String EditText2 = editText3.getText().toString().trim();
                Log.wtf("OverViewActivity", "" + EditText2);

                if (EditText2.isEmpty()) {
                    Toast.makeText(GuestStartActivity.this, "Enter number", Toast.LENGTH_SHORT).show();
                    return;
                }

                String EditText3 = editText4.getText().toString().trim();
                Log.wtf("OverViewActivity", "" + EditText3);

                if (EditText3.isEmpty()) {
                    Toast.makeText(GuestStartActivity.this, "Enter number", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    getJoinOtpVerify(EditText + EditText1 + EditText2 + EditText3);
//                    if (guestEventId != null && !guestEventId.isEmpty())
//                        getJoinOtpVerify(EditText + EditText1 + EditText2 + EditText3);
//                    else
//                        joinEvents(EditText + EditText1 + EditText2 + EditText3);
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

    private void getJoinOtpVerify(String otp) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://conneqt.senarios.co/content_viewer/guest/verify/otp/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        ApiServices apiPost = retrofit.create(ApiServices.class);
        SharedPreferences editor = getSharedPreferences("my", MODE_PRIVATE);
        String event_Id_user = editor.getString("guestEventId", eventModel.getId().toString());
        retrofit2.Call<JoinGuestModel> call = apiPost.joinEvents(event_Id_user, otp);
        call.enqueue(new Callback<JoinGuestModel>() {

            @Override
            public void onResponse(retrofit2.Call<JoinGuestModel> call, Response<JoinGuestModel> response) {
                Log.e("Success", new Gson().toJson(response.body()));
                if (response.isSuccessful()) {

                    JoinGuestModel joinGuestModel = response.body();

                    if (joinGuestModel.getSuccess() == true) {

                        SharedPreferences preference = getSharedPreferences("my", MODE_PRIVATE);
                        int eventId = preference.getInt("guestId", response.body().getData().getEventId());
                        int viewerId = preference.getInt("ViewerId", response.body().getData().getContentViewerId());
                        getGuestStart(eventId, viewerId);
//                        Toast.makeText(OverViewActivity.this, eventId + viewerId, Toast.LENGTH_SHORT).show();
//                        Toast.makeText(OverViewActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(GuestStartActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (response.body() != null && response.body().getMessage() != null)
                        Toast.makeText(getApplicationContext(), "Process Failed", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(GuestStartActivity.this, "Verification Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JoinGuestModel> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(GuestStartActivity.this, "Time out, Please try again", Toast.LENGTH_SHORT).show();
                } else if (t instanceof IOException) {
                    Toast.makeText(GuestStartActivity.this, "Check you internet connection", Toast.LENGTH_SHORT).show();
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

                    if (joinGuestModel != null && joinGuestModel.getSuccess()) {
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
                        Intent intent = new Intent(GuestStartActivity.this, BroadCastingEventCreatorActivity.class);
                        intent.putExtra("Id", joinGuestModel.getData().getEvent().getId().toString());
                        intent.putExtra("Agora_Token", joinGuestModel.getData().getEvent().getAgoraToken().toString());
                        intent.putExtra("isGuest", !signinValue.equals("Yes"));
                        startActivity(intent);
                        Toast.makeText(GuestStartActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        idProgressBarRelative.setVisibility(View.GONE);
                        lottieAnimationView.setVisibility(View.GONE);
                        blackHoleView.setVisibility(View.GONE);
                        blackHoleView.disableTouch(false);
                        Toast.makeText(GuestStartActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(GuestStartActivity.this, "Time out, Please try again", Toast.LENGTH_SHORT).show();
                } else if (t instanceof IOException) {
                    Toast.makeText(GuestStartActivity.this, "Check you internet connection", Toast.LENGTH_SHORT).show();
                }
                idProgressBarRelative.setVisibility(View.GONE);
                lottieAnimationView.setVisibility(View.GONE);
                blackHoleView.setVisibility(View.GONE);
                blackHoleView.disableTouch(false);
            }
        });
    }

    private void checkTicketStatus(String firstName, String lastUser, String email, String eventId, String cus_id, int amount) {
        idProgressBarRelative.setVisibility(View.VISIBLE);
        lottieAnimationView.setVisibility(View.VISIBLE);
        blackHoleView.setVisibility(View.VISIBLE);
        blackHoleView.disableTouch(true);
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://conneqt.senarios.co/content_viewer/guest/stripe/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        ApiServices apiPost = retrofit.create(ApiServices.class);
        retrofit2.Call<GuestUser> call = apiPost.getGuestUser(firstName, lastUser, email, eventId);
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

//                        SharedPreferences preference = getSharedPreferences("my", MODE_PRIVATE);
//                        int ticprice = preference.getInt("ticPrice", 0);
                        if (response.body() != null && response.body().getPurchaseStatus() != 1)
                            getSecretKey(cus_id, amount);
                        Toast.makeText(GuestStartActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        idProgressBarRelative.setVisibility(View.GONE);
                        lottieAnimationView.setVisibility(View.GONE);
                        blackHoleView.setVisibility(View.GONE);
                        blackHoleView.disableTouch(false);
                        Toast.makeText(GuestStartActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    idProgressBarRelative.setVisibility(View.GONE);
                    lottieAnimationView.setVisibility(View.GONE);
                    blackHoleView.setVisibility(View.GONE);
                    blackHoleView.disableTouch(false);
                    Toast.makeText(GuestStartActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<GuestUser> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(GuestStartActivity.this, "Time out, Please try again", Toast.LENGTH_SHORT).show();
                } else if (t instanceof IOException) {
                    Toast.makeText(GuestStartActivity.this, "Check you internet connection", Toast.LENGTH_SHORT).show();
                }
                idProgressBarRelative.setVisibility(View.GONE);
                lottieAnimationView.setVisibility(View.GONE);
                blackHoleView.setVisibility(View.GONE);
                blackHoleView.disableTouch(false);

            }
        });
    }

    private String getFormattedDate(Date date) {
        Calendar cal = Calendar.getInstance();
        Date currentTime = Calendar.getInstance().getTime();
        cal.setTime(date);
        //2nd of march 2015
        int day = cal.get(Calendar.DATE);

        switch (day % 10) {
            case 1:
                return new SimpleDateFormat( "hh:mm aa d'st' MMMM yyyy").format(date);
            case 2:
                return new SimpleDateFormat( "hh:mm aa d'nd' MMMM yyyy").format(date);
            case 3:
                return new SimpleDateFormat( "hh:mm aa d'rd' MMMM yyyy").format(date);
            default:
                return new SimpleDateFormat("hh:mm aa d'th' MMMM yyyy").format(date);
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
        SharedPreferences preferences = getSharedPreferences("apiToken", MODE_PRIVATE);
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
                        PaymentConfiguration.init(getApplicationContext(), "pk_test_51Jo7TGHC0KdocDH8hCrhVvFtlfOJN3J8ahfZeraUMngzTbqwBfowjzu7quXJFmo8qTKyEFUjs2xcEsQT0K9PoFmI00oMhjsCHF");
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
                        Toast.makeText(GuestStartActivity.this, "Process Failed ", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    idProgressBarRelative.setVisibility(View.GONE);
                    lottieAnimationView.setVisibility(View.GONE);
                    blackHoleView.setVisibility(View.GONE);
                    blackHoleView.disableTouch(false);
                    Toast.makeText(GuestStartActivity.this, "Process Failed ", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetSecretRes> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(GuestStartActivity.this, "Time out, Please try again", Toast.LENGTH_SHORT).show();
                } else if (t instanceof IOException) {
                    Toast.makeText(GuestStartActivity.this, "Check you internet connection", Toast.LENGTH_SHORT).show();
                }
                idProgressBarRelative.setVisibility(View.GONE);
                lottieAnimationView.setVisibility(View.GONE);
                blackHoleView.setVisibility(View.GONE);
                blackHoleView.disableTouch(false);
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
                        Toast.makeText(GuestStartActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        idProgressBarRelative.setVisibility(View.GONE);
                        lottieAnimationView.setVisibility(View.GONE);
                        blackHoleView.setVisibility(View.GONE);
                        blackHoleView.disableTouch(false);
                        Toast.makeText(GuestStartActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
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
            public void onFailure(Call<GuestPay> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(GuestStartActivity.this, "Time out, Please try again", Toast.LENGTH_SHORT).show();
                } else if (t instanceof IOException) {
                    Toast.makeText(GuestStartActivity.this, "Check you internet connection", Toast.LENGTH_SHORT).show();
                }
                idProgressBarRelative.setVisibility(View.GONE);
                lottieAnimationView.setVisibility(View.GONE);
                blackHoleView.setVisibility(View.GONE);
                blackHoleView.disableTouch(false);

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

        priceRadioBtn.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                amountRadioBtn.setChecked(false);
                purchaseTicketBtn.setOnClickListener(v -> startActivity(new Intent(GuestStartActivity.this, LoginActivity.class)));
            }
        });

        amountRadioBtn.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                priceRadioBtn.setChecked(false);
                purchaseTicketBtn.setOnClickListener(v -> {
                    guestPopup();
                    filterPopup.dismiss();
                });
            }
        });

        ImageView cancelBtn = inflatedView.findViewById(R.id.close);
        cancelBtn.setOnClickListener(v -> filterPopup.dismiss());

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
                    Toast.makeText(GuestStartActivity.this, "Please Enter your First Name", Toast.LENGTH_SHORT).show();
                    return;
                }
                String LastName = lastname.getText().toString().trim();
                if (LastName.isEmpty()) {
                    Toast.makeText(GuestStartActivity.this, "Please Enter your Last Name", Toast.LENGTH_SHORT).show();
                    return;
                }
                String Email = email.getText().toString().trim();
                if (Email.isEmpty()) {
                    Toast.makeText(GuestStartActivity.this, "Please Enter your Email Address", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!Email.matches(emailPattern)) {
                    Toast.makeText(GuestStartActivity.this, "invalid email", Toast.LENGTH_SHORT).show();
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
        cancelBtn.setOnClickListener(v -> filterPopup.dismiss());

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
                        Toast.makeText(GuestStartActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        idProgressBarRelative.setVisibility(View.GONE);
                        lottieAnimationView.setVisibility(View.GONE);
                        blackHoleView.setVisibility(View.GONE);
                        blackHoleView.disableTouch(false);
                        Toast.makeText(GuestStartActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(GuestStartActivity.this, "Time out, Please try again", Toast.LENGTH_SHORT).show();
                } else if (t instanceof IOException) {
                    Toast.makeText(GuestStartActivity.this, "Check you internet connection", Toast.LENGTH_SHORT).show();
                }
                idProgressBarRelative.setVisibility(View.GONE);
                lottieAnimationView.setVisibility(View.GONE);
                blackHoleView.setVisibility(View.GONE);
                blackHoleView.disableTouch(false);
            }
        });
    }

}