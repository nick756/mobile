package com.nova.sme.sme01;

//import android.app.Activity;
import android.app.Activity;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Vector;


/*
 *******************************************************************************
 *                                                                             *
 *   FormResizing class provides device independent view                       *
 *                                                                             *
 *******************************************************************************
 */

public class FormResizing {
    private AppCompatActivity activity;
    private android.widget.RelativeLayout base_layout;

 //   private int   base_height = 0;
    private float   width_virt     = 1080.0f;
    private float   height_virt    = 1696.0f;
    private float   width_margin   = 0.0f;
    private float   height_margin  = 0.0f;

    public FormResizing(AppCompatActivity activity, RelativeLayout base_layout) {
        this.base_layout = base_layout;
        this.activity = activity;
    }

//    public int getBaseHeight() {
//        return this.base_height;
//    }

    public void resize_n() {
//        float action_bar_height = action_bar_height_in_pixels();
        float width_area  = base_layout.getWidth();  // 1080
        float height_area = base_layout.getHeight(); // 1752

        height_area -= (float)(getTitleBarHeight() + getStatusBarHeight());//1696

        float height_to_width_etalon_factor = height_virt / width_virt; //1.777777...
        float height_to_width_real_factor = height_area / width_area; //1.47

        float margin_px;
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) base_layout.getLayoutParams();


        if (height_to_width_etalon_factor < height_to_width_real_factor) {
            margin_px           = 0.5f * (height_area - width_area * height_to_width_etalon_factor);
            params.leftMargin   = 0;
            params.rightMargin  = 0;
            params.topMargin    = (int) margin_px;
            params.bottomMargin = (int) margin_px;

            this.height_margin = margin_px;
        } else {
            margin_px           = 0.5f * (width_area - height_area / height_to_width_etalon_factor);
            params.leftMargin   = (int) margin_px;
            params.rightMargin  = (int) margin_px;
            params.topMargin    = 0;
            params.bottomMargin = 0;

            this.width_margin = margin_px;
        }
    }
    public float get_height_margin() {return this.height_margin;}
    public float get_width_margin()  {return this.width_margin;}

    private int getStatusBarHeight() {
        Rect r = new Rect();
        Window w = activity.getWindow();
        w.getDecorView().getWindowVisibleDisplayFrame(r);
        return r.top;
    }

    private int getTitleBarHeight() {
        int viewTop = activity.getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();
        return (viewTop - getStatusBarHeight());
    }
    //0.092
    void resizeFirstRegularLogins(RelativeLayout base_layout, LinearLayout layout, Vector<Button> bt_vector, float factor) {
        float  total_height = (float)(base_layout.getHeight() - getTitleBarHeight() - getStatusBarHeight()); //770
        float  sub_height   = (float)layout.getHeight();      //500
        float  init_factor  = sub_height/total_height;

        float new_button_height  = total_height * factor;              //70.84
        float old_button_height  = (float)bt_vector.elementAt(0).getHeight(); //72

        for (int i = 0; i < bt_vector.size(); i ++)
            bt_vector.elementAt(i).setHeight((int)new_button_height);

        float incr_height_factor = (new_button_height - old_button_height)*bt_vector.size();

        sub_height += incr_height_factor;//420 - 3.48
        float x_pad = (total_height - sub_height)/2.0f;

        layout.setPadding(0, (int)x_pad, 0, (int)x_pad);

//        if (init_factor == 0.88776f)return;

    }
}
