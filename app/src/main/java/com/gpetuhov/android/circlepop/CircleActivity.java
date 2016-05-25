package com.gpetuhov.android.circlepop;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class CircleActivity extends AppCompatActivity {
    Circle mCircle;

    LinearLayout mMainLayout;

    LinearLayout mIntroLayout;  // Intro screen
    Button mStartButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle);

        mMainLayout = (LinearLayout) findViewById(R.id.main_layout);

        mIntroLayout = (LinearLayout) findViewById(R.id.intro_layout);

        mStartButton = (Button) findViewById(R.id.start_button);
        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIntroLayout.setVisibility(View.GONE);  // Hide intro screen

                mCircle = new Circle(CircleActivity.this);  // Create circle
                mMainLayout.addView(mCircle);               // Add circle to main layout
            }
        });
    }
}
