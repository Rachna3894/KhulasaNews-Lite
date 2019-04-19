package com.mojodigi.khulasaNewsLite;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdView;
import com.mojodigi.khulasaNewsLite.AddsUtility.AddConstants;
import com.mojodigi.khulasaNewsLite.AddsUtility.AddMobUtils;
import com.mojodigi.khulasaNewsLite.AddsUtility.JsonParser;
import com.mojodigi.khulasaNewsLite.AddsUtility.OkhttpMethods;
import com.mojodigi.khulasaNewsLite.AddsUtility.SharedPreferenceUtil;
//import com.smaato.soma.AdDownloaderInterface;
//import com.smaato.soma.AdListenerInterface;
//import com.smaato.soma.BannerView;
//import com.smaato.soma.ErrorCode;
//import com.smaato.soma.ReceivedBannerInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


public class WebviewActivity extends AppCompatActivity {

    BroadcastReceiver internetChangerReceiver;

    SharedPreferenceUtil addprefs;
    View adContainer;
    RelativeLayout smaaToAddContainer;
    //smaatoAddBanerView
    //BannerView smaaTobannerView;


    WebView webview;
    private AdView mAdView;
    private  Context mContext;


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

        // webview.getSettings().setAllowContentAccess(true);
        //webview.getSettings().setAllowFileAccess(true);
        // webview.getSettings().setAllowFileAccessFromFileURLs(true);
        // webview.getSettings().setAllowUniversalAccessFromFileURLs(true);
        // webview.getSettings().setAppCacheEnabled(true);
        // webview.getSettings().setBlockNetworkImage(false);



        webview.loadUrl("https://m.khulasa-news.com/");
        // webview.loadUrl("https://gyanparkash.in/");

        //https://gyanparkash.in/

        WebChromeClass webChromeClient=new WebChromeClass();
        //webview.setWebChromeClient(webChromeClient);

        // CustomProgressDialog.show(WebviewActivity.this,"Loading");



        //add netwrk varibales

        mContext = WebviewActivity.this;

        mAdView=findViewById(R.id.adView);
        adContainer = findViewById(R.id.adMobView);
        smaaToAddContainer = findViewById(R.id.smaaToAddContainer);

//        smaaTobannerView = new BannerView((this).getApplication());
//        smaaTobannerView.addAdListener(this);

        addprefs = new SharedPreferenceUtil(mContext);

        AddMobUtils adutil = new AddMobUtils();

        if(AddConstants.checkIsOnline(mContext) && adContainer !=null && addprefs !=null)
        {
            String AddPrioverId=addprefs.getStringValue(AddConstants.ADD_PROVIDER_ID, AddConstants.NOT_FOUND);
            if(AddPrioverId.equalsIgnoreCase(AddConstants.Adsense_Admob_GooglePrivideId)) {
                adutil.displayServerBannerAdd(addprefs,adContainer , mContext);
            }
            else if(AddPrioverId.equalsIgnoreCase(AddConstants.SmaatoProvideId))
            {
                try {
                    int publisherId = Integer.parseInt(addprefs.getStringValue(AddConstants.APP_ID, AddConstants.NOT_FOUND));
                    int addSpaceId = Integer.parseInt(addprefs.getStringValue(AddConstants.BANNER_ADD_ID, AddConstants.NOT_FOUND));
                    //adutil.displaySmaatoBannerAdd(smaaTobannerView, smaaToAddContainer, publisherId, addSpaceId);
                }catch (Exception e)
                {
                    String string = e.getMessage();
                    System.out.print(""+string);
                }
            }
            else if(AddPrioverId.equalsIgnoreCase(AddConstants.FaceBookAddProividerId))
            {
                //adutil.dispFacebookBannerAdd(mContext,addprefs , WebviewActivity.this);
            }

        }
        else {
            adutil.displayLocalBannerAdd(mAdView);
        }




        // banner add



        // this broadcast  will  listen the  internet state change for sendig request  when internet becomes available
        IntentFilter intentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        internetChangerReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                boolean isNetworkAvailable = AddConstants.checkIsOnline(WebviewActivity.this);

                // Toast.makeText(context, "isNetworkAvailable-->" + isNetworkAvailable, Toast.LENGTH_SHORT).show();

