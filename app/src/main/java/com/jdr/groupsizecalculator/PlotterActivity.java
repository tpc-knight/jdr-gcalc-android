package com.jdr.groupsizecalculator;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlotterActivity extends AppCompatActivity {
    public static final String IMAGE_URI = "imagePath";

    private int radius = 50;
    private int shotIndex = 0;

    private Map<String, Point> shotCenterPoints;

    public PlotterActivity() {
        super();
        shotCenterPoints = new HashMap<>(3);
        shotCenterPoints.put("shot" + shotIndex++, new Point(250, 500));
        shotCenterPoints.put("shot" + shotIndex++, new Point(100, 100));
        shotCenterPoints.put("shot" + shotIndex++, new Point(300, 300));
    }

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

        drawShots();
    }

    //TODO -- maybe need to move this to the frameLayout view?
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Toast.makeText(getApplicationContext(), "Touch Down x=" + x +"  y=" + y, Toast.LENGTH_SHORT).show();
                int parentOffset = getRelativeTop(findViewById(R.id.plotter_layout));
                shotCenterPoints.put("shot" + shotIndex++, new Point((int) x, (int) y - parentOffset));
                break;
            case MotionEvent.ACTION_MOVE:

                //Handle Touch Move
                break;
            case MotionEvent.ACTION_UP:
                Toast.makeText(getApplicationContext(), "Touch Up x=" + x + "  y=" + y, Toast.LENGTH_SHORT).show();
                break;
        }

        drawShots();
        return false;
    }

    // http://stackoverflow.com/questions/3294590/set-the-absolute-position-of-a-view
    private void drawShots() {
        FrameLayout frameLayout = (FrameLayout)findViewById(R.id.plotter_layout);

        for(String shot: shotCenterPoints.keySet()) {
            Point point = shotCenterPoints.get(shot);

            ShotView shotView = new ShotView(this, radius);

            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(radius * 2, radius * 2);
            params.leftMargin = point.x - radius;
            params.topMargin  = point.y - radius;
            frameLayout.addView(shotView, params);
        }
    }

    private int getRelativeTop(View myView) {
        if (myView.getParent() == myView.getRootView())
            return myView.getTop();
        else
            return myView.getTop() + getRelativeTop((View) myView.getParent());
    }


    public class ShotView extends View {
        private Paint paint;
        private final int radius;

        public ShotView(Context context, int radius) {
            super(context);

            paint = new Paint();
            paint.setColor(Color.RED);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(4.0f);
            this.radius = radius;
        }

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawCircle(radius, radius, radius, paint);
        }

    }
}
