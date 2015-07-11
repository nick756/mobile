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
    private Activity/*AppCompatActivity*/ activity;
    private android.widget.RelativeLayout base_layout;

 //   private int   base_height = 0;
    private float   width_virt     = 1080.0f;
    private float   height_virt    = 1696.0f;
    private float   width_margin   = 0.0f;
    private float   height_margin  = 0.0f;
    private int     real_width;

    public FormResizing(/*AppCompatActivity*/Activity activity, RelativeLayout base_layout) {
        this.base_layout = base_layout;
        this.activity = activity;
    }

    public void resize() {
        float width_area  = base_layout.getWidth();  // 1080
        float height_area = base_layout.getHeight(); // 1752

        this.real_width   = (int)width_area;

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

            this.real_width -=  params.leftMargin*2;
        }

    }
    public int getRealWidth() {return this.real_width;}
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
        int height  = (viewTop - getStatusBarHeight());
        return height;
    }
    //0.092
    void resizeFirstRegularLogins(RelativeLayout base_layout, Vector<Button> bt_vector, float factor) {
        int    viewTop            = activity.getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();
        float  total_height       = (float)(base_layout.getHeight() - viewTop);//getTitleBarHeight() - getStatusBarHeight()); //770
        float  new_button_height  = total_height * factor;              //70.84

        for (int i = 0; i < bt_vector.size(); i ++)
            bt_vector.elementAt(i).setHeight((int)new_button_height);
    }


}
