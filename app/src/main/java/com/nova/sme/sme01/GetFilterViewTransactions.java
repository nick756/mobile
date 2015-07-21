package com.nova.sme.sme01;


import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.nova.sme.sme01.miscellanea.HttpRequestViewTransactions;
import com.nova.sme.sme01.miscellanea.SimpleCalendar;
import com.nova.sme.sme01.miscellanea.Vocabulary;

/*
 **********************************
 *                                 *
 *   Get date interval and send    *
 *   request to view transactions  *
 *                                 *
 ***********************************
 */

public class GetFilterViewTransactions {
    private Activity       activity;
    private Vocabulary     voc;
    private RelativeLayout base_layout;
    private String         http_request;// id
    private Spinner        year_from;
    private Spinner        month_from;
    private Spinner        day_from;
    private Spinner        year_till;
    private Spinner        month_till;
    private Spinner        day_till;
    private SimpleCalendar from_calendar;
    private SimpleCalendar till_calendar;
    private FormResizing   FR;



    public GetFilterViewTransactions(Activity activity, Vocabulary voc, RelativeLayout base_layout, FormResizing   FR, String http_request) {
        this.activity     = activity;
        this.voc          = voc;
        this.base_layout  = base_layout;
        this.FR           = FR;
        this.http_request = http_request;

        show();
    }

    private void show() {
        final Dialog dialog = new Dialog(base_layout.getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.from_till);

        year_from     = (Spinner)dialog.findViewById(R.id.year_spinner_from);
        month_from    = (Spinner)dialog.findViewById(R.id.month_spinner_from);
        day_from      = (Spinner)dialog.findViewById(R.id.day_spinner_from);
        from_calendar = new SimpleCalendar(activity, year_from, month_from, day_from);

        year_till     = (Spinner)dialog.findViewById(R.id.year_spinner_till);
        month_till    = (Spinner)dialog.findViewById(R.id.month_spinner_till);
        day_till      = (Spinner)dialog.findViewById(R.id.day_spinner_till);
        till_calendar = new SimpleCalendar(activity, year_till, month_till, day_till);

        ViewGroup.LayoutParams params = dialog.getWindow().getAttributes();

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();

        lp.width  = (int)((float)base_layout.getWidth()*0.9f);
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

//        FR.resizeCalendar(base_layout, (RelativeLayout) dialog.findViewById(R.id.from_calendar), year_from, month_from, day_from, 0.062f);
//        FR.resizeCalendar(base_layout, (RelativeLayout) dialog.findViewById(R.id.till_calendar), year_till, month_till, day_till, 0.062f);

        // make translation

        Button okButton = (Button) dialog.findViewById(R.id.ok_from_till_button);
        voc.change_caption(okButton);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String from = from_calendar.getDateFormatted();
                String till = till_calendar.getDateFormatted();

                http_request += "&dateFrom=" + from;
                http_request += "&dateTill=" + till;

                new HttpRequestViewTransactions(activity, voc, http_request);

                dialog.dismiss();
            }
        });

        dialog.show();

        dialog.getWindow().setAttributes(lp);
        resize(lp.width);

        int height = FR.getLogButtonHeight();
        if (height > 0) {
            ViewGroup.LayoutParams prms = okButton.getLayoutParams();
            prms.height = height;
        }
    }

    private void resize(float width) {              // width of stroke
        width -= converDpToPixels(6*2 + 5*2 + 5*2 + 6*2 + 2*2);// width of stroke
 //       width -= FR.get_width_margin()*2.0f;

        float  y = 4.0f;
        float  m = 6.0f;
        float  d = 3.0f;


        ViewGroup.LayoutParams params;

        params        = year_from.getLayoutParams();
        params.width  = (int)(width *y/(y + m + d));//303

        params        = month_from.getLayoutParams();
        params.width  = (int)(width *m/(y + m + d));//683

        params        = day_from.getLayoutParams();
        params.width  = (int)(width *d/(y + m + d));//151

        params        = year_till.getLayoutParams();
        params.width  = (int)(width *y/(y + m + d));//303

        params        = month_till.getLayoutParams();
        params.width  = (int)(width *m/(y + m + d));//683

        params        = day_till.getLayoutParams();
        params.width  = (int)(width *d/(y + m + d));//151

    }

    private float converDpToPixels(int dp) {
        return dp * activity.getResources().getDisplayMetrics().density;
    }

}
