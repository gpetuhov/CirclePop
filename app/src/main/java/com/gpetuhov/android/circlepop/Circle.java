package com.gpetuhov.android.circlepop;

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
    public static final int MOVE_DURATION = 3000;

    private int x;  // initial coordinates
    private int y;
    private boolean mType;  //true - RED; false - GREEN

    private int dest_X; // destination coordinates
    private int dest_Y;

    private int max_X;  // maximum coordinates
    private int max_Y;

    public Circle(Context context, AttributeSet attrs) {
        super(context, attrs);
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
            setImageDrawable(getResources().getDrawable(R.drawable.green_circle));
        }

        startMovement();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            Toast.makeText(getContext(), "Target Hit!", Toast.LENGTH_SHORT).show();
            initCircle();
        }

        return true;
    }

    public void startMovement() {
        ObjectAnimator widthAnimator = ObjectAnimator.ofFloat(Circle.this, "x", x, dest_X)
                .setDuration(MOVE_DURATION);
        widthAnimator.setInterpolator(new AccelerateInterpolator());

        ObjectAnimator heightAnimator = ObjectAnimator.ofFloat(Circle.this, "y", y, dest_Y)
                .setDuration(MOVE_DURATION);
        heightAnimator.setInterpolator(new AccelerateInterpolator());

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet
                .play(widthAnimator)
                .with(heightAnimator);
        animatorSet.start();
    }
}
