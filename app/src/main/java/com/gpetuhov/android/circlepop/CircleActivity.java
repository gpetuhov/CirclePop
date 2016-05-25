package com.gpetuhov.android.circlepop;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CircleActivity extends AppCompatActivity {
    Circle mCircle;

    LinearLayout mMainLayout;

    LinearLayout mIntroLayout;  // Intro screen
    Button mStartButton;

    LinearLayout mScoreLayout;  // Your score screen
    TextView mGameOverTextView;
    TextView mGreenHitTextView;
    TextView mGreenMissedTextView;
    TextView mRedHitTextView;
    Button mPlayAgainButton;

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

                mCircle = new Circle(CircleActivity.this);
                mMainLayout.addView(mCircle);
            }
        });
    }

    public void gameEndGreenMissed() {
        mGameOverTextView.setText("Game over: you missed " + mCircle.MAX_GREEN_MISSED + " green circles!");
        setScoreText();
        mScoreLayout.setVisibility(View.VISIBLE);
    }

    public void gameEndRedHit() {
        mGameOverTextView.setText("Game over: you hit " + mCircle.MAX_RED_HIT + " red circles!");
        setScoreText();
        mScoreLayout.setVisibility(View.VISIBLE);
    }

    private void setScoreText() {
        mGreenHitTextView.setText("Green circles hit: " + mCircle.getGreenHitNum());
        mGreenMissedTextView.setText("Green circles missed: " + mCircle.getGreenMissedNum());
        mRedHitTextView.setText("Red circles hit: " + mCircle.getRedHitNum());
    }
}
