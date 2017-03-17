package com.jdr.groupsizecalculator;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;

public class PlotterActivity extends AppCompatActivity {
    public static final String IMAGE_URI = "imagePath";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plotter);

        Intent intent = getIntent();
        String imageUriString = intent.getStringExtra(IMAGE_URI);

        Bitmap bitmap = null;
        InputStream is = null;
        try {
            is = getContentResolver().openInputStream(Uri.parse(imageUriString));
            bitmap = BitmapFactory.decodeStream(is);
        } catch (IOException e) {
            //TODO -- handle
        } finally {
            if(is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    //TODO -- handle
                }
            }
        }

        if(bitmap != null) {
            if(bitmap.getWidth() > bitmap.getHeight()) {
                Matrix rotationMatrix = new Matrix();
                rotationMatrix.postRotate(90);
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), rotationMatrix, true);
            }

            ImageView imageView = (ImageView) findViewById(R.id.plotter_image);
            imageView.setAdjustViewBounds(true);
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            imageView.setImageBitmap(bitmap);
        }
    }
}
