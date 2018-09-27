package com.aospstudio.android.apps.mediabook;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

public class dd extends AppCompatActivity {

    WebView meraWeb;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.privacy_main);

        meraWeb = findViewById(R.id.meraWeb);
        meraWeb.loadUrl("https://aospstudio.org/en/terms/version/20180111/index");
        meraWeb.getSettings().setPluginState(WebSettings.PluginState.ON);
        meraWeb.getSettings().setJavaScriptEnabled(true);
        meraWeb.getSettings().setUserAgentString("Mozilla/5.0 (Linux; Android " + Build.VERSION.RELEASE + "; " + Build.MODEL + " Build/" + Build.ID + ") AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.158 Mobile Safari/537.36 OPR/47.1.2249.129326");
        meraWeb.getSettings().setUseWideViewPort(true);
        meraWeb.getSettings().setLoadWithOverviewMode(true);
        meraWeb.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        meraWeb.getSettings().setSupportZoom(true);
        meraWeb.getSettings().setBuiltInZoomControls(true);
        meraWeb.getSettings().setDisplayZoomControls(false);
        meraWeb.setVerticalScrollBarEnabled(false);
        meraWeb.setHorizontalScrollBarEnabled(false);
    }
}
