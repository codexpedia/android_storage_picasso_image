package com.codexpedia.picassosaveimage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;

// 4/13/16
public class ImageDownloadDemoActivity extends AppCompatActivity {
    private String myImageName = "myImage.png";
    private String imageUrl = "https://cloud.githubusercontent.com/assets/5489943/14237225/ef4e3666-f9ee-11e5-886e-9e15b1f1b09d.png";
    private ImageView ivImage;
    private TextView tvLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_image_demo);

        ivImage = (ImageView) findViewById(R.id.iv_image);
        tvLog   = (TextView) findViewById(R.id.tv_log);
    }

    private void log(String logStr) {
        Log.d("ImageDownloadDemo", logStr);
        tvLog.setText(logStr);
    }

    /**********************************************************************************************************
     * Button onClick methods
     **********************************************************************************************************/
    public void asyncDownloadSaveImageFromUrl(View v) {
        log("Load image from url and save it to disk through AsyncTask");
        new DownloadImage().execute(imageUrl);
    }
    public void loadImageFromDisk(View v) {
        ivImage.setImageBitmap(loadImageBitmap(getApplicationContext(), myImageName));
        log("Load image from disk >>>" + myImageName);
    }
    public void deleteImageFromDisk(View v) {
        File file  = getApplicationContext().getFileStreamPath(myImageName);
        if (file.exists()) {
            if (file.delete()) log("Image on the disk deleted successfully! >>>" + file.getAbsolutePath());
            else log("Failed to delete " + file.getAbsolutePath());
        } else {
            log(" No such image on disk! No need to delete it! >>>" + file.getAbsolutePath());
        }
    }
    public void checkIfImageExist(View v) {
        File file = getApplicationContext().getFileStreamPath(myImageName);
        if (file.exists())log("The image is there >>>" + file.getAbsolutePath());
        else log("The image is not there, >>>" + file.getAbsolutePath());
    }

    /**********************************************************************************************************
     * Download image and save it to disk
     * <String, Void, Bitmap> String parameter, Void for progress, Bitmap for return
     **********************************************************************************************************/
    private class DownloadImage extends AsyncTask<String, Void, Bitmap> {
        private String TAG = "DownloadImage";
        private Bitmap downloadImageBitmap(String sUrl) {
            Bitmap bitmap = null;
            try {
                InputStream inputStream = new URL(sUrl).openStream();   // Download Image from URL
                bitmap = BitmapFactory.decodeStream(inputStream);       // Decode Bitmap
                inputStream.close();
            } catch (Exception e) {
                Log.d(TAG, "Exception 1, Something went wrong!");
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            return downloadImageBitmap(params[0]);
        }

        protected void onPostExecute(Bitmap result) {
            saveImage(getApplicationContext(), result, myImageName);
        }
    }

    // files are saved to /data/data/com.codexpedia.picassosaveimage/files
    public void saveImage(Context context, Bitmap b, String imageName){
        FileOutputStream foStream;
        try {
            foStream = context.openFileOutput(imageName, Context.MODE_PRIVATE);
            b.compress(Bitmap.CompressFormat.PNG, 100, foStream);
            foStream.close();
        } catch (Exception e) {
            Log.d("saveImage", "Exception 2, Something went wrong!");
            e.printStackTrace();
        }
    }

    public Bitmap loadImageBitmap(Context context, String imageName) {
        Bitmap bitmap = null;
        FileInputStream fiStream;
        try {
            fiStream    = context.openFileInput(imageName);
            bitmap      = BitmapFactory.decodeStream(fiStream);
            fiStream.close();
        } catch (Exception e) {
            Log.d("saveImage", "Exception 3, Something went wrong!");
            e.printStackTrace();
        }
        return bitmap;
    }
}