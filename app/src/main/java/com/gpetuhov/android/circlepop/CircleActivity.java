package com.gpetuhov.android.circlepop;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class CircleActivity extends AppCompatActivity {
    private Circle mCircle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle);

        mCircle = (Circle) findViewById(R.id.circle);
    }
}
