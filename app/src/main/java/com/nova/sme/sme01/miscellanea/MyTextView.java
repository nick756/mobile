package com.nova.sme.sme01.miscellanea;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.TextView;

//
public class MyTextView extends TextView {
    private boolean       flag    = false;
    private BorderFlicker flicker;


    public MyTextView(Context context) {
        super(context);
        if (!isInEditMode()) {

        }
        init();
    }
    public MyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode()) {

        }
        init();
    }

    public MyTextView(Context context, AttributeSet attrs, int defStyle) {
       this(context, attrs);
    }

    private void init() {
        flicker = new BorderFlicker(this.getContext());
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

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);

    }
}
