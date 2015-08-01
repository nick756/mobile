package com.nova.sme.sme01.miscellanea;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.Serializable;
import java.util.Vector;

/**
 * Created by User on 01.08.2015.
 */
public class MColors implements Serializable {
    // colors
    private int selected_color_choise = 0;

    private int actionbar_background_color = -1;
    private int main_background_color      = -1;
    private int text_background_color      = -1;
    private int dialog_background_color    = -1;
    // colors

    public void setSelected_color_choise(int val) {
        selected_color_choise = val;
    }
    public void setActionbar_background_color(int val) {
        actionbar_background_color = val;
    }
    public void setMain_background_color(int val) {
        main_background_color = val;
    }
    public void setText_background_color(int val) {
        text_background_color = val;
    }
    public void setDialog_background_color(int val) {
        dialog_background_color = val;
    }
    //--------------------------------------------------------//
    public int getSelected_color_choise() {
        return selected_color_choise;
    }
    public int getActionbar_background_color() {
        return actionbar_background_color;
    }
    public int getMain_background_color() {
        return main_background_color;
    }
    public int getText_background_color() {
        return text_background_color;
    }
    public int getDialog_background_color() {
        return dialog_background_color;
    }

    public void setColors(Vector<View> views) {
        View   view;
        String tag;
        int    color = -1;

        for (int i = 0; i < views.size(); i ++) {
            view  = views.get(i);
            tag   = (String) view.getTag();
            color = - 1;
            if (tag.equals("actionbar_background_color"))
                color = actionbar_background_color;
            else if (tag.equals("main_background_color"))
                color = main_background_color;
            else if (tag.equals("text_background_color"))
                color = text_background_color;
            else if (tag.equals("dialog_background_color"))
                color = dialog_background_color;

            setViewColor(view, color);

        }
    }
    private void setViewColor(View view, int color) {
        if (color == -1) return;
        String   class_name = view.getClass().getName().toUpperCase();

        if (class_name.indexOf("TEXTVIEW") != -1) {
            TextView text = (TextView) view;
            text.setBackgroundColor(color);
        } else if (class_name.indexOf("LINERALAYOUT") != -1) {
            setLayoutColor(color, view);
        } else if (class_name.indexOf("RELATIVELAYOUT") != -1) {
            setLayoutColor(color, view);
        } else {

        }
    }
    private void setLayoutColor(int color, View view) {
        if (color == dialog_background_color) {
            GradientDrawable shape;

            shape = new GradientDrawable();
            shape.setColor(color);
            shape.setCornerRadius(6);
            shape.setStroke(2, Color.BLACK);
            view.setBackgroundDrawable(shape);
        } else {
            RelativeLayout rl = (RelativeLayout) view;
            rl.setBackgroundColor(color);
        }
    }



    public MColors() {

    }
}

