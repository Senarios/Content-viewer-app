package com.senarios.coneqtliveviewer.stripe;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.senarios.coneqtliveviewer.R;
import com.senarios.coneqtliveviewer.Views.ApiServices;
import com.senarios.coneqtliveviewer.stripe.Model.AccountLinksRes;
import com.senarios.coneqtliveviewer.stripe.Model.AccountsInfo;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StripePaymentActivity extends AppCompatActivity {
    private EditText connectId, email, country;

    private Button signupBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stripe_payment);
        connectId = findViewById(R.id.custom);
        email = findViewById(R.id.email);
        country = findViewById(R.id.country);
        signupBtn = findViewById(R.id.signBTn);
        signupBtn.setOnClickListener(view -> {
            String id1 = connectId.getText().toString().trim();
            Log.wtf("Main activity", "id1" + id1);

            String email1 = email.getText().toString().trim();
            Log.wtf("Main activity", "email1" + email1);

            String country1 = country.getText().toString().trim();
            Log.wtf("Main activity", "country1" + country1);

            signupUser(id1, country1, email1);
            Log.wtf("Main activity", "signupBtn" + signupBtn);
        });
    }

    private void signupUser(String type, String country, String email) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://api.stripe.com/v1/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        ApiServices apiPost = retrofit.create(ApiServices.class);
        Call<AccountsInfo> call = apiPost.createUser("Bearer sk_test_51Jo7TGHC0KdocDH8NFpknppOflgFxc89Ws2JNh6zrwju47t8fw0Z2cH2VlLGF9fmYBEleUUAsFjZLKsWVdTEHjUp004NvPQKKG", type, country, email,true,true);
        call.enqueue(new retrofit2.Callback<AccountsInfo>() {
            @Override
            public void onResponse(Call<AccountsInfo> call, Response<AccountsInfo> response) {
                Log.e("Success", new Gson().toJson(response.body()));

                accountLink(response.body().getId(), "account_onboarding", "https://conneqt.senarios.co/","https://conneqt.senarios.co/");

                Toast.makeText(getApplicationContext(), "successfully added", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<AccountsInfo> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "error in failure", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void accountLink(String account, String type , String returnUrl , String refreshUrl) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://api.stripe.com/v1/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        ApiServices apiPost = retrofit.create(ApiServices.class);
        Call<AccountLinksRes> call = apiPost.accountLink("Bearer sk_test_51Jo7TGHC0KdocDH8NFpknppOflgFxc89Ws2JNh6zrwju47t8fw0Z2cH2VlLGF9fmYBEleUUAsFjZLKsWVdTEHjUp004NvPQKKG",account,type,returnUrl,refreshUrl);
        call.enqueue(new retrofit2.Callback<AccountLinksRes>() {
            @Override
            public void onResponse(Call<AccountLinksRes> call, Response<AccountLinksRes> response) {
                Log.e("AccountSuccess", new Gson().toJson(response.body()));
                startActivity(new Intent(StripePaymentActivity.this,CreateStripeAccountActivity.class).putExtra("url",response.body().getUrl()));

            }

            @Override
            public void onFailure(Call<AccountLinksRes> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "error in failure ", Toast.LENGTH_SHORT).show();

            }
        });

    }


}