package com.senarios.coneqtliveviewer.Views;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.senarios.coneqtliveviewer.Controller.SessionManager;
import com.senarios.coneqtliveviewer.R;
import com.senarios.coneqtliveviewer.Views.LoginandRegister.StartupActivity;
import com.senarios.coneqtliveviewer.Views.OverView.OverViewActivity;

import java.util.Timer;

public class SplashActivitie extends AppCompatActivity {
    ImageView imageView;
    Animation uptodown,downtoup;
    Timer timer;

    //shared preference
    private SharedPreferences mPrefs;
    private static final String PREFS_NAME="PrefsFileUser";

    SessionManager session;

    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_splash_activitie);
        imageView= findViewById(R.id.contLogo);
//        uptodown = AnimationUtils.loadAnimation(SplashActivitie.this, R.anim.uptodown);
//        downtoup = AnimationUtils.loadAnimation(SplashActivitie.this, R.anim.downtoup);
//
//        imageView.setAnimation(uptodown);
          initLayout();

    }
        private void initLayout(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                AuthSession authSession = new AuthSession(SplashActivitie.this);
                String email = authSession.getSession();
                if (!email.equals("abc")) {
                    startActivity(new Intent(SplashActivitie.this, OverViewActivity.class));
                    finish();
                } else {
                    startActivity(new Intent(SplashActivitie.this, StartupActivity.class));
                    finish();
                }
            }
        },3000);

        }

//        private void getPreferencesData(){
//            SharedPreferences sp = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
//            if (sp.contains("Pref_Agree_StartUp")){
//                if (session.getUserInfo().get("Email") == "FishItUser") {
//                    Log.wtf("Splash", "getUserInfo -> null");
//                    session.createLoginSession("FishItUser");
//                    timer=new Timer();
//                    timer.schedule(new TimerTask() {
//                        @Override
//                        public void run() {
//                            Intent intent=new Intent(SplashActivitie.this, LoginActivity.class);
//                            startActivity(intent);
//                            finish();
//                        }
//                    },3000);
//                }
//                else {
//                    timer=new Timer();
//                    timer.schedule(new TimerTask() {
//                        @Override
//                        public void run() {
//                            startActivity(new Intent(SplashActivitie.this, StartedActivity.class));
////                    startActivity(intent);
//                            finish();
//                        }
//                    },3000);
//                    Log.wtf("Terms Issue ->","Nobody Agree Terms");
//                }
//            }
//
//        }

    }