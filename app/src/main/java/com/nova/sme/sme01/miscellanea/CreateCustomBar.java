package com.nova.sme.sme01.miscellanea;

import android.app.Activity;
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
 **************************************
 *                                    *
 *  Set Logout button on action bar   *
 *                                    *
 **************************************
 */
public class CreateCustomBar {
    private Button button = null;
    public  Button getButton(){ return button;}

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

    private RelativeLayout layout;

    public CreateCustomBar(AppCompatActivity activity, RelativeLayout base_layout) {

        try {
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(activity.getBaseContext().LAYOUT_INFLATER_SERVICE);
            layout                  = (RelativeLayout) inflater.inflate(R.layout.custom_title_bar, null);
            layout.setPadding(0, 10, 0, 10);

            ActionBar actionBar = activity.getSupportActionBar();
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setDisplayShowTitleEnabled(true);

            actionBar.setCustomView(layout);
            float button_factor = 0.25f;
            int width = base_layout.getWidth();
            float height;
            float h_margin;
            button = (Button) activity.findViewById(R.id.logout_button);
            if (button != null) {
                button.setWidth((int) ((float) width * button_factor));
 //               RelativeLayout rl = (RelativeLayout)layout.findViewById(R.id.custom_layout);
 //               button.setTag(rl);
            }

        } catch(Exception err) {
            println(err.getMessage().toString());
        }
    }
}
