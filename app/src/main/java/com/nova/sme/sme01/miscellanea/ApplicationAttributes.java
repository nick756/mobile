package com.nova.sme.sme01.miscellanea;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


import com.nova.sme.sme01.R;

import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;
import java.io.Serializable;
import java.util.Vector;

import static java.sql.DriverManager.println;


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
//    private Button  button;
    private Context         context;
    private WindowMetrics   wm;
    private MyColors        colors;//                = new MyColors();
    private int             selected_button       = 0;// default
    private int             selected_button_color = Color.rgb(0, 0, 0);// default

    private Vector<Integer> buttons_text_colors   = new Vector<Integer>();
    private Vector<Integer> button_background_ids = new Vector<Integer>();

    private String          base_url              = "http://" + "103.6.239.242:80/sme/mobile/";

    public int    getSelectedButton()              {return selected_button;}
    public void   setSelectedButton(int selected) {selected_button = selected;}

    public String getBaseUrl()           {return base_url;}
    public void   setBaseUrl(String url) {
        base_url = "http://" + url;
    }

    public ApplicationAttributes(Context ctx) {
        fillButtonsBackgroundIds(ctx);

        this.context   = context;
        FileManager FM = new FileManager(ctx);
        this.wm        = (WindowMetrics)FM.readFromFile("windowMetrics.bin");

        colors         = new MyColors(wm);
    }

    private void fillButtonsBackgroundIds(Context ctx) {
        String names[] = {
                "login_button_selector",
                "button_1_selector",
                "button_8_selector",
                "button_9_selector",
                "button_2_selector",
                "button_3_selector",
                "button_4_selector",
                "button_5_selector",
                "button_6_selector",
                "button_7_selector",

        };
        int id = 0;
        for (int j = 0; j < names.length;j ++) {
            id = getResourceID(ctx, names[j]);
            button_background_ids.add(id);
        }


/*
        button_background_ids.add(2130837608);
        button_background_ids.add(2130837566);
        button_background_ids.add(2130837587);
        button_background_ids.add(2130837590);
        button_background_ids.add(2130837569);
        button_background_ids.add(2130837572);
        button_background_ids.add(2130837575);
        button_background_ids.add(2130837578);
        button_background_ids.add(2130837581);
        button_background_ids.add(2130837584);
*/
/*

        Resources res = ctx.getResources();
        int cnt;
        XmlResourceParser xpp = res.getLayout(R.layout.buttons);// .getXml(R.layout.buttons);
        String str = "", attr_name = "";
        int background_id = 0;
        try {
            xpp.next();
            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                 if (eventType == XmlPullParser.START_TAG) {
                    str = xpp.getName();
                    if (str.equals("Button")) {
                        cnt = xpp.getAttributeCount();
                        for (int j = 0; j < cnt; j ++) {
                            attr_name = xpp.getAttributeName(j) ;
                            if (attr_name.equals("background")) {
                                background_id = xpp.getAttributeResourceValue(j, 0);
                                button_background_ids.add(background_id);
                            }
                         }
                    }
                }
                eventType = xpp.next();
            }
        } catch(org.xmlpull.v1.XmlPullParserException e) {

        } catch (java.io.IOException e) {

        }
*/
    }

    public MyColors getColors(){
        WindowMetrics w_m = colors.getWM();
        if (w_m == null) {
            FileManager FM = new FileManager(context);
            wm = (WindowMetrics)FM.readFromFile("windowMetrics.bin");
            colors.setWM(wm);
        }

        return colors;
    }

    public  Vector<Integer> getButtonColors(){return buttons_text_colors;}


    public void setButtonColors(Vector<ViewsGroup> views_group) {
        buttons_text_colors.clear();
        ViewsGroup vg;
        int     pos;
        for (int i = 0; i < views_group.size(); i ++) {
            vg  = views_group.get(i);
            pos = vg.sb.getProgress();
            buttons_text_colors.add(Color.rgb(pos, pos, pos));
        }
    }

    public int  getSelectedButtonColor(){return selected_button_color;}
    public void setSelectedButtonColor(int color) {selected_button_color = color;}

    public void setButtons(View base_layout, Vector<Button> buttons) {
        Context ctx = base_layout.getContext();

        int     id  = button_background_ids.get(selected_button);

        for (int i = 0; i < buttons.size(); i ++)
            set_button(buttons.elementAt(i), ctx, id);
    }
    public void setButtons(View base_layout) {
        Context ctx = base_layout.getContext();
        int id = button_background_ids.get(selected_button);
        if (id != -1)
            set_Buttons(ctx, base_layout, id);
    }

    public void setButtons(View base_layout, Button logout) {
        Context ctx = base_layout.getContext();
        int id = button_background_ids.get(selected_button);
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
            btn.setBackgroundDrawable(ctx.getResources().getDrawable(id));
        } else {
            try {
                btn.setBackground(ctx.getResources().getDrawable(id));
            } catch(Exception e) {
                println(e.getMessage().toString());
            }
        }
        btn.setTextColor(selected_button_color);
    }
    /*
    login_button_selector
    button_1_selector
    button_8_selector
    button_9_selector
    button_2_selector
    button_3_selector
    button_4_selector
    button_5_selector
    button_6_selector
    button_7_selector
*/
    private int getResourceID(Context ctx, String resName) {
        try {
            return ctx.getResources().getIdentifier(resName, "drawable", ctx.getApplicationInfo().packageName);
        } catch (Exception e) {
            println(e.getMessage().toString());
        }
        return -1;//@drawable/login_button_selector
    }

}