                Log.d("isNetworkAvailable", "" + isNetworkAvailable);
                if (isNetworkAvailable) {
                    new WebCall().execute();

                } else {
                    if (mAdView != null && addprefs != null) {
                        AddMobUtils util = new AddMobUtils();
                        util.displayLocalBannerAdd(mAdView);


                    }
                }
            }

        };
        registerReceiver(internetChangerReceiver, intentFilter);
        // this broadcast  will  listen the  internet state change for sendig request  when internet becomes available


    }

    @Override
    protected void onResume() {
        super.onResume();




    }

    /* @Override
     public void onReceiveAd(AdDownloaderInterface adDownloaderInterface, ReceivedBannerInterface receivedBanner) {
         if(receivedBanner.getErrorCode() != ErrorCode.NO_ERROR){
             //Toast.makeText(getBaseContext(), receivedBanner.getErrorMessage(), Toast.LENGTH_SHORT).show();
             Log.d("SmaatoErrorMsg", ""+receivedBanner.getErrorMessage());
             if(receivedBanner.getErrorMessage().equalsIgnoreCase(AddConstants.NO_ADDS))
             {
                 smaaToAddContainer.setVisibility(View.GONE);
             }
         }
     }
*/


    public class myWebClient extends WebViewClient
    {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
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


    public class WebCall extends AsyncTask<String,String,String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... strings) {

            try {
                JSONObject requestObj=AddConstants.prepareAddJsonRequest(WebviewActivity.this, AddConstants.VENDOR_ID);
                return OkhttpMethods.CallApi(WebviewActivity.this,AddConstants.API_URL,requestObj.toString());
            } catch (IOException e) {
                e.printStackTrace();
                return ""+e.getMessage();
            }
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.d("JsonResponse", s);

            if (addprefs != null)
            {
                int responseCode = addprefs.getIntValue(AddConstants.API_RESPONSE_CODE, 0);

                if (s != null  && responseCode==200 ) {
                    try {

                        JSONObject mainJson = new JSONObject(s);
                        if (mainJson.has("status")) {
                            String status = JsonParser.getkeyValue_Str(mainJson, "status");

                            String newVersion = JsonParser.getkeyValue_Str(mainJson,"appVersion");
                            //Log.e("Json Version " , newVersion+"");
                            addprefs.setValue(AddConstants.APP_VERSION, newVersion);
                            //addprefs.setValue(AddConstants.APP_VERSION, "1.1");

                            if (status.equalsIgnoreCase("true")) {

                                String adShow = JsonParser.getkeyValue_Str(mainJson, "AdShow");

                                if (adShow.equalsIgnoreCase("true")) {
                                    if (mainJson.has("data")) {
                                        JSONObject dataJson = mainJson.getJSONObject("data");

                                        String show_Add = JsonParser.getkeyValue_Str(mainJson, "AdShow");

                                        String adProviderId =JsonParser.getkeyValue_Str(dataJson, "adProviderId");
                                        String adProviderName = JsonParser.getkeyValue_Str(dataJson, "adProviderName");
                                        String appId_PublisherId = JsonParser.getkeyValue_Str(dataJson, "appId_PublisherId");
                                        String bannerAdId = JsonParser.getkeyValue_Str(dataJson, "bannerAdId");
                                        String interstitialAdId = JsonParser.getkeyValue_Str(dataJson, "interstitialAdId");
                                        String videoAdId = JsonParser.getkeyValue_Str(dataJson, "videoAdId");
                                        Log.d("AddiDs", adProviderName + " ==" + appId_PublisherId + "==" + bannerAdId + "==" + interstitialAdId + "==" + videoAdId);




                                        try {
                                            addprefs.setValue(AddConstants.SHOW_ADD, Boolean.parseBoolean(show_Add));
                                        }catch (Exception e)
                                        {
                                            // IN CASE OF EXCEPTION CONSIDER  FALSE AS THE VALUE WILL NOT BE TRUE,FALSE.
                                            addprefs.setValue(AddConstants.SHOW_ADD, false);
                                        }

                                        addprefs.setValue(AddConstants.ADD_PROVIDER_ID, adProviderId);
                                        addprefs.setValue(AddConstants.APP_ID, appId_PublisherId);
                                        addprefs.setValue(AddConstants.BANNER_ADD_ID, bannerAdId);
                                        addprefs.setValue(AddConstants.INTERESTIAL_ADD_ID, interstitialAdId);
                                        addprefs.setValue(AddConstants.VIDEO_ADD_ID, videoAdId);
                                        AddMobUtils util=new AddMobUtils();
                                        if (adContainer != null  && adProviderId.equalsIgnoreCase(AddConstants.Adsense_Admob_GooglePrivideId))

                                        {
                                            // requst googleAdd

                                            util.displayServerBannerAdd(addprefs, adContainer, WebviewActivity.this);
                                            // util.showInterstitial(addprefs,HomeActivity.this, interstitialAdId);
                                            //util.displayRewaredVideoAdd(addprefs,WebviewActivity.this, videoAdId);
                                        }
                                        else if (adProviderId.equalsIgnoreCase(AddConstants.InMobiProvideId))
                                        {
                                            // no inmobi Adds in this version



                                        }
                                           /* else if( smaaTobannerView !=null && adProviderId.equalsIgnoreCase(AddConstants.SmaatoProvideId))
                                            {
                                                //requestSmaatoBanerAdds


                                                try {
                                                    int publisherId = Integer.parseInt(addprefs.getStringValue(AddConstants.APP_ID, AddConstants.NOT_FOUND));
                                                    int addSpaceId = Integer.parseInt(addprefs.getStringValue(AddConstants.BANNER_ADD_ID, AddConstants.NOT_FOUND));
                                                    util.displaySmaatoBannerAdd(smaaTobannerView, smaaToAddContainer, publisherId, addSpaceId);
                                                }catch (Exception e)
                                                {
                                                    String string = e.getMessage();
                                                    System.out.print(""+string);
                                                }


//

                                            }*/
                                        else  if(adProviderId.equalsIgnoreCase(AddConstants.FaceBookAddProividerId))
                                        {
                                            // util.dispFacebookBannerAdd(mContext,addprefs , WebviewActivity.this);
                                        }



                                    } else {
                                        String message = JsonParser.getkeyValue_Str(mainJson, "message");
                                        Log.d("message", "" + message);
                                    }
                                } else {
                                    String message = JsonParser.getkeyValue_Str(mainJson, "message");

                                    Log.d("message", "" + message);
                                }
                            }

                            //call dispUpdateDialog
                            dispUpdateDialog();

                        }

                    } catch (JSONException e) {
                        Log.d("jsonParse", "error while parsing json -->" + e.getMessage());
                        e.printStackTrace();
                    }


                } else {
                    // display loccal AddiDs Adds;
                    if (mAdView != null) {
                        AddMobUtils util = new AddMobUtils();
                        util.displayLocalBannerAdd(mAdView);
                        //util.showInterstitial(addprefs,HomeActivity.this, null);
                        util.displayRewaredVideoAdd(addprefs,WebviewActivity.this, null);
                    }
                }


            }

        }
    }




    private void dispUpdateDialog() {
        try {
            String currentVersion = "0";
            String newVersion="0";
            if(addprefs!=null)
                newVersion=addprefs.getStringValue(AddConstants.APP_VERSION, AddConstants.NOT_FOUND);

            try {
                currentVersion = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
                Log.d("currentVersion", "" + currentVersion);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

            if (Float.parseFloat(newVersion) > Float.parseFloat(currentVersion) && !newVersion.equalsIgnoreCase("0"))

            {
                if (mContext != null) {
                    final Dialog dialog = new Dialog(mContext);
                    dialog.setContentView(R.layout.dialog_version_update);
                    long time = addprefs.getLongValue("displayedTime", 0);
                    long diff = 86400000; // one day
                    //long diff=60000; // one minute;

                    if (time < System.currentTimeMillis() - diff) {
                        dialog.show();
                        addprefs.setValue("displayedTime", System.currentTimeMillis());
                    }


                    TextView later = dialog.findViewById(R.id.idDialogLater);
                    TextView updateNow = dialog.findViewById(R.id.idDialogUpdateNow);
                    TextView idVersionDetailsText = dialog.findViewById(R.id.idVersionDetailsText);
                    TextView idAppVersionText = dialog.findViewById(R.id.idAppVersionText);
                    TextView idVersionTitleText = dialog.findViewById(R.id.idVersionTitleText);


                    idAppVersionText.setText(newVersion);


                    later.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            dialog.dismiss();
                        }
                    });


                    updateNow.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            final String appPackageName = getPackageName(); // package name of the app
                            try {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                            } catch (android.content.ActivityNotFoundException anfe) {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                            }


                            dialog.dismiss();
                        }
                    });


                }


            }
        }
        catch (Exception e)
        {

        }

    }




}