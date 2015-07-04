package com.nova.sme.sme01;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/*
 *******************************************************************************
 *                                                                             *
 *   PlaceViews class provides device independent placing of components        *
 *   for login procedure                                                       *
 *                                                                             *
 *******************************************************************************
 */

public class PlaceViews {
    private AppCompatActivity activity      = null;
    private android.widget.RelativeLayout   base_layout    = null;
    private android.widget.RelativeLayout   data_layout    = null;
    private TextView                        caption        = null;
    private Button                          login_button   = null;
    private ImageView                       user_icon      = null;
    private ImageView                       password_icon  = null;
    private EditText                        user_name      = null;
    private EditText                        password       = null;

    private float                           width_margin   = 0.0f;
    private float                           height_margin  = 0.0f;


    PlaceViews(AppCompatActivity activity, android.widget.RelativeLayout base_layout) {
        this.base_layout = base_layout;
        this.activity    = activity;

        data_layout   = (RelativeLayout) activity.findViewById(R.id.data_layout);
        user_name     = (EditText)       activity.findViewById(R.id.user_name);
        password      = (EditText)       activity.findViewById(R.id.password);
        caption       = (TextView)       activity.findViewById(R.id.cap_text);
        login_button  = (Button)         activity.findViewById(R.id.button_login);
        user_icon     = (ImageView)      activity.findViewById(R.id.user_icon);
        password_icon = (ImageView)      activity.findViewById(R.id.password_icon);
    }

    void set_parent_margins(float w, float h) {this.width_margin = w;this.height_margin = h;}

    EditText getUserName(){return this.user_name;}
    EditText getPassword(){return this.password;}

    public void Placing() {
        // DATA_LAYOUT
        float width  = base_layout.getWidth();
        float height = base_layout.getHeight();

        height -= this.height_margin*2.0f;
        width  -= this.width_margin*2.0f;

        // these numbers are taken from jpg prototype
        float height_factor = (1077.0f - 565.0f)/1600.0f;
        float width_factor  = (873.0f - 29.0f)/900.0f;
        float up_factor     = 0.2f; // margin_top/total_margin_height

        float data_layout_width  =  width*width_factor;
        float data_layout_height =  height*height_factor;
        float margin_width       =  (width - data_layout_width)/2.0f;

        android.view.ViewGroup.MarginLayoutParams params = (android.view.ViewGroup.MarginLayoutParams) data_layout.getLayoutParams();
        params.leftMargin  = (int) margin_width;
        params.rightMargin = (int) margin_width;

        float total_margin_height = height - data_layout_height;
        float margin_top          = total_margin_height*up_factor;
        float margin_bottom       = total_margin_height - margin_top;

        params.topMargin    = (int) margin_top;
        params.bottomMargin = (int) margin_bottom;
        // DATA_LAYOUT


        // CAPTION
        height_factor        =  (669.0f - 566.0f)/(1077.0f - 565.0f);
        float caption_height = height_factor * data_layout_height;

        caption.setPadding(0, 0, 0, 0);
        caption.setHeight((int) caption_height);

        TextAlignment ta = new TextAlignment(caption.getContext());
  //      ta.ResizeFontSizeByHeight(caption, caption.getText().toString(), 1.7f, (int)(caption_height*0.9));
        caption.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        // CAPTION

        float width_group       = data_layout_width * 0.8f;
        float side_margin_group = (data_layout_width - width_group)/4.0f;

        float delta = (data_layout_height - caption_height*4.0f)/4.0f;
        // USER NAME

        set_height(user_name, caption_height);
        user_name.setGravity(Gravity.CENTER_VERTICAL);
        set_margins(user_name, side_margin_group, caption_height + delta);

//        ta.ResizeFontSizeByHeight(user_name, user_name.getText().toString(), 1.9f, (int) (caption_height * 0.7));

        user_name.setPadding((int) (data_layout_width * 0.02f), 0, 0, 0);
        user_name.setText("");
        user_name.setHint("User Name");

        user_name.setInputType(android.text.InputType.TYPE_CLASS_TEXT);
        // USER NAME

        // PASSWORD

        set_height(password, caption_height);
        password.setGravity(Gravity.CENTER_VERTICAL);
//        ta.ResizeFontSizeByHeight(password, password.getText().toString(), 1.9f, (int) (caption_height * 0.7));

        set_margins(password, side_margin_group, caption_height + caption_height + delta + delta);

        password.setPadding((int) (data_layout_width * 0.02f), 0, 0, 0);
        password.setHint("Password");
        password.setText("");
        // PASSWORD


        // LOGIN BUTTON
        set_height(login_button, caption_height);
        login_button.setPadding(0, 0, 0, 0);
        login_button.setHeight((int) caption_height);
        login_button.setText("Login");
//        ta.ResizeFontSizeByHeight(login_button, login_button.getText().toString(), 1.9f, (int) (caption_height * 0.7));
        set_margins(login_button, side_margin_group, caption_height + caption_height + caption_height + delta + delta + delta);
        // LOGIN BUTTON


        // ICONs
        setIconMargins(user_icon, side_margin_group, caption_height, delta, 1.0f);
        setIconMargins(password_icon, side_margin_group, caption_height, delta, 2.0f);
        // ICONS

    }

    private void setIconMargins(ImageView icon, float side_margin_group, float caption_height, float delta, float factor) {
        android.view.ViewGroup.MarginLayoutParams params;
        params = (android.view.ViewGroup.MarginLayoutParams) icon.getLayoutParams();

        params.topMargin   = (int) ((caption_height + delta)*factor);//140
        params.rightMargin = (int) side_margin_group;                // 50
        params.height      = (int) caption_height;                   // 112
        params.width       = (int) caption_height;

        int padd = (int) (caption_height*0.1f);//11
        icon.setPadding(padd, padd, padd, padd);
    }

    private void set_margins(TextView view, float side_margin, float top_margin) {
        android.view.ViewGroup.MarginLayoutParams params;
        params              = (android.view.ViewGroup.MarginLayoutParams) view.getLayoutParams();
        params.leftMargin   = (int)side_margin;
        params.rightMargin  = (int)side_margin;
        params.topMargin    = (int)top_margin;
    }

    private void set_height(TextView view, float height) {
        android.view.ViewGroup.MarginLayoutParams params = (android.view.ViewGroup.MarginLayoutParams) view.getLayoutParams();
        params.height = (int) height;
    }

}
