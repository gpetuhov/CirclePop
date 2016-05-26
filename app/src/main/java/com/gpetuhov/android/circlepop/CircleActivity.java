package com.gpetuhov.android.circlepop;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CircleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);    // Initially create activity with intro layout

        Button startButton = (Button) findViewById(R.id.start_button);
        if (startButton != null) {
            startButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    gameStart();
                }
            });
        }
    }

    // Set layout with circle and start game
    private void gameStart() {
        setContentView(R.layout.activity_circle);

        Circle circle = (Circle) findViewById(R.id.circle); // All circles are generated into one Circle object
        if (circle != null) {
            circle.startMovement();    // Start circle movement (start game)
        }
    }

    // Set layout with score and display results
    public void gameEnd(int greenHitNum, int greenMissedNum, int redHitNum, int maxGreenMissed, int maxRedHit) {
        setContentView(R.layout.activity_score);

        TextView gameOverTextView = (TextView) findViewById(R.id.gameover_textview);
        TextView greenHitTextView = (TextView) findViewById(R.id.greenhit_textview);
        TextView greenMissedTextView = (TextView) findViewById(R.id.greenmissed_textview);
        TextView redHitTextView = (TextView) findViewById(R.id.redhit_textview);

        Button playAgainButton = (Button) findViewById(R.id.play_again_button);
        if (playAgainButton != null) {
            playAgainButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    gameStart();
                }
            });
        }

        if (gameOverTextView != null) {
            if (greenMissedNum == maxGreenMissed) { // Game over due to green circles missed
                gameOverTextView.setText("Game over: you missed " + maxGreenMissed + " green circles!");
            }
            if (redHitNum == maxRedHit) {   // Game over due to red circles hit
                gameOverTextView.setText("Game over: you hit " + maxRedHit + " red circles!");
            }
        }

        if (greenHitTextView != null) {
            greenHitTextView.setText("Green circles hit: " + greenHitNum);
        }

        if (greenMissedTextView != null) {
            greenMissedTextView.setText("Green circles missed: " + greenMissedNum);
        }

        if (redHitTextView != null) {
            redHitTextView.setText("Red circles hit: " + redHitNum);
        }
    }
}

// Application icon created with the Android Asset Studio: https://romannurik.github.io/AndroidAssetStudio/