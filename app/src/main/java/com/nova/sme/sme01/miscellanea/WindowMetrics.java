package com.nova.sme.sme01.miscellanea;

import android.app.Activity;
import android.graphics.Point;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import java.io.Serializable;

/*
 ***************************
 *                         *
 *   Get display metrics   *
 *                         *
 ***************************
 */
public class WindowMetrics implements Serializable {
    public int       widthPixels;
    public int       heightPixels;
    public float     density;
    public float     scaledDensity;

    public void init(Activity activity) {
        this.density       = activity.getResources().getDisplayMetrics().density;
        this.scaledDensity = activity.getResources().getDisplayMetrics().scaledDensity;

        WindowManager  w       = activity.getWindowManager();
        Display        d       = w.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        d.getMetrics(metrics);
        // since SDK_INT = 1;
        widthPixels  = metrics.widthPixels;
        heightPixels = metrics.heightPixels;
        // includes window decorations (statusbar bar/menu bar)
        if(Build.VERSION.SDK_INT >= 14 && Build.VERSION.SDK_INT < 17)
            try {
                widthPixels  = (Integer) Display.class.getMethod("getRawWidth").invoke(d);
                heightPixels = (Integer) Display.class.getMethod("getRawHeight").invoke(d);
            } catch(Exception ignored) {

            }
        // includes window decorations (statusbar bar/menu bar)
        if(Build.VERSION.SDK_INT >= 17) {
            try {
                Point realSize = new Point();
                Display.class.getMethod("getRealSize", Point.class).invoke(d, realSize);
                widthPixels  = realSize.x;
                heightPixels = realSize.y;
            } catch (Exception ignored) {

            }
        }
    }

    public WindowMetrics() {
    }
}
