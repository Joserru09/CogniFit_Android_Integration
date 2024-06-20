package com.cf.es.android_cognifit_integration;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.InputStreamReader;

//DONT FORGET TO PUT THE ID PARAMS BELOW
// This is an example without using backend and serverless, making API calls directly from the frontend, but we recommend the second option commented below
public class FetchAccessTokenTask extends AsyncTask<Void, Void, String> {

    private static final String TAG = "FetchAccessTokenTask";
    private MainActivity mainActivity;

    public FetchAccessTokenTask(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    protected String doInBackground(Void... voids) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String accessToken = null;

        try {
            // Build the URL and HTTP connection
            URL url = new URL("https://api.cognifit.com/issue-access-token");
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setDoOutput(true);

            // Create the JSON request body
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("client_id", Config.CLIENT_ID);  
            jsonBody.put("client_secret", Config.CLIENT_SECRET);  
            jsonBody.put("user_token", Config.USER_TOKEN); 
            // Write the request body to the connection
            OutputStream outputStream = urlConnection.getOutputStream();
            outputStream.write(jsonBody.toString().getBytes());
            outputStream.close();

            // Read the response from the connection
            InputStream inputStream = urlConnection.getInputStream();
            StringBuilder buffer = new StringBuilder();
            if (inputStream == null) {
                // No data to read
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line).append("\n");
            }

            if (buffer.length() == 0) {
                // The buffer is empty
                return null;
            }
            accessToken = buffer.toString();

        } catch (IOException | JSONException e) {
            Log.e(TAG, "Error reading the PHP content: " + e.getMessage(), e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e(TAG, "Error closing the reader: " + e.getMessage(), e);
                }
            }
        }

        return accessToken;
    }

    @Override
    protected void onPostExecute(String jsonResponse) {
        super.onPostExecute(jsonResponse);

        if (jsonResponse != null) {
            try {
                // Parse the JSON response
                JSONObject jsonObject = new JSONObject(jsonResponse);
                String accessToken = jsonObject.getString("access_token");
                mainActivity.loadCogniFitActivity(accessToken);

                // Log the access token
                Log.d(TAG, "Access Token: " + accessToken);

            } catch (JSONException e) {
                Log.e(TAG, "Error parsing JSON response: " + e.getMessage(), e);
            }
        } else {
            Log.e(TAG, "Error: PHP content is null");
        }
    }
}

// We highly recommend for best practices using backend calls to your servers, this is an example.

/*
public class FetchAccessTokenTask extends AsyncTask<Void, Void, String> {

    private static final String TAG = "FetchAccessTokenTask";
    private MainActivity mainActivity;

    public FetchAccessTokenTask(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    protected String doInBackground(Void... voids) {
        String urlString = "https://domain/accesstoken.php"; //replace with your server domain
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        try {
            URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStreamReader inputStreamReader = new InputStreamReader(urlConnection.getInputStream());
            reader = new BufferedReader(inputStreamReader);

            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }

            return stringBuilder.toString();
        } catch (Exception e) {
            Log.e(TAG, "Error fetching access token: " + e.getMessage(), e);
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e) {
                    Log.e(TAG, "Error closing reader: " + e.getMessage(), e);
                }
            }
        }
    }

    @Override
    protected void onPostExecute(String result) {
        if (result != null) {
            Log.d(TAG, "Access Token received: " + result);

            // Call the method to load the CogniFit activity
            mainActivity.loadCogniFitActivity(result);
        } else {
            Log.e(TAG, "Error: Result is null");
        }
    }
}
*/
