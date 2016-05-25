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
    public static final int GREEN_CIRCLES_NUMBER = 2;
    public static final int MAX_GREEN_CIRCLES_AFTER_ACCELERATION = 2;
    public static final float ACCELERATION_INCREMENT = 0.5f;

    private int x;  // initial coordinates
    private int y;
    private boolean mType;  //true - RED; false - GREEN

    private int dest_X; // destination coordinates
    private int dest_Y;

    private int max_X;  // maximum coordinates
    private int max_Y;

    private int redNum; // Number of circles hit
    private int greenNum;

    private int greenLeft; // Number of green circles left

    private int moveDuration;
    private float moveDurationDivider;
    private int greenNumAfterAcceleration;  // Number of green circles after acceleration

    private AnimatorSet mAnimatorSet;

    private PopSound mPopSound;

    public Circle(Context context, AttributeSet attrs) {
        super(context, attrs);
        greenLeft = GREEN_CIRCLES_NUMBER;
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
        if (true /* greenLeft > 0 */) {
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
                greenLeft--;
                greenNumAfterAcceleration++;
                setImageDrawable(getResources().getDrawable(R.drawable.green_circle));
            }

            startMovement();
        } else {
            gameEnd();
        }
    }

    private void gameEnd() {
        Toast.makeText(getContext(), "You missed " + (GREEN_CIRCLES_NUMBER - greenNum) + " green circles!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            popCircle();
        }

        return true;
    }

    private void popCircle() {
        stopMovement();
    }

    private void countHitScore() {
        if (mType == RED) {
            mPopSound.redPop();
            redNum++;
        } else {
            mPopSound.greenPop();
            greenNum++;
        }

        Toast.makeText(getContext(), "Green = " + greenNum + ", Red = " + redNum, Toast.LENGTH_SHORT).show();
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
