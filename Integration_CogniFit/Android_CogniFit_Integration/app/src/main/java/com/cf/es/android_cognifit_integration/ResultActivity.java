package com.cf.es.android_cognifit_integration;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.content.FileProvider;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

// This is an example without using backend and serverless, making API calls directly from the frontend, but we recommend the second option (see FetchAccessTokenTask.java).
public class ResultActivity extends AppCompatActivity {

    private static final String TAG = "ResultActivity";
    private static final String CHANNEL_ID = "DOWNLOAD_NOTIFICATION";
    private static final String API_URL = "https://api.cognifit.com/user-info/get-assessment-report";
    private static final String CLIENT_ID = "b76cd92b3aa08ac0fe2706c9d8c9d744";  // Replace with a valid client ID (In your account, API keys section)
    private static final String CLIENT_SECRET = "78fccdc9f7dec856b7ea6586d71424b2"; // Replace with a valid client secret (In your account, API keys section)
    private static final String USER_TOKEN = "DQfs3xC7foFcdFovP/EgOsAyTuwEZM6PA6+xATM0tDTzT+LemlM/WZtUFlHuB3ytRA/a2fnqDB5Acsf5uyMDgg=="; // Obtained making an api call,see the readme file for more info
    private static final int SESSION_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        // Set up the download button
        Button downloadButton = findViewById(R.id.downloadButton);
        downloadButton.setOnClickListener(v -> fetchPdfReport());
    }

    // Method to initiate the PDF report request
    private void fetchPdfReport() {
        Log.d(TAG, "Starting PDF request");

        OkHttpClient client = new OkHttpClient();
        String jsonBody = "{" +
                "\"client_id\": \"" + CLIENT_ID + "\"," +
                "\"client_secret\": \"" + CLIENT_SECRET + "\"," +
                "\"user_token\": \"" + USER_TOKEN + "\"," +
                "\"session_id\": " + SESSION_ID + "," +
                "\"locale\": \"en\"," +
                "\"report_types\": [" +
                "\"ASSESSMENT_REPORT\"," +
                "\"COGNITIVE_PROFILE\"," +
                "\"CARE_PLAN\"" +
                "]" +
                "}";

        // Build the request
        Request request = new Request.Builder()
                .url(API_URL)
                .post(RequestBody.create(jsonBody, okhttp3.MediaType.parse("application/json; charset=utf-8")))
                .header("Content-Type", "application/json")
                .header("cache-control", "no-cache")
                .build();

        // Execute the request in a new thread
        new Thread(() -> {
            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "Successful response");

                    String contentType = response.header("Content-Type", "");
                    Log.d(TAG, "Content-Type: " + contentType);

                    // Save the file based on its content type
                    if (contentType.contains("application/pdf")) {
                        saveFileToDisk(response.body().byteStream(), "report.pdf");
                    } else if (contentType.contains("application/zip")) {
                        saveFileToDisk(response.body().byteStream(), "reports.zip");
                    } else {
                        Log.e(TAG, "Unsupported file format");
                        runOnUiThread(() -> Toast.makeText(ResultActivity.this, "Unsupported file format", Toast.LENGTH_SHORT).show());
                    }
                } else {
                    Log.e(TAG, "Response error: " + response.code());
                    runOnUiThread(() -> Toast.makeText(ResultActivity.this, "Error receiving response", Toast.LENGTH_SHORT).show());
                }
            } catch (IOException e) {
                Log.e(TAG, "Request error", e);
                runOnUiThread(() -> Toast.makeText(ResultActivity.this, "Request error", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

    // Method to save the file to disk
    private void saveFileToDisk(InputStream inputStream, String fileName) {
        File outputDir = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        File outputFile = new File(outputDir, fileName);
        Log.d(TAG, "Saving file to: " + outputFile.getAbsolutePath()); // File location

        try (FileOutputStream outputStream = new FileOutputStream(outputFile)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            runOnUiThread(() -> showNotification(outputFile));
        } catch (IOException e) {
            Log.e(TAG, "Error saving file", e);
            runOnUiThread(() -> Toast.makeText(this, "Error saving file", Toast.LENGTH_SHORT).show());
        }
    }

    // Optional: Method to show a notification after the file is downloaded, remember to check
    private void showNotification(File file) {
        createNotificationChannel();

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Uri fileUri = FileProvider.getUriForFile(this, "com.cf.es.android_cognifit_integration.provider", file);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(fileUri, file.getName().endsWith(".pdf") ? "application/pdf" : "application/zip");
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_GRANT_READ_URI_PERMISSION);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Download Complete")
                .setContentText("The report has been downloaded successfully.")
                .setSmallIcon(android.R.drawable.stat_sys_download_done)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();

        notificationManager.notify(1, notification);
        Log.d(TAG, "File saved and notification shown");
    }

    // Method to create the notification channel
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Download Channel";
            String description = "Channel for download notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}

