package com.nova.sme.sme01.miscellanea;

import android.content.Context;
import android.graphics.Canvas;
import android.widget.TextView;


public class CustomTextView extends TextView {
    private int viewWidth  = 0;
    private int viewHeight = 0;

    public CustomTextView(Context context) {
        super(context);
    }
    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        String msg = "width: " + viewWidth + "height: " + viewHeight;
        System.out.println(msg);
    }
    @Override
    protected void onSizeChanged(int xNew, int yNew, int xOld, int yOld){
        super.onSizeChanged(xNew, yNew, xOld, yOld);
        viewWidth  = xNew;
        viewHeight = yNew;
    }
}
