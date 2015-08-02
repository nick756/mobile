package com.nova.sme.sme01.miscellanea;

//com.nova.sme.sme01.miscellanea.MyLayout

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import com.nova.sme.sme01.R;

public class MyLayout extends LinearLayout {

    public MyLayout(Context context) {
        super(context);
    }

    public MyLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode()) {
            createTypeface(context, attrs); //whatever added functionality you are trying to add to Widget, call that inside this condition.
        }

    }

    public MyLayout(Context context, AttributeSet attrs, int defStyle) {
        this(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
/*
        int x=80;
        int y=80;
        int radius=40;
        Paint paint=new Paint();
        // Use Color.parseColor to define HTML colors
        paint.setColor(Color.parseColor("#CD5C5C"));
        canvas.drawCircle(x, x, radius, paint);
*/
    }
    private void createTypeface(Context context, AttributeSet attrs) {

        TypedArray styledAttrs = context.obtainStyledAttributes(attrs, R.styleable.LinearLayoutCompat_Layout);
        styledAttrs.recycle();

    }
}
