package com.application.intercom.helper;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class DownloadsImage extends AsyncTask<String, Void, Void> {

    private static final String CHANNEL_ID = "image_download_channel";
    Context mcontext;

    public DownloadsImage(Context context) {
        this.mcontext = context;
    }

    @Override
    protected Void doInBackground(String... strings) {
        URL url = null;

        try {
            url = new URL(strings[0]);
            System.out.println(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Bitmap bm = null;
        try {
            bm = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Create Path to save Image
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS); //Creates app specific folder

        if (!path.exists()) {
            path.mkdirs();
        }

        File imageFile = new File(path, String.valueOf(System.currentTimeMillis()) + ".png"); // Imagename.png
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(imageFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            bm.compress(Bitmap.CompressFormat.PNG, 100, out); // Compress Image
            out.flush();
            out.close();
            // Tell the media scanner about the new file so that it is
            // immediately available to the user.
            MediaScannerConnection.scanFile(mcontext, new String[]{imageFile.getAbsolutePath()}, null, new MediaScannerConnection.OnScanCompletedListener() {
                public void onScanCompleted(String path, Uri uri) {
                    // Log.i("ExternalStorage", "Scanned " + path + ":");
                    //    Log.i("ExternalStorage", "-> uri=" + uri);
                }
            });
        } catch (Exception e) {
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        Toast.makeText(mcontext, "Receipt Saved in Gallery!", Toast.LENGTH_SHORT).show();
        //showDownloadNotification("Receipt Saved in Gallery!");
    }

    private void showDownloadNotification(String text) {
        NotificationManager notificationManager = (NotificationManager) mcontext.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Image Download", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        Notification notification = new NotificationCompat.Builder(mcontext, CHANNEL_ID)
                .setContentTitle("Download Complete")
                .setContentText(text)
                .setSmallIcon(android.R.drawable.stat_sys_download_done)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .build();

        notificationManager.notify(1, notification);
    }
}
