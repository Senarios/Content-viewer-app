package com.senarios.coneqtliveviewer.Views.ConeqtCreator;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;
import com.senarios.coneqtliveviewer.Adapter.MessageAdapter;
import com.senarios.coneqtliveviewer.Adapter.ParticipantsAdapter;
import com.senarios.coneqtliveviewer.Model.EventCompletedModel;
import com.senarios.coneqtliveviewer.Model.MessageModel;
import com.senarios.coneqtliveviewer.Model.ParticipantsModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.senarios.coneqtliveviewer.Model.RatingModel;
import com.senarios.coneqtliveviewer.Model.StreamReported;
import com.senarios.coneqtliveviewer.R;
import com.senarios.coneqtliveviewer.Views.ApiServices;
import com.senarios.coneqtliveviewer.Views.TouchBlackHoleView;


import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;
import io.agora.rtc.Constants;
import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;
import io.agora.rtc.models.ClientRoleOptions;
import io.agora.rtc.video.VideoCanvas;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class BroadCastingEventCreatorActivity extends AppCompatActivity {

    // Fill the App ID of your project generated on Agora Console.
    private String appId = "f5b328dc4dfe41c7bfbe5e216b7c515e";
    // Fill the channel name.
    private String channelName ;
    // Fill the temp token generated on Agora Console.
    private String token ;
    String LastUser;

    ArrayList<String> tempy = new ArrayList<>();
    private RtcEngine mRtcEngine;

    // Viewers id,s
    private ImageView imageViewEye,close_stream , close_stream_cross,voice;
    private TextView viewer_tv, firstLetter , firstLetter1 , streamingTxt;

    // Reactions id,s
    private EditText comments;
    private ImageView likeImg, heartImg;

    private FrameLayout fl_local, fl_remote;
    ///// send Btn
    private ImageView sendBtn , headerImage , headerImage1 ;

    private TextView nameTxtView;

    private RelativeLayout liveLayout,chatRL;
    private LinearLayout showMessageLayout,view_participants;
    private ImageView idHeaderCartBtn , personImage , idBelowArrow;

    private RelativeLayout cloneContainer, cloneContainer2, nameContainer;
    String tempAudio ="Unmute", tempVideo ="enable";

    private TextView eventNameTxt, eventDesTxt;

    private ArrayList<MessageModel> arrayList;
    private ArrayList<ParticipantsModel> participantsModelArrayList = new ArrayList<>();
    private ParticipantsAdapter participantsAdapter;
    private MessageAdapter adapter;
    private RecyclerView recyclerView;

    FirebaseDatabase firebaseDatabase;

    DatabaseReference dbref;
    String userid;
    String idUser;
    String image;
    String creatorImage;
    String creatorName;
    String creatorLastName;
    boolean isGuest = false;
    boolean isBlocked = true;
    boolean endStream = false;
    private boolean isMuted = false;

    RelativeLayout idProgressBarRelative;
    LottieAnimationView lottieAnimationView;
    TouchBlackHoleView blackHoleView;

    MessageModel messageModel;


    private final IRtcEngineEventHandler mRtcEventHandler = new IRtcEngineEventHandler() {
        @Override
        // Listen for the remote host joining the channel to get the uid of the host.
        public void onUserJoined(int uid, int elapsed) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.e("checkin", "onCreate: check2" );
                    // Call setupRemoteVideo to set the remote video view after getting uid from the onUserJoined callback.
                    setupRemoteVideo(uid);
                }
            });
        }
        @Override
        public void onUserOffline(int uid, int reason) { // Tutorial Step 7
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    endStream = true;
                    CompletedStream();
                }

            });
        }
    };

    private static final int PERMISSION_REQ_ID = 22;
    private static final String[] REQUESTED_PERMISSIONS = {
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA
    };
    int j = 0;
    boolean show = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broad_casting_event_creator);

        channelName = getIntent().getStringExtra("Id");
        token = getIntent().getStringExtra("Agora_Token");
        isGuest = getIntent().getBooleanExtra("isGuest", false);
        dbref = FirebaseDatabase.getInstance().getReference();

        if(isGuest) {
            chatRL = findViewById(R.id.line1);
            chatRL.setVisibility(View.INVISIBLE);
            streamingTxt = findViewById(R.id.streamingTxt);
            streamingTxt.setVisibility(View.VISIBLE);
            close_stream = findViewById(R.id.close_stream);
            close_stream.setVisibility(View.GONE);
        }

        if (checkSelfPermission(REQUESTED_PERMISSIONS[0], PERMISSION_REQ_ID) &&
                checkSelfPermission(REQUESTED_PERMISSIONS[1], PERMISSION_REQ_ID)) {
            channelName = getIntent().getStringExtra("Id");
            token = getIntent().getStringExtra("Agora_Token");
            initializeAndJoinChannel(token,channelName);
        }
        imageViewEye = findViewById(R.id.imageEye);
        close_stream = findViewById(R.id.close_stream);
        close_stream_cross = findViewById(R.id.close_stream_cross);
        viewer_tv = findViewById(R.id.TextViewLive);

        /// Relative Layout OF LottieAnimation
        idProgressBarRelative = findViewById(R.id.idProgressBarRelative);
        lottieAnimationView = findViewById(R.id.lottie_main);
        blackHoleView = findViewById(R.id.blackHole);

        sendBtn = findViewById(R.id.sendBtn);
        headerImage = findViewById(R.id.headerImage);
        headerImage1 = findViewById(R.id.headerImage1);
        firstLetter = findViewById(R.id.idFirstLetter);
        firstLetter1 = findViewById(R.id.idFirstLetter1);
        nameTxtView = findViewById(R.id.nameTextView);
        eventNameTxt = findViewById(R.id.idEventNameBroadcast);
        eventDesTxt = findViewById(R.id.idEventDescriptionBroadcast);
        chatRL = findViewById(R.id.line1);

        SharedPreferences preferences = getSharedPreferences("my", MODE_PRIVATE);
        String Name = preferences.getString("namePerson", "xyz");
        String Des = preferences.getString("nameDes", "xyz");

        Integer Interaction = preferences.getInt("StreamInteraction", 0);
        String intercationNhi = String.valueOf(Interaction);
        if(intercationNhi!=null && !intercationNhi.isEmpty()&& intercationNhi.equals("1")) {
            chatRL.setVisibility(View.VISIBLE);
        } else {
            chatRL.setVisibility(View.INVISIBLE);
        }
        eventNameTxt.setText(Name);
        eventDesTxt.setText(Des);

        String signinValue = preferences.getString("my", "abc");
        image = preferences.getString("imageName", "");
        idUser = preferences.getString("first_name", "xyz");
        LastUser = preferences.getString("last_name", "xyz");
        //////
        creatorName = preferences.getString("CreatorName", "");
        creatorLastName = preferences.getString("CreatorLastName", "");
        creatorImage= preferences.getString("CreatorImage", "");
        /////
        ///////// default userId and change to String.
        Integer IdUser = preferences.getInt("IdUser", 0);
        userid = Integer.toString(IdUser);

        if (signinValue.equals("Yes")) {
            nameTxtView.setText(creatorName + " " + creatorLastName);

            if(creatorImage !=null && !creatorImage.isEmpty()) {
                Glide.with(this)
                        .load(creatorImage) // image url
                        .addListener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                String test = creatorName;
                                String test1 = creatorLastName;
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
                String test = creatorName;
                String test1 = creatorLastName;
                String s=test.substring(0,1).toUpperCase();
                String s1=test1.substring(0,1).toUpperCase();
                firstLetter.setText(s+ s1);
                firstLetter.setVisibility(View.VISIBLE);
            }
        } else {
            nameTxtView.setText(creatorName + " " + creatorLastName);

            if(creatorImage !=null && !creatorImage.isEmpty()) {
                Glide.with(this)
                        .load(creatorImage) // image url
                        .addListener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                String test = creatorName;
                                String test1 = creatorLastName;
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
                String test = creatorName;
                String test1 = creatorLastName;
                String s=test.substring(0,1).toUpperCase();
                String s1=test1.substring(0,1).toUpperCase();
                firstLetter.setText(s+ s1);
                firstLetter.setVisibility(View.VISIBLE);
            }
        }


        liveLayout = findViewById(R.id.abc);
        showMessageLayout = findViewById(R.id.showMessageLayout);
        view_participants = findViewById(R.id.view_participants);
        idHeaderCartBtn = findViewById(R.id.idHeaderCartBtn);

        idHeaderCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (show) {
                    idHeaderCartBtn.setImageDrawable(getDrawable(R.drawable.ic_up_downarrow));
                    showMessageLayout.setVisibility(View.VISIBLE);
                    show = false;
                }else {
                    idHeaderCartBtn.setImageDrawable(getDrawable(R.drawable.ic_down_arrow_video));
                    showMessageLayout.setVisibility(View.GONE);
                    show = true;
                }

            }
        });

        //Reactions Initializations

        likeImg = findViewById(R.id.likeImage);
        likeImg.setVisibility(View.GONE);
        heartImg = findViewById(R.id.heartImage);

        cloneContainer = findViewById(R.id.heart);
        nameContainer = findViewById(R.id.userReactionRL);

        cloneContainer2 = findViewById(R.id.like);
        cloneContainer2.setVisibility(View.GONE);


        messageModel = new MessageModel();
        recyclerView = findViewById(R.id.live_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        final int[] size = {0};

        dbref = FirebaseDatabase.getInstance().getReference().child("streams").child(channelName).child("chat");
        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList = new ArrayList<>();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    MessageModel model = snapshot1.getValue(MessageModel.class);
                    arrayList.add(model);
                }
                if (j == 0) {
                    j = 1;
                    size[0] = arrayList.size();
                }
                if (size[0] != arrayList.size()) {
                    adapter = new MessageAdapter(arrayList, BroadCastingEventCreatorActivity.this);
                    recyclerView.setAdapter(adapter);
                    recyclerView.scrollToPosition(adapter.getItemCount() - 1);
                    adapter.notifyDataSetChanged();
                } else {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        comments = findViewById(R.id.idEdtEmployeeAddress);

        close_stream.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeDialog();
            }
        });
        close_stream_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeDialogCross();
            }
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(comments.getText().length()>0) {
                    String a = comments.getText().toString().trim();
                    if(a.trim().equals("")) {
                        Toast.makeText(BroadCastingEventCreatorActivity.this, "Enter Comment first", Toast.LENGTH_SHORT).show();
                    } else {
                        addDataToFirebase(a);
                        comments.getText().clear();
                    }
                } else {
                    Toast.makeText(BroadCastingEventCreatorActivity.this, "Enter Comment first", Toast.LENGTH_SHORT).show();
                }
            }
        });

        firebaseDatabase = FirebaseDatabase.getInstance();
        heartImg.setOnClickListener(v -> {
            addReaction("heart");
            heartOnClick("heart");
        });

        likeImg.setOnClickListener(v -> {
        });

        addViewer();
        dbref = FirebaseDatabase.getInstance().getReference().child("streams").child(channelName);
        dbref.child("status").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    try {
                        if (!String.valueOf(snapshot.child("audio").getValue()).equals(tempAudio)){
                            Toast t =Toast.makeText(BroadCastingEventCreatorActivity.this,
                                    ""+String.valueOf(snapshot.child("audio").getValue())
                                    ,Toast.LENGTH_SHORT);
                            t.setGravity(Gravity.CENTER, 0, 0);
                            t.show();
                            tempAudio = String.valueOf(snapshot.child("audio").getValue());
                        }
                        if (!String.valueOf(snapshot.child("video").getValue()).equals(tempVideo)){
                            Toast t = Toast.makeText(BroadCastingEventCreatorActivity.this,
                                    ""+String.valueOf(snapshot.child("video").getValue())
                                    ,Toast.LENGTH_SHORT);
                            t.setGravity(Gravity.CENTER, 0, 0);
                            t.show();
                            tempVideo = String.valueOf(snapshot.child("video").getValue());
                        }
                    } catch (Exception e) {
                        Log.e("TAG", e.getMessage());
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Cancel",error.getMessage());
            }
        });

        dbref = FirebaseDatabase.getInstance().getReference().child("streams").child(channelName).child("participents");
        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    int count = (int) snapshot.getChildrenCount();
                    viewer_tv.setText(String.valueOf(count));

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }

    //comments the reaction code here.

    private void addReaction(String react ) {

        Map<String, String> map = new HashMap<>();
        map.put("type", react);
        map.put("url", image );
        map.put("firstname" , idUser);
        dbref = FirebaseDatabase.getInstance().getReference().child("streams").child(channelName);
        dbref.child("reactions").push().setValue(map);
    }


    private void removeViewer() {
        Log.e("checkin", "onCreate: check4" );
        dbref.child(userid).removeValue();


    }

    private void addViewer() {
        Map<String, String> map = new HashMap<>();
        map.put("name", idUser + " " + LastUser);
        map.put("image", image );
        map.put("userId",userid);
        dbref = FirebaseDatabase.getInstance().getReference().child("streams").child(channelName).child("participents");
        dbref.child(userid).setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                leaveFromScreen();
            }
        });
    }

    private void blockDialogPopup() {
        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View inflatedView = layoutInflater.inflate(R.layout.block_popup, null, false);
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

        TextView Ok = inflatedView.findViewById(R.id.idYesEvent);
        Ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterPopup.dismiss();
//                mRtcEngine.leaveChannel();
                onBackPressed();
            }
        });
    }

    private void leaveFromScreen() {
        FirebaseDatabase.getInstance().getReference()
                .child("streams").child(channelName).child("participents").child(userid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists() && isBlocked && !endStream){
                    Log.e("checkin", "onCreate: check3" );
                    mRtcEngine.leaveChannel();
                    blockDialogPopup();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void addDataToFirebase(String message) {
        final int[] i = {0};
        messageModel.setId(Integer.parseInt(userid));
        messageModel.setLastName(LastUser);
        messageModel.setMessage(message);
        messageModel.setName(idUser);
        messageModel.setTime("2022-04-25 10:53:28");
        messageModel.setImage(image);


        dbref = FirebaseDatabase.getInstance().getReference().child("streams").child(channelName).child("chat");


        dbref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (i[0] == 0) {
                    i[0] = 1;
                    dbref.child(dbref.push().getKey()).setValue(messageModel);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void closeDialog() {
        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View inflatedView = layoutInflater.inflate(R.layout.close_popup, null, false);
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

        TextView no = inflatedView.findViewById(R.id.idNoEvent);

        TextView yes = inflatedView.findViewById(R.id.idYesEvent);

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterPopup.dismiss();
            }
        });
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterPopup.dismiss();
                ReportedStream();
                onBackPressed();
//                mRtcEngine.leaveChannel();
            }
        });
    }

    private void closeDialogCross() {
        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View inflatedView = layoutInflater.inflate(R.layout.broadcast_closepopup, null, false);
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

        RatingBar ratingBar = inflatedView.findViewById(R.id.rating);


        TextView no = inflatedView.findViewById(R.id.idCancelTxt);

        TextView yes = inflatedView.findViewById(R.id.idEndTxt);

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterPopup.dismiss();
            }
        });
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                Log.wtf("rating", String.valueOf(ratingBar.getRating()));
                RatingStream(Double.parseDouble(String.valueOf(ratingBar.getRating())));
                filterPopup.dismiss();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        isBlocked = false;
        mRtcEngine.leaveChannel();

    }

    private boolean checkSelfPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(this, permission) !=
                PackageManager.PERMISSION_GRANTED) {
            Log.e("checkin", "onCreate: permission1" );
            ActivityCompat.requestPermissions(this, REQUESTED_PERMISSIONS, requestCode);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==22) {
            Log.e("checkin", "onCreate: permission2" );
            if(grantResults[0]== PackageManager.PERMISSION_GRANTED) {
                channelName = getIntent().getStringExtra("Id");
                token = getIntent().getStringExtra("Agora_Token");
                initializeAndJoinChannel(token , channelName);

            }
            Log.e("checkin", "onCreate: permission3" );
        }
        Log.e("checkin", "onCreate: permission4" );
    }

    private void initializeAndJoinChannel(String token, String channelName) {
        Log.e("checkin", "onCreate: check" );
        try {
            mRtcEngine = RtcEngine.create(getBaseContext(), appId, mRtcEventHandler);
        } catch (Exception e) {
            throw new RuntimeException("Check the error.");
        }

        // For a live streaming scenario, set the channel profile as BROADCASTING.
        mRtcEngine.setChannelProfile(Constants.CHANNEL_PROFILE_LIVE_BROADCASTING);
        // Set the client role as AUDIENCE and the latency level as low latency.
        ClientRoleOptions clientRoleOptions = new ClientRoleOptions();
        clientRoleOptions.audienceLatencyLevel = Constants.CHANNEL_PROFILE_LIVE_BROADCASTING;
        mRtcEngine.setClientRole(Constants.CLIENT_ROLE_AUDIENCE, clientRoleOptions);

        // By default, video is disabled, and you need to call enableVideo to start a video stream.
        mRtcEngine.enableVideo();

//        FrameLayout container = findViewById(R.id.local_video_view_container);
//        // Call CreateRendererView to create a SurfaceView object and add it as a child to the FrameLayout.
//        SurfaceView surfaceView = RtcEngine.CreateRendererView(getBaseContext());
//        container.addView(surfaceView);
//        // Pass the SurfaceView object to Agora so that it renders the local video.
//        mRtcEngine.setupLocalVideo(new VideoCanvas(surfaceView, VideoCanvas.RENDER_MODE_FILL, 0));

        // Join the channel with a token.
        mRtcEngine.joinChannel(token, channelName, "", 0);
    }

    private void setupRemoteVideo(int uid) {
        FrameLayout container = findViewById(R.id.remote_video_view_container);
        SurfaceView surfaceView = RtcEngine.CreateRendererView(getBaseContext());
        surfaceView.setZOrderMediaOverlay(true);
        container.addView(surfaceView);
        mRtcEngine.setupRemoteVideo(new VideoCanvas(surfaceView, VideoCanvas.RENDER_MODE_FILL, uid));
    }

    private void heartOnClick(String tag) {
        disableAllParentsClip(tag.equals("heart") ? heartImg : likeImg);
        ImageView imageClone = cloneImage(tag);
        if(image !=null && !image.isEmpty()) {
            CircleImageView myImage = myCloneImage(tag);
            animateFlying(imageClone, myImage, null);
            animateFading(imageClone, myImage, null);
        } else {
            TextView nameText = myCloneName();
            animateFlying(imageClone, null, nameText);
            animateFading(imageClone, null, nameText);
        }

    }

    private ImageView cloneImage(String tag) {
        ImageView clone = new ImageView(BroadCastingEventCreatorActivity.this);
        clone.setLayoutParams(tag.equals("heart") ? heartImg.getLayoutParams() : likeImg.getLayoutParams());
        clone.setImageDrawable(tag.equals("heart") ? getResources().getDrawable(R.drawable.ic_heartbreak) : getResources().getDrawable(R.drawable.ic_thumb));
        if (tag.equals("heart")) {
            cloneContainer.addView(clone);
        } else {
            cloneContainer2.addView(clone);
        }
        return clone;
    }

    private CircleImageView myCloneImage(String tag) {
        CircleImageView clone = new CircleImageView(BroadCastingEventCreatorActivity.this);
        clone.setLayoutParams(tag.equals("heart") ? heartImg.getLayoutParams() : likeImg.getLayoutParams());

        if(image !=null && !image.isEmpty()) {
            Glide.with(this)
                    .load(image) // image url
                    .addListener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            String test = idUser;
                            String s=test.substring(0,1).toUpperCase();
                            firstLetter1.setText(s);
                            firstLetter1.setVisibility(View.VISIBLE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    })
                    .into(clone);
            firstLetter1.setVisibility(View.GONE);
        } else {
            String test = idUser;
            String s=test.substring(0,1).toUpperCase();
            firstLetter1.setText(s);
            firstLetter1.setVisibility(View.VISIBLE);
        }
        if (tag.equals("heart")) {
            cloneContainer.addView(clone);
        } else {
            cloneContainer2.addView(clone);
        }
        return clone;
    }

    private TextView myCloneName() {
        TextView cloneText = new TextView(BroadCastingEventCreatorActivity.this);
        cloneText.setLayoutParams(heartImg.getLayoutParams());
        cloneText.setBackground(firstLetter1.getBackground());
        cloneText.setGravity(firstLetter1.getGravity());
        cloneText.setTextColor(ContextCompat.getColor(this,R.color.white));
        String test = idUser;
        String s=test.substring(0,1).toUpperCase();
        cloneText.setText(s);
        cloneText.setVisibility(View.VISIBLE);
        cloneContainer.addView(cloneText);
        return cloneText;
    }

    private void animateFading(ImageView imageView, CircleImageView myImage, TextView cloneText) {
        imageView.animate()
                .alpha(0f)
                .setDuration(2500)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        cloneContainer.removeView(imageView);
                    }
                });

        if (myImage != null) {
            myImage.animate()
                    .alpha(0f)
                    .setDuration(2500)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            cloneContainer.removeView(myImage);
                        }
                    });
        } else {
            cloneText.animate()
                    .alpha(0f)
                    .setDuration(2500)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            cloneContainer.removeView(cloneText);
                        }
                    });
        }
    }

    private void animateFlying(ImageView imageView, CircleImageView myImage, TextView cloneText) {
        float x = 0f;
        float y = 0f;
        int r = new Random().nextInt(5000 - 1000) + 5000;
        float angle = 25f;

        Path path = new Path();
//        if (r % 2 == 0) {
//            path.addArc(new RectF(x, y - r, x + 2 * r, y + r), 180f, angle);
//        } else {
//            path.addArc(new RectF(x - 2 * r, y - r, x, y + r), 0f, -angle);
//        }
        path.addArc(new RectF(x - 4 * r, y - r, x, y + r), 0f, -angle);


        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(imageView, View.X, View.Y, path);
        objectAnimator.setDuration(8000);
        objectAnimator.start();

        ObjectAnimator objectAnimator2;
        if (myImage != null) {
            objectAnimator2 = ObjectAnimator.ofFloat(myImage, View.X, View.Y, path);
        } else {
            objectAnimator2 = ObjectAnimator.ofFloat(cloneText, View.X, View.Y, path);
        }
        objectAnimator2.setDuration(5000);
        objectAnimator2.start();

    }

    private void disableAllParentsClip(View view) {
        if (view.getParent() != null) {
            while (view.getParent() instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) view.getParent();
                viewGroup.setClipChildren(false);
                viewGroup.setClipToPadding(false);
                view = viewGroup;

            }
        }

    }

    private void ReportedStream() {
        idProgressBarRelative.setVisibility(View.VISIBLE);
        lottieAnimationView.setVisibility(View.VISIBLE);
        blackHoleView.setVisibility(View.VISIBLE);
        blackHoleView.disableTouch(true);
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://conneqt.senarios.co/content_viewer/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        ApiServices apiPost = retrofit.create(ApiServices.class);
        SharedPreferences preferences = getSharedPreferences("my", MODE_PRIVATE);
        String signinValue = preferences.getString("apiToken", "abc");
        Integer CreatorId = preferences.getInt("CreatorId", 0);
        Integer eventId = preferences.getInt("getEventsId", 0);
        Integer ViewerId = preferences.getInt("IdUser", 0);
        Call<StreamReported> call = apiPost.getStreamReported("Bearer " + signinValue, CreatorId, eventId, ViewerId);
        call.enqueue(new retrofit2.Callback<StreamReported>() {
            @Override
            public void onResponse(Call<StreamReported> call, Response<StreamReported> response) {
                if (response.isSuccessful()) {
                    StreamReported resObj = response.body();

                    if (resObj.getSuccess() == true) {
                        Log.e("ReportedStream", new Gson().toJson(response.body()));
                        idProgressBarRelative.setVisibility(View.GONE);
                        lottieAnimationView.setVisibility(View.GONE);
                        blackHoleView.setVisibility(View.GONE);
                        blackHoleView.disableTouch(false);
                        startActivity(new Intent(BroadCastingEventCreatorActivity.this, CreateEventHistoryActivity.class));
                        Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        idProgressBarRelative.setVisibility(View.GONE);
                        lottieAnimationView.setVisibility(View.GONE);
                        blackHoleView.setVisibility(View.GONE);
                        blackHoleView.disableTouch(false);
                        Toast.makeText(BroadCastingEventCreatorActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
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
            public void onFailure(Call<StreamReported> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(BroadCastingEventCreatorActivity.this, "Time out, Please try again", Toast.LENGTH_SHORT).show();
                } else if (t instanceof IOException) {
                    Toast.makeText(BroadCastingEventCreatorActivity.this, "Check you internet connection", Toast.LENGTH_SHORT).show();
                }
                idProgressBarRelative.setVisibility(View.GONE);
                lottieAnimationView.setVisibility(View.GONE);
                blackHoleView.setVisibility(View.GONE);
                blackHoleView.disableTouch(false);
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
                        Toast.makeText(BroadCastingEventCreatorActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(BroadCastingEventCreatorActivity.this, "Time out, Please try again", Toast.LENGTH_SHORT).show();
                } else if (t instanceof IOException) {
                    Toast.makeText(BroadCastingEventCreatorActivity.this, "Check you internet connection", Toast.LENGTH_SHORT).show();
                }
                idProgressBarRelative.setVisibility(View.GONE);
                lottieAnimationView.setVisibility(View.GONE);
                blackHoleView.setVisibility(View.GONE);
                blackHoleView.disableTouch(false);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mRtcEngine !=null) {
            mRtcEngine.leaveChannel();
            mRtcEngine.destroy();
            removeViewer();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences.Editor e = getSharedPreferences("my", MODE_PRIVATE).edit();
        e.putString("streamEnd", "Yes");
        e.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        addViewer();
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
                mRtcEngine.leaveChannel();
                onBackPressed();
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
                        Toast.makeText(BroadCastingEventCreatorActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(BroadCastingEventCreatorActivity.this, "Time out, Please try again", Toast.LENGTH_SHORT).show();
                } else if (t instanceof IOException) {
                    Toast.makeText(BroadCastingEventCreatorActivity.this, "Check you internet connection", Toast.LENGTH_SHORT).show();
                }
                idProgressBarRelative.setVisibility(View.GONE);
                lottieAnimationView.setVisibility(View.GONE);
                blackHoleView.setVisibility(View.GONE);
                blackHoleView.disableTouch(false);
            }
        });
    }


}
