package com.nova.sme.sme01.miscellanea;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;

/*
 ****************************************
 *                                      *
 *  Used to create the list of buttons  *
 *  to be selected                      *
 *                                      *
 ****************************************
 */
public class ShapeBuilder {
    public static Drawable generateSelectorFromDrawables(Drawable pressed, Drawable normal) {
        StateListDrawable states = new StateListDrawable();
        states.addState(new int[]{ -android.R.attr.state_focused, -android.R.attr.state_pressed, -android.R.attr.state_selected}, normal);
        states.addState(new int[]{ android.R.attr.state_pressed}, pressed);
        states.addState(new int[]{ android.R.attr.state_focused}, pressed);
        states.addState(new int[]{ android.R.attr.state_selected}, pressed);

        return states;
    }

    public static Drawable generateShape(String colorTop, String colorBot, String colorStroke, int stokeSize, float strokeRadius) {
        int top, bot, stroke;
        top    = Color.parseColor(colorTop);
        bot    = Color.parseColor(colorBot);
        stroke = Color.parseColor(colorStroke);

        GradientDrawable drawable = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, new int[]{top, bot});
        drawable.setStroke(stokeSize, stroke);
        drawable.setCornerRadius(strokeRadius);

        return drawable;
    }

    public static Drawable buildSelectorShapeFromColors(String colorNormalStroke,  String colorNormalBackTop,  String colorNormalBackBot,
                                                        String colorPressedStroke, String colorPressedBackTop, String colorPressedBackBot,
                                                        int strokeSize, float strokeRadius) {

        Drawable pressed = generateShape(colorPressedBackTop, colorPressedBackBot, colorPressedStroke, strokeSize, strokeRadius);
        Drawable normal  = generateShape(colorNormalBackTop,  colorNormalBackBot,  colorNormalStroke,  strokeSize, strokeRadius);
        return generateSelectorFromDrawables(pressed, normal);
    }
}
/*
        android:startColor="#9284F0"  146 132 240
        android:endColor="#262461"     38  36 97
*/
