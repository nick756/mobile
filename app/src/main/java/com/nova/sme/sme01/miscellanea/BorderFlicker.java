package com.nova.sme.sme01.miscellanea;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathDashPathEffect;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.graphics.SumPathEffect;

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

    private int    radius;//4;
    private float  density;
    private float  scaleDensity;

    private Path                     shapePath;
    private int                      phase    = 0;
    private float                    advance  = 10.0f;
    private PathDashPathEffect.Style style    = PathDashPathEffect.Style.ROTATE;
    private Rect                     rect;

    public BorderFlicker(float density, float  scaleDensity) {
        this.density      = density;
        this.scaleDensity = scaleDensity;

        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        paint.setColor(Color.WHITE);
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
            radius = 2;//(int)(2.0f*density);

            path = new Path();
            path.addRect(0, 0, width, height, Path.Direction.CW);

            shapePath = new Path();
            shapePath.addCircle(radius, radius, radius, Path.Direction.CW);

            rect = new Rect(0, 0, width, height);
        }

        drawRectangle(canvas, Color.BLACK, width, height, radius*2);

        paint.setColor(Color.WHITE);
        paint.setPathEffect(new PathDashPathEffect(shapePath, advance, ++phase, style));
        canvas.drawPath(path, paint);

    }
    private void drawRectangle(Canvas canvas, int color, int width, int height, int strokeWidth) {
        paint.setColor(color);
        paint.setStrokeWidth(strokeWidth);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(rect, paint);
    }
/*
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        switch(metrics.densityDpi){
             case DisplayMetrics.DENSITY_LOW:
                        break;
             case DisplayMetrics.DENSITY_MEDIUM:
                         break;
             case DisplayMetrics.DENSITY_HIGH:
                         break;
}

     */
}
