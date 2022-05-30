package com.senarios.coneqtliveviewer.Views.LoginandRegister;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.messaging.FirebaseMessaging;
import com.senarios.coneqtliveviewer.Model.Data;
import com.senarios.coneqtliveviewer.Model.Login;
import com.senarios.coneqtliveviewer.Model.SocialLogin;
import com.senarios.coneqtliveviewer.R;
import com.senarios.coneqtliveviewer.Views.ApiServices;
import com.senarios.coneqtliveviewer.Views.ConeqtCreator.BroadCastingEventCreatorActivity;
import com.senarios.coneqtliveviewer.Views.OverView.OverViewActivity;
import com.google.gson.Gson;
import com.senarios.coneqtliveviewer.Views.TouchBlackHoleView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEdit, passEdit;
    private TextView forgetTxt, signupTxt;
    private Button loginBtn;
    RelativeLayout idProgressBarRelative;
    private ImageView appleImage, googleImage, facebookImage, idGmailLoginImageViewLogin, showPasswordIcon;
    private boolean passwordVisible;
    private ImageView imgGoogle, imgFacebook;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[0-9])" +
            "(?=.*[a-z])" +
            "(?=.*[A-Z])" +
            "(?=.*[@#$%^&+=])" +
            "(?=\\S+$)." +
            "{8,15}" +
            "$");
    LottieAnimationView lottieAnimationView;
    private CallbackManager callbackManager;
    private FirebaseAuth mAuth;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    private static final int RC_SIGN_IN = 1;
    TouchBlackHoleView blackHoleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initLayout();
        mAuth = FirebaseAuth.getInstance();
        callbackManager = CallbackManager.Factory.create();
        facebookLoginAccess();
        getFcmToken();
    }

    private void initLayout() {

        ///// editText initialization
        emailEdit = findViewById(R.id.idEmailLoginFishIt);
        passEdit = findViewById(R.id.idPasswordLoginFishIt);
        idGmailLoginImageViewLogin = findViewById(R.id.idGmailLoginImageViewLogin);

        /////  TextView initialization

        forgetTxt = findViewById(R.id.idForgetPassTvLogin);
        signupTxt = findViewById(R.id.idLoginTerms2);

        /////  Button initialization
        loginBtn = findViewById(R.id.idLoginBtnLogin);

        /////// ImageView initialization
        appleImage = findViewById(R.id.idGmailLoginImageViewLogin);

        facebookImage = findViewById(R.id.idfbLoginImageViewLogin);

        googleImage = findViewById(R.id.idPhoneLoginImageViewLogin);

        // Password showing Eye Icon Initialization
        showPasswordIcon = findViewById(R.id.showPasswordIcon);

        //// Social Login
        googleLoginAccess();
        imgGoogle = findViewById(R.id.idGoogleLogin);
        imgFacebook = findViewById(R.id.idFacebookLogin);

        idProgressBarRelative = findViewById(R.id.idProgressBarRelative);
        lottieAnimationView = findViewById(R.id.lottie_main);
        blackHoleView = findViewById(R.id.blackHole);


        allClickListener();
    }

    private boolean isShown = false;

    @SuppressLint("ClickableViewAccessibility")
    private void allClickListener() {
        loginBtn.setOnClickListener(v -> {

            validateData();

//            UserSession user = new UserSession(Email, passEdit);
//            AuthSession authSession = new AuthSession(LoginActivity.this);
//            authSession.saveSession(user);
//            startActivity(new Intent(LoginActivity.this, WelcomeEvent.class));
        });

        signupTxt.setOnClickListener(view ->
                startActivity(new Intent(LoginActivity.this, SignupActivity.class)));

        forgetTxt.setOnClickListener(v ->
                startActivity(new Intent(LoginActivity.this, ForgetActivity.class)));

        showPasswordIcon.setOnClickListener(v -> {
            if (isShown) {
                showPasswordIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_eyeicon_pass));
                passEdit.setTransformationMethod(new PasswordTransformationMethod());
                passEdit.setSelection(passEdit.getText().length());
                isShown = false;
            } else {
                showPasswordIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_resetpasswordeye));
                passEdit.setTransformationMethod(null);
                passEdit.setSelection(passEdit.getText().length());
                isShown = true;
            }
        });

        imgGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                googleSignIn();
            }
        });

        imgFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile", "email"));
            }
        });

    }

    private void googleLoginAccess() {
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        gsc = GoogleSignIn.getClient(this, gso);

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(LoginActivity.this);
        if (account != null) {
            startActivity(new Intent(LoginActivity.this, OverViewActivity.class));
        }

    }

    private void googleSignIn() {
        Intent signInIntent = gsc.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.wtf("FirebaseWithGoogleId", account.getId() + account.getDisplayName() + account.getGivenName().toString().trim()+ account.getEmail());
                SharedPreferences preferences = getSharedPreferences("fcmtoken", MODE_PRIVATE);
                String token = preferences.getString("fcmToken", "abc");
                getSocialLogin(account.getGivenName(), account.getFamilyName(), account.getId(), account.getEmail(),"google", String.valueOf(account.getPhotoUrl()), token);
                Toast.makeText(this, "google sign in succeed", Toast.LENGTH_SHORT).show();

            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                Toast.makeText(this, "google sign in failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void facebookLoginAccess() {
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {

                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        AccessToken accessToken = AccessToken.getCurrentAccessToken();
                        AccessToken.getCurrentAccessToken().getPermissions();
                        GraphRequest request = GraphRequest.newMeRequest(
                                accessToken,
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(
                                            JSONObject object,
                                            GraphResponse response) {
                                        {
                                            try {
                                                String fullName = object.getString("name");
                                                Log.wtf("fullName", fullName);
                                                String email = object.getString("email");
                                                Log.wtf("Email", email);
                                                String Id = object.getString("id");
                                                Log.wtf("id", Id);
                                                String url = object.getJSONObject("picture").getJSONObject("data").getString("url");
                                                Log.wtf("Url", url);
                                                SharedPreferences preferences = getSharedPreferences("fcmtoken", MODE_PRIVATE);
                                                String token = preferences.getString("fcmToken", "abc");
                                                getSocialLogin(fullName,fullName, Id ,email ,"facebook", url, token);

                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                                            // Application code
                                        }
                                    }
                                });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id, email, name,link,picture.type(large)");
                        request.setParameters(parameters);
                        request.executeAsync();
                        handleFacebookAccessToken(loginResult.getAccessToken());
                        finish();

                    }

                    @Override
                    public void onCancel() {
                        Log.d(TAG, "on Cancel");

                    }

                    @Override
                    public void onError(@NonNull FacebookException e) {

                    }
                });

    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            Toast.makeText(LoginActivity.this, "sign up successfully", Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    //    private void firebaseAuthWithGoogle(String idToken) {
//        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
//        mAuth.signInWithCredential(credential)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            // Sign in success, update UI with the signed-in user's information
//                            Log.wtf("Tag","signInWithCredential:success" );
//                            Toast.makeText(LoginActivity.this, "sign up with credential successfully", Toast.LENGTH_SHORT).show();
//                            FirebaseUser user = mAuth.getCurrentUser();
//
//                        } else {
//                            // If sign in fails, display a message to the user.
//                            Toast.makeText(LoginActivity.this, "signinfailed", Toast.LENGTH_SHORT).show();
//                            Log.w(TAG, "signInWithCredential:failure", task.getException());
//                            Toast.makeText(LoginActivity.this, "sign up with credential Failed", Toast.LENGTH_SHORT).show();
//
//
//                        }
//                    }
//                });
//    }
    private void validateData() {

        String Email = emailEdit.getText().toString().trim();
        Log.wtf("SignUpActivity", "" + Email);

        if (Email.isEmpty()) {
            Toast.makeText(LoginActivity.this, "Enter email", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Email.matches(emailPattern)) {
            Toast.makeText(LoginActivity.this, "invalid email", Toast.LENGTH_SHORT).show();
            return;
        }
        String Password = passEdit.getText().toString().trim();
        Log.wtf("SignUpActivity", "" + Password);

        if (Password.isEmpty()) {
            Toast.makeText(LoginActivity.this, "Enter Password", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!PASSWORD_PATTERN.matcher(Password).matches()) {
            Toast.makeText(LoginActivity.this, "Password must have minimum 8 characters at least 1 Uppercase Alphabet, 1 Lowercase Alphabet, 1 Number and 1 Special Character", Toast.LENGTH_LONG).show();
            return;
        }

        if (Password.length() < 8) {
            Toast.makeText(LoginActivity.this, "Password too short", Toast.LENGTH_SHORT).show();
            return;
        }

        if (Password.length() > 15) {
            Toast.makeText(LoginActivity.this, "Password too long", Toast.LENGTH_SHORT).show();
            return;
        } else {
//            UserSession user = new UserSession(Email, Password);
//            AuthSession authSession = new AuthSession(LoginActivity.this);
//            authSession.saveSession(user);
            SharedPreferences preferences = getSharedPreferences("fcmtoken", MODE_PRIVATE);
            String token = preferences.getString("fcmToken", "abc");
            signUpUser(Email, Password, token);
        }
    }

    private void signUpUser(String email, String password, String token) {
        idProgressBarRelative.setVisibility(View.VISIBLE);
        lottieAnimationView.setVisibility(View.VISIBLE);
        blackHoleView.setVisibility(View.VISIBLE);
        blackHoleView.disableTouch(true);

        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://conneqt.senarios.co/content_viewer/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        ApiServices apiPost = retrofit.create(ApiServices.class);
        Call<Login> call = apiPost.LoginUser(email, password, token);
        call.enqueue(new retrofit2.Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                if (response.isSuccessful()) {
                    Login resObj = response.body();

                    List<Data> list = Collections.singletonList(resObj.getData());

                    if (resObj.getSuccess() == true) {
                        Log.e("SignUpUser", new Gson().toJson(response.body()));
                        idProgressBarRelative.setVisibility(View.GONE);
                        lottieAnimationView.setVisibility(View.GONE);
                        blackHoleView.setVisibility(View.GONE);
                        blackHoleView.disableTouch(false);

                        SharedPreferences.Editor e = getSharedPreferences("my", MODE_PRIVATE).edit();
                        e.putString("signedInKey", "Yes");
                        e.putInt("IdUser", response.body().getData().getId());
                        e.putString("user_email_sheet", email);
                        e.putString("user_password", password);
                        e.putString("apiToken", response.body().getData().getApiToken());
                        e.putString("stripe_cus_id_key", response.body().getData().getStripe_customer_id());
                        e.putString("first_name", response.body().getData().getFirstName());
                        e.putString("last_name", response.body().getData().getLastName());
                        e.putString("phone_no", response.body().getData().getPhone());
                        e.putString("imageName", response.body().getData().getImageUrl1());
                        e.apply();
                        Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, OverViewActivity.class));
                        finish();
                    } else {
                        idProgressBarRelative.setVisibility(View.GONE);
                        lottieAnimationView.setVisibility(View.GONE);
                        blackHoleView.setVisibility(View.GONE);
                        blackHoleView.disableTouch(false);
                        Toast.makeText(LoginActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

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
            public void onFailure(Call<Login> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(LoginActivity.this, "Time out, Please try again", Toast.LENGTH_SHORT).show();
                } else if (t instanceof IOException) {
                    Toast.makeText(LoginActivity.this, "Check you internet connection", Toast.LENGTH_SHORT).show();
                }
                idProgressBarRelative.setVisibility(View.GONE);
                lottieAnimationView.setVisibility(View.GONE);
                blackHoleView.setVisibility(View.GONE);
                blackHoleView.disableTouch(false);
            }
        });

    }

    private void getSocialLogin(String firstname, String lastname, String email, String email1, String auth_type, String image, String token) {
        idProgressBarRelative.setVisibility(View.VISIBLE);
        lottieAnimationView.setVisibility(View.VISIBLE);
        blackHoleView.setVisibility(View.VISIBLE);
        blackHoleView.disableTouch(true);
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://conneqt.senarios.co/content_viewer/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        ApiServices apiPost = retrofit.create(ApiServices.class);
        Call<SocialLogin> call = apiPost.getSocialLogin(firstname, lastname, email, email1, auth_type, image, token);
        call.enqueue(new retrofit2.Callback<SocialLogin>() {
            @Override
            public void onResponse(Call<SocialLogin> call, Response<SocialLogin> response) {
                if (response.isSuccessful()) {
                    SocialLogin resObj = response.body();
                    Log.e("getSocialLogin", new Gson().toJson(response.body()));

                    if (resObj.getSuccess() == true) {

                        idProgressBarRelative.setVisibility(View.GONE);
                        lottieAnimationView.setVisibility(View.GONE);
                        blackHoleView.setVisibility(View.GONE);
                        blackHoleView.disableTouch(false);

                        SharedPreferences.Editor e = getSharedPreferences("my", MODE_PRIVATE).edit();
                        e.putString("signedInKey", "Yes");
                        e.putString("googleEmail", response.body().getData().getEmail());
                        e.putInt("IdUser", response.body().getData().getId());
                        e.putString("apiToken", response.body().getData().getApiToken());
                        e.putString("stripe_cus_id_key", response.body().getData().getStripeCustomerId());
                        e.putString("first_name", firstname);
                        e.putString("last_name", lastname);
                        e.putString("phone_no", response.body().getData().getPhone());
                        e.putString("imageName", response.body().getData().getImageUrl());
                        e.apply();
                        Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, OverViewActivity.class));
                        finish();

                    } else {
                        idProgressBarRelative.setVisibility(View.GONE);
                        lottieAnimationView.setVisibility(View.GONE);
                        blackHoleView.setVisibility(View.GONE);
                        blackHoleView.disableTouch(false);
                        Toast.makeText(LoginActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
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
            public void onFailure(Call<SocialLogin> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(LoginActivity.this, "Time out, Please try again", Toast.LENGTH_SHORT).show();
                } else if (t instanceof IOException) {
                    Toast.makeText(LoginActivity.this, "Check you internet connection", Toast.LENGTH_SHORT).show();
                }
                idProgressBarRelative.setVisibility(View.GONE);
                lottieAnimationView.setVisibility(View.GONE);
                blackHoleView.setVisibility(View.GONE);
                blackHoleView.disableTouch(false);
            }
        });

    }

    private void getFcmToken() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();
//                        Toast.makeText(getApplicationContext(), token, Toast.LENGTH_SHORT).show();
                        SharedPreferences.Editor editor = getSharedPreferences("fcmtoken", MODE_PRIVATE).edit();
                        editor.putString("fcmToken", token);
                        editor.apply();

                    }
                });
    }
}

