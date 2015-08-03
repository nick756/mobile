package com.nova.sme.sme01.miscellanea;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/*
 ***********************************
 *                                 *
 *   Selected view's border        *
 *   is flickering                 *
 *                                 *
 ***********************************
 */
public class BorderFlicker {
    private Paint paint  = new Paint();
    private int   red    = 0;
    private int   green  = 0;
    private int   blue   = 0;
    private int   incr   = 5;

    public BorderFlicker() {
        paint.setStyle(Paint.Style.STROKE);
    }


    public void draw (Canvas canvas, int width, int height) {
        paint.setStrokeWidth(Math.max(height, width)/60);
        paint.setColor(Color.rgb(red, green, blue));

        red += incr;green += incr;blue += incr;

        if (red > 255 || green > 255 || blue > 255) {
            incr = -3;
            red = 255; green = 255; blue = 255;
        } else if (red < 0 || green < 0 || blue < 0) {
            incr = 3;
            red = 0; green = 0; blue = 0;
        }

        canvas.drawLine(0, 0, width, 0, paint);
        canvas.drawLine(width, 0, width, height, paint);
        canvas.drawLine(width, height, 0, height, paint);
        canvas.drawLine(0, height, 0, 0, paint);
    }
}
