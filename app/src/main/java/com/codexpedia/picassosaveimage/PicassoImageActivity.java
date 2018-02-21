package com.codexpedia.picassosaveimage;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

// 4/13/16
public class PicassoImageActivity extends AppCompatActivity {
    private String imageUrl = "https://cloud.githubusercontent.com/assets/5489943/14237225/ef4e3666-f9ee-11e5-886e-9e15b1f1b09d.png";
//    private String imagePath = "/data/data/com.codexpedia.picassosaveimage/app_imageDir/my_image.png";

    private String imageDir = "imageDir";
    private String imageName = "my_image.png";
    private ImageView ivImage;
    private TextView tvLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picasso);

        ivImage = (ImageView) findViewById(R.id.iv_image);
        tvLog   = (TextView) findViewById(R.id.tv_log);
    }

    private void log(String logStr) {
        Log.d("MainActivity", logStr);
        tvLog.setText(logStr);
    }

    /**********************************************************************************************************
     * Button onClick methods
     **********************************************************************************************************/
    public void loadImageFromUrl(View v) {
        log("Load image from url");
        Picasso.with(this).load(imageUrl).into(ivImage);
    }
    public void downloadSaveImageFromUrl(View v) {
        log("Load image from url and save it to disk through Picasso");
        Picasso.with(this).load(imageUrl).into(picassoImageTarget(getApplicationContext(), imageDir, imageName));
    }

    public void loadImageFromDisk(View v) {
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File directory = cw.getDir(imageDir, Context.MODE_PRIVATE);
        File myImageFile = new File(directory, imageName);

        if (myImageFile.exists()) {
            log("Load image from disk: " + myImageFile.getAbsolutePath());
            Picasso.with(this).load(myImageFile).into(ivImage);

        } else {
            ivImage.setImageBitmap(null); // remove the existing image first
            log("The image doesn't exist on disk!");
        }
    }

    public void deleteImageFromDisk(View v) {
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File directory = cw.getDir(imageDir, Context.MODE_PRIVATE);
        File myImageFile = new File(directory, imageName);

        if (myImageFile.exists()) {
            if (myImageFile.delete()) log("The image on the disk deleted successfully!");
            else log("Failed to delete " + myImageFile.getAbsolutePath());
        } else {
            log("No such image on disk! No need to delete it!");
        }
    }

    public void checkIfImageExist(View v) {
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File directory = cw.getDir(imageDir, Context.MODE_PRIVATE);
        File myImageFile = new File(directory, imageName);

        if (myImageFile.exists())log("the image is there!");
        else log("The image is not there!");
    }

    /**********************************************************************************************************
     * Target class for saving image bitmap returned from Picasso
     **********************************************************************************************************/
    private Target picassoImageTarget(Context context, final String imageDir, final String imageName) {
        Log.d("picassoImageTarget", " picassoImageTarget");
        ContextWrapper cw = new ContextWrapper(context);
        final File directory = cw.getDir(imageDir, Context.MODE_PRIVATE); // path to /data/data/yourapp/app_imageDir
        return new Target() {
            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final File myImageFile = new File(directory, imageName); // Create image file
                        FileOutputStream fos = null;
                        try {
                            fos = new FileOutputStream(myImageFile);
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                fos.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                log("image saved to >>>" + myImageFile.getAbsolutePath());
                            }
                        });

                    }
                }).start();
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
            }
            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                if (placeHolderDrawable != null) {}
            }
        };
    }
}
