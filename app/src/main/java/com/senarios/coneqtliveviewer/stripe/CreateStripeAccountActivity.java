package com.senarios.coneqtliveviewer.stripe;

import android.os.Bundle;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

import com.senarios.coneqtliveviewer.R;


public class CreateStripeAccountActivity extends AppCompatActivity {
    private WebView webView;
    private String WebLink;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_stripe_account);
        webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        WebLink = getIntent().getStringExtra("url");
        webView.loadUrl(WebLink);
    }
}