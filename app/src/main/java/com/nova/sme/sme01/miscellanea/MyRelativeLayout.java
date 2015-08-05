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
    private BorderFlicker flicker;// = new BorderFlicker();


    public MyRelativeLayout(Context context) {
        super(context);

        if (!isInEditMode()) {

        }
        init();
    }

    public MyRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (!isInEditMode()) {

        }
        init();
    }

    public MyRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
        this(context, attrs);
    }
    private void init() {
        flicker = new BorderFlicker(this.getResources().getDisplayMetrics().density, this.getResources().getDisplayMetrics().scaledDensity);
    }

    public void startAnimation(){flag = true;}
    public void stopAnimation() {flag = false;}
    public void setDash(boolean dash) {flicker.setDash(dash);}
    public void setColor(int red, int green, int blue) {flicker.setColor(red, green, blue);}


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
