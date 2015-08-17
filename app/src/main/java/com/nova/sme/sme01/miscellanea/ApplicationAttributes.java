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
    private MyColors        colors                = new MyColors();
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

/*
 size = 12
 size = 12
 0 = {java.lang.Integer@829650426936} "2130837596"
1 = {java.lang.Integer@829650427688}  "2130837566"
2 = {java.lang.Integer@829650428288}  "2130837587"
3 = {java.lang.Integer@829650428888}  "2130837590"
4 = {java.lang.Integer@829650429488}  "2130837569"
5 = {java.lang.Integer@829650430088}  "2130837572"
6 = {java.lang.Integer@829650430688}  "2130837575"
7 = {java.lang.Integer@829650431288}  "2130837578"
8 = {java.lang.Integer@829650431888}  "2130837581"
9 = {java.lang.Integer@829650432488}  "2130837584"
10 = {java.lang.Integer@829650433088} "2130837596"
11 = {java.lang.Integer@829650433688} "2130837596"

0 = {java.lang.Integer@831731759576}  "2130837608"
1 = {java.lang.Integer@831731838792}  "2130837566"
2 = {java.lang.Integer@831731751280}  "2130837587"
3 = {java.lang.Integer@831731991776}  "2130837590"
4 = {java.lang.Integer@831732029960}  "2130837569"
5 = {java.lang.Integer@831731874624}  "2130837572"
6 = {java.lang.Integer@831731722584}  "2130837575"
7 = {java.lang.Integer@831731757496}  "2130837578"
8 = {java.lang.Integer@831731723880}  "2130837581"
9 = {java.lang.Integer@831732126928}  "2130837584"
10 = {java.lang.Integer@831731755624} "2130837608"
11 = {java.lang.Integer@831731740016} "2130837608"

     */

    public ApplicationAttributes(Context ctx) {
        fillButtonsBackgroundIds(ctx);
    }

    private void fillButtonsBackgroundIds(Context ctx) {
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

    public MyColors getColors(){return colors;}

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
            btn.setBackground(ctx.getResources().getDrawable(id));
        }
        btn.setTextColor(selected_button_color);
    }
    /*
    private int getResourceID(Context ctx) {
        try {
            return ctx.getResources().getIdentifier(resName, "drawable", ctx.getApplicationInfo().packageName);
        } catch (Exception e) {
            err = e.getMessage().toString();
        }
        return -1;//@drawable/login_button_selector
    }
    */
}
