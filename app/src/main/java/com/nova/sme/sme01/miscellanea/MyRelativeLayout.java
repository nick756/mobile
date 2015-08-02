package com.nova.sme.sme01.miscellanea;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.RelativeLayout;


public class MyRelativeLayout extends RelativeLayout {
    private Paint paint = new Paint();
    private Rect  rect  = new Rect();

    public MyRelativeLayout(Context context) {
        super(context);

        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(5);
        paint.setStyle(Paint.Style.STROKE);

        if (!isInEditMode()) {

        }

    }

    public MyRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(5);
        paint.setStyle(Paint.Style.STROKE);

        if (!isInEditMode()) {
            //          createTypeface(context, attrs); //whatever added functionality you are trying to add to Widget, call that inside this condition.
        }
    }

    public MyRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
        this(context, attrs);
    }


    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

    }

}
