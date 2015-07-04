package com.nova.sme.sme01;

//import android.app.Activity;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;


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
        float action_bar_height = action_bar_height_in_pixels();


        float width_area  = base_layout.getWidth();  // 1080
        float height_area = base_layout.getHeight(); // 1752

        height_area -= action_bar_height;//1696

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

    private int action_bar_height_in_pixels() {
        int h = 0;
        int heightInPixels = 0;
        try {
            TypedValue tv = new TypedValue();
            if (activity.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
                h = TypedValue.complexToDimensionPixelSize(tv.data, activity.getResources().getDisplayMetrics());

            DisplayMetrics displaymetrics = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

            float d = displaymetrics.density;
            heightInPixels = (int) (h / d);
        } catch (Exception err) {
            heightInPixels = 0;
        }
        return heightInPixels;
    }

    public void resize_indirectly(TextView target, android.widget.RelativeLayout layout, float percent) {
        if (target == null || layout == null || percent == 0)
            return;

        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) layout.getLayoutParams();
        int width = params.width;

        params = (ViewGroup.MarginLayoutParams) target.getLayoutParams();
        width = (int)(((float)width)*percent);
        params.width = width;
    }
}
