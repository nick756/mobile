package com.nova.sme.sme01.miscellanea;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.nova.sme.sme01.R;

import java.io.Serializable;
import java.util.List;
import java.util.Vector;


/*
 ***********************************
 *                                 *
 *  Buttons views, colors,         *
 *  base url etc data to be saved  *
 *  to file and restored from file *
 *                                 *
 ***********************************
 */

public class ApplicationAttributes implements Serializable {
    private MColors         colors                = new MColors();
    private int             selected_button       = 0;// default
    private int             selected_button_color = Color.rgb(0, 0, 0);// default

    private Vector<Integer> buttons_text_colors = new Vector<Integer>();
    private String          base_url            = "http://" + "103.6.239.242:80/sme/mobile/";

    public int    getSelectedButton()              {return selected_button;}
    public void   setSelectedButton(int selected) {selected_button = selected;}

    public String getBaseUrl()           {return base_url;}
    public void   setBaseUrl(String url) {
        base_url = "http://" + url;
    }

    public ApplicationAttributes() {


    }

    public MColors getColors(){return colors;}

    public  Vector<Integer> getButtonColors(){return buttons_text_colors;}
    public void setButtonColors(List<SeekBar> sbs) {
        buttons_text_colors.clear();
        SeekBar sb;
        int     pos;
        for (int i = 0; i < sbs.size(); i ++) {
            sb  = sbs.get(i);
            pos = sb.getProgress();
            buttons_text_colors.add(Color.rgb(pos, pos, pos));
        }
    }
    public int  getSelectedButtonColor(){return selected_button_color;}
    public void setSelectedButtonColor(int color) {selected_button_color = color;}

    public void setButtons(View base_layout, Vector<Button> buttons) {
        Context ctx = base_layout.getContext();
        int     id  = getResourceID(ctx);

        for (int i = 0; i < buttons.size(); i ++)
            set_button(buttons.elementAt(i), ctx, id);
    }
    public void setButtons(View base_layout) {
        Context ctx = base_layout.getContext();
        int     id  = getResourceID(ctx);
        if (id != -1)
            set_Buttons(ctx, base_layout, id);
    }

    public void setButtons(View base_layout, Button logout) {
        Context ctx = base_layout.getContext();
        int     id  = getResourceID(ctx);
        if (id != -1) {
            set_Buttons(ctx, base_layout, id);

            if (logout != null)
                set_button(logout, ctx, id);
        }
    }
    private void set_Buttons(Context ctx, View view, int id) {
        String className = view.getClass().getName().toUpperCase().trim();
        int    cnt;
        if (className.indexOf("BUTTON") != -1 && className.indexOf("RADIOBUTTON") == -1 && className.indexOf("IMAGEBUTTON") == -1) {
            set_button((Button) view, ctx, id);
        } else  if ((view instanceof ViewGroup)) {
            ViewGroup vg = (ViewGroup) view;
            for (int i = 0; i < vg.getChildCount(); i++)
                set_Buttons(ctx, vg.getChildAt(i), id);
        }
    }
    private void set_button(Button btn, Context ctx, int id) {
        int     sdk = android.os.Build.VERSION.SDK_INT;
        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            btn.setBackgroundDrawable(ctx.getResources().getDrawable(id) );
        } else {
            btn.setBackground(ctx.getResources().getDrawable(id));
        }
        btn.setTextColor(selected_button_color);
    }
    private int getResourceID(Context ctx) {
        String err = "";

        String resName;
        if (selected_button > 0)
            resName = "@drawable/button_" + selected_button + "_selector";
        else
            resName = "@drawable/login_button_selector";


        try {
            return ctx.getResources().getIdentifier(resName, "drawable", ctx.getApplicationInfo().packageName);
        } catch (Exception e) {
            err = e.getMessage().toString();
        }
        return -1;
    }
}
