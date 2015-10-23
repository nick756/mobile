package com.nova.sme.sme01.miscellanea;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nova.sme.sme01.R;

import static java.sql.DriverManager.println;

/*
 ********************************
 *                              *
 *  Create own action bar with  *
 *  Logout button               *
 *                              *
 ********************************
 */
public class CustomBar {
    private RelativeLayout base_layout;
    private ActionBar      actionBar;
    private Button         button = null;

    public  Button getButton(){ return button;}

    private RelativeLayout layout;

    public RelativeLayout getBase() {
        RelativeLayout rl = (RelativeLayout)layout.findViewById(R.id.custom_layout);
        rl.setTag("actionbar_background_color");
        return rl;
    }
    public TextView getTitle() {
        TextView text = (TextView)layout.findViewById(R.id.title_id);
        text.setTag("actionbar_background_color");
        return text;
    }

    public void setBackgound() {
        ApplicationAttributes attr = (ApplicationAttributes) new FileManager(base_layout.getContext()).readFromFile("attributes.bin");
        if (attr == null) return;

        int color = attr.getColors().getActionbar_background_color();
        if (color != -1)
            actionBar.setBackgroundDrawable(new ColorDrawable(color));
    }



    public CustomBar(AppCompatActivity activity, RelativeLayout base_layout) {

        try {
            this.base_layout = base_layout;

            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(activity.getBaseContext().LAYOUT_INFLATER_SERVICE);
            layout                  = (RelativeLayout) inflater.inflate(R.layout.custom_title_bar, null);
            layout.setPadding(0, 10, 0, 10);

            actionBar = activity.getSupportActionBar();
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setDisplayShowTitleEnabled(true);


            actionBar.setCustomView(layout);

            float button_factor = 0.25f;
            int width = base_layout.getWidth();
            float height;
            float h_margin;
            button = (Button) activity.findViewById(R.id.logout_button);
//            if (button != null)
//                button.setWidth((int) ((float) width * button_factor));

            setBackgound();
        } catch(Exception err) {
            println(err.getMessage().toString());
        }
    }
    public CustomBar(AppCompatActivity activity, RelativeLayout base_layout, String caption) {

        try {
            this.base_layout = base_layout;

            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(activity.getBaseContext().LAYOUT_INFLATER_SERVICE);
            layout                  = (RelativeLayout) inflater.inflate(R.layout.custom_title_bar, null);
            layout.setPadding(0, 10, 0, 10);

            actionBar = activity.getSupportActionBar();
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setDisplayShowTitleEnabled(true);


            actionBar.setCustomView(layout);

            float button_factor = 0.25f;
            int width = base_layout.getWidth();
            float height;
            float h_margin;
            button = (Button) activity.findViewById(R.id.logout_button);

            button.setText(caption);
//            if (button != null)
//                button.setWidth((int) ((float) width * button_factor));

            setBackgound();
        } catch(Exception err) {
            println(err.getMessage().toString());
        }
    }

}
