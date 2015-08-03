package com.nova.sme.sme01.miscellanea;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathDashPathEffect;
import android.graphics.PathEffect;
import android.graphics.Rect;

import java.util.Vector;

/*
 ***********************************
 *                                 *
 *   Selected view's border        *
 *   is flickering                 *
 *                                 *
 ***********************************
 */
public class BorderFlicker {
    private Paint   paint  = new Paint();
    private int     red    = 0;
    private int     green  = 0;
    private int     blue   = 0;
    private int     incr   = 5;
    private Path    path   = null;
    private boolean dash   = false;


//    private int   offset = 10;
    private int   radius = 4;
//    private Paint mPaint;

    private Path                     shapePath;
    private PathDashPathEffect       pathDashPathEffect;
    private int                      phase    = 0;
    private float                    advance  = 30.0f;
    private PathDashPathEffect.Style style    = PathDashPathEffect.Style.ROTATE;


    public BorderFlicker() {
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(12);

        shapePath = new Path();
        shapePath.addCircle(radius, radius, radius, Path.Direction.CCW);
    }

    public void setDash(boolean dash){this.dash = dash;}

    public void draw (Canvas canvas, int width, int height) {
        if (dash) {
            draw_dash(canvas, width, height);
            return;
        }
        paint.setStrokeWidth(Math.min(height, width)/20);
        paint.setColor(Color.rgb(red, green, blue));

        red += incr;green += incr;blue += incr;

        if (red > 255 || green > 255 || blue > 255) {
            incr = -3;
            red  = 255; green = 255; blue = 255;
        } else if (red < 0 || green < 0 || blue < 0) {
            incr = 3;
            red  = 0; green = 0; blue = 0;
        }

        canvas.drawLine(0, 0, width, 0, paint);
        canvas.drawLine(width, 0, width, height, paint);
        canvas.drawLine(width, height, 0, height, paint);
        canvas.drawLine(0, height, 0, 0, paint);
    }

    public void setColor(int red, int green, int blue) {
        this.red = red; this.green = green; this.blue = blue;
    }

    public void draw_dash (Canvas canvas, int width, int height) {
        if (path == null) {
            path = new Path();
            path.addRect(0, 0, width, height, Path.Direction.CW);
        }


        pathDashPathEffect = new PathDashPathEffect(shapePath, advance, ++phase, style);

        paint.setPathEffect(pathDashPathEffect);
        canvas.drawPath(path, paint);
    }



}