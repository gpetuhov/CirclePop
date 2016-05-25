package com.gpetuhov.android.circlepop;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Random;

public class Circle extends ImageView {
    public static final boolean RED = true;
    public static final boolean GREEN = false;
    public static final int RADIUS = 100;
    public static final int MAX_MOVE_DURATION = 3000;
    public static final int MAX_GREEN_CIRCLES_AFTER_ACCELERATION = 2;   // Move duration decreases every MAX_GREEN_CIRCLES_AFTER_ACCELERATION green circles
    public static final float ACCELERATION_INCREMENT = 0.5f;
    public static final boolean CIRCLE_MISSED = false;
    public static final boolean CIRCLE_HIT = true;
    public static final int MAX_GREEN_MISSED = 3;
    public static final int MAX_RED_HIT = 3;

    private int x;  // initial coordinates
    private int y;
    private boolean mType;  //true - RED; false - GREEN

    private int dest_X; // destination coordinates
    private int dest_Y;

    private int max_X;  // maximum coordinates
    private int max_Y;

    private int redHitNum; // Number of circles hit
    private int greenHitNum;

    private boolean circleHit;  // true - circle hit, false - circle missed

    private int greenMissedNum;

    private int moveDuration;
    private float moveDurationDivider;
    private int greenNumAfterAcceleration;  // Number of green circles generated after acceleration

    private AnimatorSet mAnimatorSet;

    private PopSound mPopSound;

    public Circle(Context context, AttributeSet attrs) {
        super(context, attrs);
        greenMissedNum = 0;
        moveDuration = MAX_MOVE_DURATION;
        moveDurationDivider = 1;
        greenNumAfterAcceleration = 0;
        mPopSound = new PopSound(context);
        initCoordinatesRange(context);
        initCircle();
    }

    // Detect maximum X and Y
    private void initCoordinatesRange(Context context) {
        Point point = new Point();
        ((Activity) context).getWindowManager().getDefaultDisplay().getSize(point);

        max_X = point.x - RADIUS;
        max_Y = point.y - RADIUS;
    }

    private void initCircle() {
        if (greenMissedNum < MAX_GREEN_MISSED && redHitNum < MAX_RED_HIT) {
            Random random = new Random();

            x = random.nextInt(max_X);
            y = random.nextInt(max_Y);

            setX(x);
            setY(y);

            dest_X = random.nextInt(max_X);
            dest_Y = random.nextInt(max_Y);

            int z = 1 + random.nextInt(10); // generate circle type (red or green)
            mType = (z % 2) == 0;

            if (mType == RED) {
                setImageDrawable(getResources().getDrawable(R.drawable.red_circle));
            }
            if (mType == GREEN) {
                greenNumAfterAcceleration++;
                setImageDrawable(getResources().getDrawable(R.drawable.green_circle));
            }

            circleHit = CIRCLE_MISSED;

            startMovement();
        } else {
            gameEnd();
        }
    }

    private void gameEnd() {
        setVisibility(GONE);
        if (greenMissedNum == MAX_GREEN_MISSED) {
            Toast.makeText(getContext(), "Game over: you missed " + greenMissedNum + " green circles!", Toast.LENGTH_SHORT).show();
            Toast.makeText(getContext(), "Your score: Green = " + greenHitNum + ", Red = " + redHitNum, Toast.LENGTH_SHORT).show();
        }
        if (redHitNum == MAX_RED_HIT) {
            Toast.makeText(getContext(), "Game over: you hit " + redHitNum + " red circles!", Toast.LENGTH_SHORT).show();
            Toast.makeText(getContext(), "Your score: Green hit = " + greenHitNum + ", Green Missed = " + greenMissedNum, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            popCircle();
        }

        return true;
    }

    private void popCircle() {
        circleHit = CIRCLE_HIT;
        stopMovement();
    }

    private void countHitScore() {
        if (mType == RED) {
            mPopSound.redPop();
            redHitNum++;
        } else {
            mPopSound.greenPop();
            greenHitNum++;
        }
    }

    private void countMissScore() {
        if (circleHit == CIRCLE_MISSED && mType == GREEN) {
            greenMissedNum++;
        }
    }

    public void startMovement() {
        ObjectAnimator widthAnimator = ObjectAnimator.ofFloat(Circle.this, "x", x, dest_X)
                .setDuration(moveDuration);
        widthAnimator.setInterpolator(new AccelerateInterpolator());

        ObjectAnimator heightAnimator = ObjectAnimator.ofFloat(Circle.this, "y", y, dest_Y)
                .setDuration(moveDuration);
        heightAnimator.setInterpolator(new AccelerateInterpolator());

        mAnimatorSet = new AnimatorSet();
        mAnimatorSet
                .play(widthAnimator)
                .with(heightAnimator);
        mAnimatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                // onAnimationEnd is always called
                countMissScore();
                initMoveDuration();
                initCircle();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                countHitScore();
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        mAnimatorSet.start();
    }

    private void stopMovement() {
        mAnimatorSet.cancel();
    }

    private void initMoveDuration() {
        if (greenNumAfterAcceleration == MAX_GREEN_CIRCLES_AFTER_ACCELERATION) {
            moveDurationDivider += ACCELERATION_INCREMENT;
            greenNumAfterAcceleration = 0;
        }

        moveDuration = (int) (MAX_MOVE_DURATION / moveDurationDivider);
    }
}
