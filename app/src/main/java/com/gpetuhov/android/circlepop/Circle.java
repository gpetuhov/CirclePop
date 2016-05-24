package com.gpetuhov.android.circlepop;

import android.graphics.Color;
import android.view.MotionEvent;

import java.util.Random;

public class Circle {
    public static final int RADIUS = 100;
    public static final boolean RED = true;
    public static final boolean GREEN = false;

    private int x;
    private int y;
    private int radius = RADIUS;
    private boolean mType;  //true - RED; false - GREEN
    private int mColor;

    public Circle(int x, int y, boolean type) {
        this.x = x;
        this.y = y;
        mType = type;
        if (mType == RED) {
            mColor = Color.RED;
        } else {
            mColor = Color.GREEN;
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getRadius() {
        return radius;
    }

    public boolean getType() {
        return mType;
    }

    public int getColor() {
        return mColor;
    }

    public boolean isTargetHit(float eventX, float eventY) {
        int distance; // distance of touch coordinates from circle center

        // distance ^ 2 = (x - eventX) ^ 2 + (y - eventY) ^ 2
        distance = (int) Math.sqrt(Math.pow(x - eventX,2) + Math.pow(y - eventY,2));
        return distance <= radius;
    }

    public static Circle getRandomCircle(int max_X, int max_Y) {
        Random random = new Random();
        int x = random.nextInt(max_X);
        int y = random.nextInt(max_Y);
        int z = random.nextInt(10);
        boolean type = (z % 2) == 0;
        Circle circle = new Circle(x, y, type);
        return circle;
    }
}
