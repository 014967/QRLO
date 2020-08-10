package com.example.qrlo;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

public class AddressWebViewActivity extends AppCompatActivity {

    private final static String serverUrl = "https://qrlo-798fd.web.app/daum.html";
    private final static String AppName = "QRLO";

    private WebView browser;

    @SuppressLint({"SetJavaScriptEnabled", "AddJavascriptInterface"})
    class MyJavaScriptInterface
    {
        @JavascriptInterface
        @SuppressWarnings("unused")
        public void processDATA(String data, String data2, String data3) {
            Bundle extra = new Bundle();
            Intent intent = new Intent();
            extra.putString("data", data + " " + data2 + " " + data3);
            intent.putExtras(extra);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    @SuppressLint({"SetJavaScriptEnabled", "AddJavascriptInterface"})
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_web_view);

        browser = findViewById(R.id.webView);
        browser.getSettings().setJavaScriptEnabled(true);
        browser.addJavascriptInterface(new MyJavaScriptInterface(), "QRLO");

        browser.loadUrl("https://qrlo-798fd.web.app/daum.html");
    }
}