package com.gpetuhov.android.circlepop;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.Random;

public class Circle extends ImageView {
    public static final boolean RED = true;
    public static final boolean GREEN = false;
    public static final int RADIUS = 100;
    public static final int MAX_MOVE_DURATION = 3000;   // Initial circle move duration
    public static final int MAX_GREEN_CIRCLES_AFTER_ACCELERATION = 2;   // Move duration decreases every MAX_GREEN_CIRCLES_AFTER_ACCELERATION green circles
    public static final float ACCELERATION_INCREMENT = 0.5f;    // Used in calculating new move duration
    public static final boolean CIRCLE_MISSED = false;
    public static final boolean CIRCLE_HIT = true;
    public static final int MAX_GREEN_MISSED = 3;   // Game over if missed MAX_GREEN_MISSED green circles
    public static final int MAX_RED_HIT = 3;        // Game over if hit MAX_RED_HIT red circles

    private int x;  // Initial coordinates
    private int y;
    private boolean mType;  //true - RED; false - GREEN

    private int dest_X; // Destination coordinates
    private int dest_Y;

    private int max_X;  // Maximum coordinates
    private int max_Y;

    private int redHitNum; // Number of red circles hit
    private int greenHitNum; // Number of green circles hit

    private boolean circleHit;  // true - circle hit, false - circle missed

    private int greenMissedNum; // Number of green circles missed

    private int moveDuration;               // Duration of circle movement on screen (circle time to live)
    private float moveDurationDivider;      // Used in calculating new moveDuration
    private int greenNumAfterAcceleration;  // Number of green circles generated after acceleration

    private AnimatorSet mAnimatorSet;   // Animator set for circle movement

    private PopSound mPopSound; // Play circle pop sounds

    private CircleActivity mCircleActivity;

    // Constructor for creating Circle programmatically
    public Circle(Context context) {
        this(context, null);
    }

    // Constructor for using Circle in XML
    public Circle(Context context, AttributeSet attrs) {
        super(context, attrs);
        mCircleActivity = (CircleActivity) context;

        // Set circle diameter (ImageView width and height)
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(RADIUS * 2, RADIUS * 2);
        setLayoutParams(params);

        initCoordinatesRange();
        greenMissedNum = 0;
        moveDuration = MAX_MOVE_DURATION;
        moveDurationDivider = 1;
        greenNumAfterAcceleration = 0;
        mPopSound = new PopSound(context);
        initCircle();
    }

    // Detect maximum X and Y
    private void initCoordinatesRange() {
        max_X = mCircleActivity.screenWidth - (RADIUS * 2); // Calculate maximum coordinates so that circle doesn't fall out from screen
        max_Y = mCircleActivity.screenHeight - (RADIUS * 2);
    }

    // Initialize new circle
    private void initCircle() {
        if (greenMissedNum < MAX_GREEN_MISSED && redHitNum < MAX_RED_HIT) {
            Random random = new Random();

            x = random.nextInt(max_X);  // Generate initial coordinates
            y = random.nextInt(max_Y);

            setX(x);    // Set initial coordinates
            setY(y);

            dest_X = random.nextInt(max_X); // Generate destination coordinates
            dest_Y = random.nextInt(max_Y);

            int z = 1 + random.nextInt(10); // Generate circle type (red or green)
            mType = (z % 2) == 0;

            // Set circle color
            if (mType == RED) {
                setImageDrawable(getResources().getDrawable(R.drawable.red_circle));
            }
            if (mType == GREEN) {
                greenNumAfterAcceleration++;
                setImageDrawable(getResources().getDrawable(R.drawable.green_circle));
            }

            circleHit = CIRCLE_MISSED;  // Initially circle is missed

            setVisibility(VISIBLE);
            startMovement();
        } else {
            gameEnd();
        }
    }

    // Game over
    private void gameEnd() {
        setVisibility(GONE);

        if (greenMissedNum == MAX_GREEN_MISSED) {
            mCircleActivity.gameEndGreenMissed();
        }
        if (redHitNum == MAX_RED_HIT) {
            mCircleActivity.gameEndRedHit();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {    // Circle hit
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            popCircle();
        }

        return true;
    }

    // Set circleHit flag and stop movement
    private void popCircle() {
        circleHit = CIRCLE_HIT;
        stopMovement();
    }

    // Calculate number of circles hit
    private void countHitScore() {
        if (mType == RED) {
            mPopSound.redPop();
            redHitNum++;
        } else {
            mPopSound.greenPop();
            greenHitNum++;
        }
    }

    // Calculate number of green circles missed
    private void countMissScore() {
        if (circleHit == CIRCLE_MISSED && mType == GREEN) {
            greenMissedNum++;
        }
    }

    public void startMovement() {
        // Horizontal animator
        ObjectAnimator widthAnimator = ObjectAnimator.ofFloat(Circle.this, "x", x, dest_X)
                .setDuration(moveDuration);
        widthAnimator.setInterpolator(new AccelerateInterpolator());

        // Vertical animator
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
                // onAnimationCancel is called only when circle is hit (when we force circle to stop)
                countHitScore();
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        mAnimatorSet.start();
    }

    private void stopMovement() {
        mAnimatorSet.cancel();  // Call onAnimationCancel
    }

    // Calculate new move duration
    private void initMoveDuration() {
        // Move duration decreases every time when we generate MAX_GREEN_CIRCLES_AFTER_ACCELERATION after previous duration decrease
        if (greenNumAfterAcceleration == MAX_GREEN_CIRCLES_AFTER_ACCELERATION) {
            moveDurationDivider += ACCELERATION_INCREMENT;
            greenNumAfterAcceleration = 0;
        }

        moveDuration = (int) (MAX_MOVE_DURATION / moveDurationDivider);
    }

    public int getRedHitNum() {
        return redHitNum;
    }

    public int getGreenHitNum() {
        return greenHitNum;
    }

    public int getGreenMissedNum() {
        return greenMissedNum;
    }
}
