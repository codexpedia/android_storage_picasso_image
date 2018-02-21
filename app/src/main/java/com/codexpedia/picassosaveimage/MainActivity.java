package com.codexpedia.picassosaveimage;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void picassoImageDownload(View v) {
        Intent i = new Intent(this, PicassoImageActivity.class);
        startActivity(i);
    }

    public void regularImageDownload(View v) {
        Intent i = new Intent(this, ImageDownloadDemoActivity.class);
        startActivity(i);
    }
}
