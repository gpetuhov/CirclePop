package com.gpetuhov.android.circlepop;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

// Background for circles
public class DrawCircleView extends View {
    private Circle mCircle;
    private Paint mPaint;
    private Canvas mCanvas;

    public DrawCircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mCanvas = canvas;
        mCircle = new Circle(getWidth() / 2, getHeight() / 2, Circle.RED);
        drawCircle(mCircle);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (mCircle.isTargetHit(event.getX(), event.getY())) {
                Toast.makeText(getContext(), "Target hit!", Toast.LENGTH_SHORT).show();
            }
        }

        return true;
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
    }

    private void drawCircle(Circle circle) {
        mPaint.setColor(mCircle.getColor());
        mCanvas.drawCircle(circle.getX(), circle.getY(), circle.getRadius(), mPaint);
    }
}
