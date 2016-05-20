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
    Circle mCircle;

    public DrawCircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mCircle = new Circle(300, 300, Circle.RED);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(mCircle.getColor());
        canvas.drawCircle(mCircle.getX(), mCircle.getY(), mCircle.getRadius(), paint);
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
}
