package com.nova.sme.sme01.miscellanea;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.TextView;


public class MyTextView extends TextView {
    private boolean flag  = false;
    private Paint   paint = new Paint();
    private Rect    rect  = new Rect();

    public MyTextView(Context context) {
        super(context);
        if (!isInEditMode()) {

        }
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(5);
        paint.setStyle(Paint.Style.STROKE);

    }
    public MyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode()) {

        }
    }

    public MyTextView(Context context, AttributeSet attrs, int defStyle) {
        this(context, attrs);
    }

    public void startAnimation(){flag = true;}
    public void stopAnimation() {flag = false;}

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        if (flag) {
            int height = this.getHeight();
            int width  = this.getWidth();

            canvas.drawRect((float)0,  (float)0,  (float)width,  (float) height, paint);
 //           invalidate();
        }

    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);

    }
}
