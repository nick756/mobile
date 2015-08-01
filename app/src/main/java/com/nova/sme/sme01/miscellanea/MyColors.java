package com.nova.sme.sme01.miscellanea;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nova.sme.sme01.R;

import java.io.Serializable;
import java.util.Vector;

/*
 ********************************
 *                              *
 *   Set colors dynamically     *
 *                              *
 ********************************
 */

public class MyColors implements Serializable {
    // colors
    private int selected_color_choise = 0;

    private int actionbar_background_color = -1;
    private int main_background_color      = -1;
    private int text_background_color      = -1;
    private int dialog_background_color    = -1;
    private int exception_id_1               = R.id.relativeLayout;//linearLayout
    private int exception_id_2               = R.id.linearLayout;//linearLayout
    // colors

    public void setSelected_color_choise(int val) {
        selected_color_choise = val;
    }
    public void setActionbar_background_color(int val) {
        if (val != -1)
            actionbar_background_color = val;
    }
    public void setMain_background_color(int val) {
        if (val != -1)
            main_background_color = val;
    }
    public void setText_background_color(int val) {
        if (val != -1)
            text_background_color = val;
    }
    public void setDialog_background_color(int val) {
        if (val != -1)
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

    public void setColor(View view) {
        String tag   = (String) view.getTag();
        int    color = - 1;
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

        if (class_name.indexOf(new String("TextView").toUpperCase()) != -1) {
            TextView text = (TextView) view;
            text.setBackgroundColor(color);
        } else if (class_name.indexOf(new String("LinearLayout").toUpperCase()) != -1) {
            setLayoutColor(color, view);
        } else if (class_name.indexOf(new String("RelativeLayout").toUpperCase()) != -1) {
            setLayoutColor(color, view);
        } else {

        }
    }
    private void setLayoutColor(int color, View view) {
        int id = view.getId();
        if (color == dialog_background_color && (id != exception_id_1) && (id != exception_id_2)) {
            GradientDrawable shape;

            shape = new GradientDrawable();
            shape.setColor(color);
            shape.setCornerRadius(6);
            shape.setStroke(2, Color.BLACK);
            view.setBackgroundDrawable(shape);
        } else {
            if (view.getClass().getSimpleName().toUpperCase().indexOf(new String("RelativeLayout").toUpperCase()) != -1) {
                RelativeLayout rl = (RelativeLayout) view;
                rl.setBackgroundColor(color);
            } else if (view.getClass().getSimpleName().toUpperCase().indexOf(new String("LinearLayout").toUpperCase()) != -1) {
                LinearLayout ll = (LinearLayout) view;
                ll.setBackgroundColor(color);
            }

        }
    }

    public MyColors() {

    }
}

