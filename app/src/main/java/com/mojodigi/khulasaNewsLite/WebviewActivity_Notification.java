package com.mojodigi.khulasaNewsLite;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.RenderProcessGoneDetail;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.mojodigi.khulasaNewsLite.AddsUtility.AddConstants;
import com.mojodigi.khulasaNewsLite.AddsUtility.AddMobUtils;
import com.mojodigi.khulasaNewsLite.AddsUtility.JsonParser;
import com.mojodigi.khulasaNewsLite.AddsUtility.OkhttpMethods;
import com.mojodigi.khulasaNewsLite.AddsUtility.SharedPreferenceUtil;
import com.mojodigi.khulasaNewsLite.firebase.pushUtility;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;




public class WebviewActivity_Notification extends AppCompatActivity {



    WebView webview;

    @SuppressLint("JavascriptInterface")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        webview = (WebView) findViewById(R.id.webview01);

        webview.setWebViewClient(new myWebClient());
        webview.getSettings().setJavaScriptEnabled(true);

        webview.getSettings().setDomStorageEnabled(true);

        webview.setWebContentsDebuggingEnabled(true);

        webview.addJavascriptInterface(new WebAppInterface(this), "Android");

        String url=getIntent().getStringExtra(pushUtility.UrlKey);

        if(url!=null)
        {
            Log.d("URL",url);
            openUrlInChromeOrdefaultBrowser(url);
            finish();
        }

        /*if(url!=null)
        webview.loadUrl(url);
        else
            Toast.makeText(this, "can't load the page ", Toast.LENGTH_SHORT).show();*/

        WebChromeClass webChromeClient=new WebChromeClass();

    }
    private void openUrlInChromeOrdefaultBrowser(String  url)
    {
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.setPackage("com.android.chrome");
        try {
            startActivity(i);
        } catch (ActivityNotFoundException e) {
            // Chrome is probably not installed
            // Try with the default browser
            i.setPackage(null);
            startActivity(i);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();




    }

    public class myWebClient extends WebViewClient
    {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            Log.d("12345",url);
            super.onPageStarted(view, url, favicon);

        }

        @Override
        public boolean onRenderProcessGone(WebView view, RenderProcessGoneDetail detail) {


            return super.onRenderProcessGone(view, detail);

        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            // CustomProgressDialog.dismiss();

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            CustomProgressDialog.dismiss();
            super.onPageFinished(view, url);
            Log.e("onpagefinish", url);

        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.d("098765",url);
            // CustomProgressDialog.show(WebviewActivity.this,"Loading");
            view.loadUrl(url);
            return true;

        }

    }

    // To handle "Back" key press event for WebView to go back to previous screen.
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webview.canGoBack()) {
            webview.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    public class WebChromeClass extends WebChromeClient {


    }










}