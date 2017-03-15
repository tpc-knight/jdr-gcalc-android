package com.jdr.groupsizecalculator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class PlotterActivity extends AppCompatActivity {
    public static final String IMAGE_PATH = "imagePath";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plotter);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String message = intent.getStringExtra(IMAGE_PATH);

        // Capture the layout's TextView and set the string as its text
        TextView textView = (TextView) findViewById(R.id.plotter_text);
        textView.setText(message);
    }
}
