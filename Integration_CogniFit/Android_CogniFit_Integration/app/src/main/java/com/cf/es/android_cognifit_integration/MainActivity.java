package com.cf.es.android_cognifit_integration;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.appcompat.app.AppCompatActivity;

import com.cognifit.sdk.CognifitSDK;
import com.cognifit.sdk.ContainerHTML5Configuration;
import com.cognifit.sdk.ContainerHTML5EventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private WebView webView;
    private static String activityKey = "CONCENTRATION_ASSESSMENT"; // Change to the desired activity keyd

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the WebView
        webView = findViewById(R.id.webView);
        setupWebView(webView);

        // Start the task to fetch the access token
        new FetchAccessTokenTask(this).execute();
    }

    // Configure the WebView settings
    private void setupWebView(WebView webView) {
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                // Inject JavaScript to communicate with the WebView
                webView.evaluateJavascript("window.ANDROID = { communicate: function(status, action, body) { Android.communicate(status, action, body); } };", null);
            }
        });
    }

    // Load the CogniFit activity using the access token
    public void loadCogniFitActivity(String accessToken) {
        try {
            // Initialize the Cognifit SDK
            CognifitSDK.setInstance(getApplicationContext());
            Log.d(TAG, "SDK Initialized");

            // Configure the container
            ContainerHTML5Configuration containerConfig = new ContainerHTML5Configuration.Builder()
                    .setClientId(Config.CLIENT_ID)
                    .setAccessToken(accessToken)
                    .build();
            CognifitSDK.setContainerHTML5Configuration(containerConfig);
            Log.d(TAG, "Container configuration set");

            // Set up the JavaScript listener for container events
            ContainerHTML5EventListener jsListener = new ContainerHTML5EventListener() {
                @JavascriptInterface
                public void communicate(String eventName, String eventAction, String eventBody) {
                    Log.d(TAG, "Container listener started");

                    Log.d(TAG, eventName);
                    Log.d(TAG, eventAction);
                    Log.d(TAG, eventBody);

                    // Handle the "completed" event by starting the ResultActivity
                    if (eventName.equals("completed")) {
                        Log.d(TAG, "Completion event detected.");
                        Intent intent = new Intent(MainActivity.this, ResultActivity.class);
                        startActivity(intent);
                    }
                }
            };
            CognifitSDK.setContainerHTML5Listener(jsListener);

            // Load the CogniFit container in assessment mode with the specified activity key
            CognifitSDK.loadModeLatestContainerHTML5("assessmentMode", activityKey);

        } catch (Exception e) {
            Log.e(TAG, "Error initializing CognifitSDK: " + e.getMessage(), e);
        }
    }
}






