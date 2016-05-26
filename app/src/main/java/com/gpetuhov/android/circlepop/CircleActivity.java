package com.gpetuhov.android.circlepop;

import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CircleActivity extends AppCompatActivity {
    private Circle mCircle;     // All circles are generated into one Circle object

    private LinearLayout mMainLayout;   // Main layout

    private LinearLayout mIntroLayout;  // Intro screen
    private Button mStartButton;

    private LinearLayout mScoreLayout;  // Your score screen
    private TextView mGameOverTextView;
    private TextView mGreenHitTextView;
    private TextView mGreenMissedTextView;
    private TextView mRedHitTextView;
    private Button mPlayAgainButton;

    public int screenWidth;     // Screen size
    public int screenHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle);

        getScreenSize();

        mMainLayout = (LinearLayout) findViewById(R.id.main_layout);

        mIntroLayout = (LinearLayout) findViewById(R.id.intro_layout);

        mStartButton = (Button) findViewById(R.id.start_button);
        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIntroLayout.setVisibility(View.GONE);  // Hide intro screen
                createCircle();
            }
        });

        mScoreLayout = (LinearLayout) findViewById(R.id.score_layout);
        mGameOverTextView = (TextView) findViewById(R.id.gameover_textview);
        mGreenHitTextView = (TextView) findViewById(R.id.greenhit_textview);
        mGreenMissedTextView = (TextView) findViewById(R.id.greenmissed_textview);
        mRedHitTextView = (TextView) findViewById(R.id.redhit_textview);

        mPlayAgainButton = (Button) findViewById(R.id.play_again_button);
        mPlayAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mScoreLayout.setVisibility(View.GONE);
                mMainLayout.removeView(mCircle);
                createCircle();
            }
        });
    }

    // Create circle and add to main layout
    private void createCircle() {
        mCircle = new Circle(CircleActivity.this);  // Create circle
        mCircle.setVisibility(View.INVISIBLE);      // Make circle invisible until fully initialized
        mMainLayout.addView(mCircle);               // Add circle to main layout
    }

    // Calculate screen size
    private void getScreenSize() {
        Point point = new Point();  // Stores screen size
        getWindowManager().getDefaultDisplay().getSize(point); // Get screen size

        screenWidth = point.x;
        screenHeight = point.y;
    }

    // Game over due to green circles missed
    public void gameEndGreenMissed() {
        mGameOverTextView.setText("Game over: you missed " + mCircle.MAX_GREEN_MISSED + " green circles!");
        setScoreText();
        mScoreLayout.setVisibility(View.VISIBLE);
    }

    // Game over due to red circles hit
    public void gameEndRedHit() {
        mGameOverTextView.setText("Game over: you hit " + mCircle.MAX_RED_HIT + " red circles!");
        setScoreText();
        mScoreLayout.setVisibility(View.VISIBLE);
    }

    // Set text in TextViews on Your Score Layout
    private void setScoreText() {
        mGreenHitTextView.setText("Green circles hit: " + mCircle.getGreenHitNum());
        mGreenMissedTextView.setText("Green circles missed: " + mCircle.getGreenMissedNum());
        mRedHitTextView.setText("Red circles hit: " + mCircle.getRedHitNum());
    }
}

// Application icon created with the Android Asset Studio: https://romannurik.github.io/AndroidAssetStudio/