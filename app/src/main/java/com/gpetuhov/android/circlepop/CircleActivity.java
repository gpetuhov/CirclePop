package com.gpetuhov.android.circlepop;

import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CircleActivity extends AppCompatActivity {
    private Circle mCircle;     // All circles are generated into one Circle object

    private Button mStartButton;

    private TextView mGameOverTextView;
    private TextView mGreenHitTextView;
    private TextView mGreenMissedTextView;
    private TextView mRedHitTextView;
    private Button mPlayAgainButton;

    public int screenWidth;     // Screen size
    public int screenHeight;

    public PopSound mPopSound; // Play circle pop sounds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        getScreenSize();

        mPopSound = new PopSound(CircleActivity.this);

        mStartButton = (Button) findViewById(R.id.start_button);
        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameStart();
            }
        });
    }

    // Calculate screen size
    private void getScreenSize() {
        Point point = new Point();  // Stores screen size
        getWindowManager().getDefaultDisplay().getSize(point); // Get screen size

        screenWidth = point.x;
        screenHeight = point.y;
    }

    private void gameStart() {
        setContentView(R.layout.activity_circle);

        mCircle = (Circle) findViewById(R.id.circle);
        mCircle.startMovement();
    }

    private void gameEndInit() {
        setContentView(R.layout.activity_score);

        mGameOverTextView = (TextView) findViewById(R.id.gameover_textview);
        mGreenHitTextView = (TextView) findViewById(R.id.greenhit_textview);
        mGreenMissedTextView = (TextView) findViewById(R.id.greenmissed_textview);
        mRedHitTextView = (TextView) findViewById(R.id.redhit_textview);

        mPlayAgainButton = (Button) findViewById(R.id.play_again_button);
        mPlayAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameStart();
            }
        });
    }

    // Game over due to green circles missed
    public void gameEndGreenMissed() {
        gameEndInit();
        mGameOverTextView.setText("Game over: you missed " + mCircle.MAX_GREEN_MISSED + " green circles!");
        setScoreText();
    }

    // Game over due to red circles hit
    public void gameEndRedHit() {
        gameEndInit();
        mGameOverTextView.setText("Game over: you hit " + mCircle.MAX_RED_HIT + " red circles!");
        setScoreText();
    }

    // Set text in TextViews on Your Score Layout
    private void setScoreText() {
        mGreenHitTextView.setText("Green circles hit: " + mCircle.getGreenHitNum());
        mGreenMissedTextView.setText("Green circles missed: " + mCircle.getGreenMissedNum());
        mRedHitTextView.setText("Red circles hit: " + mCircle.getRedHitNum());
    }
}

// Application icon created with the Android Asset Studio: https://romannurik.github.io/AndroidAssetStudio/