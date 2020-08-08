package com.example.qrlo;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

public class AddressWebViewActivity extends AppCompatActivity {

    private final static String serverUrl = "http://qrlo-798fd.firebaseapp.com/daum.html";

    private WebView browser;

    @SuppressLint({"SetJavaScriptEnabled", "AddJavascriptInterface"})
    class MyJavaScriptInterface
    {
        @JavascriptInterface
        @SuppressWarnings("unused")
        public void processDATA(String data) {
            Bundle extra = new Bundle();
            Intent intent = new Intent();
            extra.putString("data", data);
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
        browser.addJavascriptInterface(new MyJavaScriptInterface(), "Android");

        // 필요 없는 부분 같음
        /*
        browser.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                browser.loadUrl("javascript:sample2_execDaumPostcode();");
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                browser.loadUrl("javascript:sample2_execDaumPostcode();");
                browser.loadUrl("javascript:testEcho('Hello World!')");
                browser.post(new Runnable() {
                    @Override
                    public void run() {
                        browser.loadUrl("javascript:sample2_execDaumPostcode();");
                    }
                });
                Log.d("log", "asd");
            }
        });

         */

        browser.loadUrl(serverUrl);
    }
}