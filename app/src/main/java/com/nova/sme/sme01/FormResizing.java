package com.nova.sme.sme01;

//import android.app.Activity;
import android.app.Activity;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
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
    private Activity activity;
    private android.widget.RelativeLayout base_layout;

    private float   width_virt     = 1080.0f;
    private float   height_virt    = 1696.0f;
    private float   width_margin   = 0.0f;
    private float   height_margin  = 0.0f;
    private int     real_width;
    private int     real_height;
    private int     log_button_height = 0;
    private Button  logout_button = null;

    public FormResizing(/*AppCompatActivity*/Activity activity, RelativeLayout base_layout) {
        this.base_layout = base_layout;
        this.activity = activity;
    }

    public Button getLogoutButton(){return  logout_button;}
    public void   setLogoutButton(Button btn) {logout_button = btn;}
    public void resize() {
        float width_area  = base_layout.getWidth();  // 1080
        float height_area = base_layout.getHeight(); // 1752

        height_area -= (float)(getTitleBarHeight() + getStatusBarHeight());//1696

        this.real_width   = (int)width_area;
        this.real_height  = (int)height_area;

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

            this.real_height -= params.topMargin*2;
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
    public int getRealHeight() {return this.real_height;}
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
    void resizeRegularLogins(RelativeLayout base_layout, Vector<Button> bt_vector, Button logout_button, float factor) {
        int    viewTop            = activity.getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();
        float  total_height       = (float)(base_layout.getHeight() - viewTop);//getTitleBarHeight() - getStatusBarHeight()); //770
        float  new_button_height  = total_height * factor;              //70.84

        Button button;
        ViewGroup.LayoutParams params;
        if (bt_vector != null) {
            for (int i = 0; i < bt_vector.size(); i++) {
                button = bt_vector.elementAt(i);
                params = button.getLayoutParams();
                params.height = (int) new_button_height;
            }
        }
        if (logout_button != null) {
            params             = logout_button.getLayoutParams();
            params.height      = (int) new_button_height;
            log_button_height  = params.height;
            this.logout_button = logout_button;
        }
    }
    void resizeLoginButton(RelativeLayout base_layout, Button logout_button, float factor) {
        int    viewTop            = activity.getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();
        float  total_height       = (float)(base_layout.getHeight() - viewTop);//getTitleBarHeight() - getStatusBarHeight()); //770
        float  new_button_height  = total_height * factor;              //70.84

        ViewGroup.LayoutParams params;
        if (logout_button != null) {
            params = logout_button.getLayoutParams();
            params.height = (int) new_button_height;

            log_button_height = params.height;
        }
        this.logout_button = logout_button;
    }
    public int  getLogButtonHeight(){return log_button_height;}
    public void setLogButtonHeight(int height){log_button_height = height;}

    void resizeOperationListTemplate(int id, float factor) {
        int    viewTop      = activity.getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();
        float  total_height = (float)(base_layout.getHeight() - viewTop);
        float  new_height   = total_height * factor;

        LinearLayout           base = (LinearLayout) activity.getWindow().findViewById(id);
        LinearLayout           element;
        RelativeLayout         sub_element, inner;
        ImageView              ib;
        View                   view;
        ViewGroup.LayoutParams params;
        String                 className;

        if (base != null) {
            for (int i = 0; i <  base.getChildCount(); i ++) {
                element       = (LinearLayout) base.getChildAt(i);
                for (int j = 0; j < element.getChildCount(); j ++) {
                    sub_element   = (RelativeLayout)element.getChildAt(j);
//                    params        = sub_element.getLayoutParams();
//                    params.height = (int) new_height;

                    for (int k = 0; k < sub_element.getChildCount(); k ++) {
                        view      = sub_element.getChildAt(k);
                        className = view.getClass().getSimpleName().toUpperCase().trim();
                        if (className.indexOf(new String("Image").toUpperCase()) != -1) {
                            ib            = (ImageView) view;
                            params        = ib.getLayoutParams();
                            params.height = (int) new_height;
                            params.width  = (int) new_height;
                        }
                    }

                }
            }
        }
    }
    public void resizeCalendar(RelativeLayout base_layout, RelativeLayout base_calendar_layout, Spinner years, Spinner months, Spinner days, float factor) {
        int    viewTop      = activity.getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();
        float  total_height = (float)(base_layout.getHeight() - viewTop);
        float  new_height   = total_height * factor;

        float  y = 4.0f;
        float  m = 7.0f;
        float  d = 3.0f;


        float  width  = (float)base_calendar_layout.getWidth();//
        width -= converDpToPixels(6*2);
        width -= this.width_margin*2;

        ViewGroup.LayoutParams params;

        params        = years.getLayoutParams();
        params.height = (int)new_height;
        params.width  = (int)(width *y/(y + m + d));//303

        params        = months.getLayoutParams();
        params.height = (int)new_height;
        params.width  = (int)(width *m/(y + m + d));//683

        params        = days.getLayoutParams();
        params.height = (int)new_height;
        params.width  = (int)(width *d/(y + m + d));//151

    }
    public void resizeAmounts(RelativeLayout base_layout, RelativeLayout base_amount_layout, EditText amount, EditText cents, Button button, float factor) {
        int    viewTop      = activity.getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();
        float  total_height = (float)(base_layout.getHeight() - viewTop);
        float  new_height   = total_height * factor;

        float  width  = (float)base_amount_layout.getWidth() - this.width_margin*2 - converDpToPixels(6*2);

        ViewGroup.LayoutParams params;

        if (button != null) {
            if (logout_button != null) {
                int height = logout_button.getHeight();

                if (height == 0) {
                    logout_button.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    height = logout_button.getMeasuredHeight();
                }

                if (log_button_height > 0)
                    height = Math.min(height, log_button_height); // I know, I know.... hoodoo

                params        = button.getLayoutParams();
                params.height = height;
            }
        }


        params = amount.getLayoutParams();
        params.width  = (int) (width*0.35f);
        params.height = (int) new_height;

        params = cents.getLayoutParams();
        params.width = (int) (width*0.18f);
        params.height = (int) new_height;

    }

    private float converDpToPixels(int dp) {
        return dp * activity.getResources().getDisplayMetrics().density;
    }


}
