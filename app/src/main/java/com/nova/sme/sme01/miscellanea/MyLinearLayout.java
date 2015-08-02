package com.nova.sme.sme01.miscellanea;

//com.nova.sme.sme01.miscellanea.MyLayout

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import com.nova.sme.sme01.R;

public class MyLinearLayout extends LinearLayout {
    private Paint paint = new Paint();

    public MyLinearLayout(Context context) {
        super(context);
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(5);
        paint.setStyle(Paint.Style.STROKE);
    }

    public MyLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(5);
        paint.setStyle(Paint.Style.STROKE);

        if (!isInEditMode()) {
  //          createTypeface(context, attrs); //whatever added functionality you are trying to add to Widget, call that inside this condition.
        }
    }

    public MyLinearLayout(Context context, AttributeSet attrs, int defStyle) {
        this(context, attrs);
    }


    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

     }

    private void createTypeface(Context context, AttributeSet attrs) {

    }
}
