package com.nova.sme.sme01.miscellanea;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.RelativeLayout;


public class MyRelativeLayout extends RelativeLayout {
    private boolean       flag    = false;
    private BorderFlicker flicker = new BorderFlicker();


    public MyRelativeLayout(Context context) {
        super(context);


        if (!isInEditMode()) {

        }
    }

    public MyRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (!isInEditMode()) {

        }
    }

    public MyRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
        this(context, attrs);
    }

    public void startAnimation(){flag = true;}
    public void stopAnimation() {flag = false;}


    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        if (flag) {
            flicker.draw(canvas, this.getWidth(), this.getHeight());
            invalidate();
        }
    }

}
