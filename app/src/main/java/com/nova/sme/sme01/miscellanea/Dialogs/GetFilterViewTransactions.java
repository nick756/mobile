package com.nova.sme.sme01.miscellanea.Dialogs;


import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.Spinner;

import com.nova.sme.sme01.FormResizing;
import com.nova.sme.sme01.R;
import com.nova.sme.sme01.miscellanea.ApplicationAttributes;
import com.nova.sme.sme01.miscellanea.FileManager;
import com.nova.sme.sme01.miscellanea.MyHttpRequest;
import com.nova.sme.sme01.miscellanea.SelectableOperationList;
import com.nova.sme.sme01.miscellanea.SimpleCalendar;
import com.nova.sme.sme01.miscellanea.Vocabulary;

import java.util.List;
import java.util.Vector;

import static java.sql.DriverManager.println;

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
    private int            dialog_width;
    private RelativeLayout dialog_layout;
    private ScrollView     sv;


    private int            selected_item = 0;
    private MyDialog       my_dialog;// = new MyDialog(FR, voc, base_layout);

    private SelectableOperationList sol;//(Dialog dialog, Vocabulary voc, RelativeLayout base_layout, FormResizing FR)



    public GetFilterViewTransactions(Activity activity, Vocabulary voc, RelativeLayout base_layout, FormResizing   FR, String http_request) {
        this.activity     = activity;
        this.voc          = voc;
        this.base_layout  = base_layout;
        this.FR           = FR;
        this.http_request = http_request;
        this.my_dialog    = new MyDialog(FR, voc, base_layout);

        show();
    }

    private void show() {
        final Dialog dialog = new Dialog(base_layout.getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.from_till);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0x88000000));

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
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;//(int) (((float) lp.width)*1.2f);//WindowManager.LayoutParams.WRAP_CONTENT;

        dialog_width = lp.width;

        // make translation
        // from_till_base_layout
        RelativeLayout layout = (RelativeLayout)dialog.findViewById(R.id.from_till_base_layout);
        voc.TranslateAll(layout);

        Button okButton = (Button) dialog.findViewById(R.id.ok_from_till_button);
        voc.change_caption(okButton);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if (sol.isEmpty()) {
                my_dialog.show(voc.getTranslatedString("Selected list is empty"), R.mipmap.ic_zero);
                return;
            }


            String from = from_calendar.getDateFormatted();
            String till = till_calendar.getDateFormatted();

            http_request += "&dateFrom=" + from;
            http_request += "&dateTill=" + till;

            new MyHttpRequest(FR, activity, base_layout, voc, http_request, "ListTransactions");
            sol.save();
            dialog.dismiss();
            }
        });

        CheckBox cb = (CheckBox) dialog.findViewById(R.id.seletcAllCB);
        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                sol.setAllCheckBoxes(isChecked);
            }
        });
        // set theme
        Vector<Button> btns = new Vector<Button>();
        btns.add(okButton);
        setDialogButtonsTheme(btns);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();

        dialog.getWindow().setAttributes(lp);
        resize(lp.width);

        int height = FR.getLogButtonHeight();
        if (height > 0) {
            ViewGroup.LayoutParams prms = okButton.getLayoutParams();
            prms.height                 = height;
        }
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        ApplicationAttributes attr = (ApplicationAttributes) new FileManager(base_layout.getContext()).readFromFile("attributes.bin");
        if (attr == null)
            attr = new ApplicationAttributes(base_layout.getContext());
//        if (attr == null) return;

        this.dialog_layout = (RelativeLayout) dialog.findViewById(R.id.from_till_base_layout);
        this.dialog_layout.setTag("dialog_background_color");
        attr.getColors().setColor(this.dialog_layout);

        sol = new SelectableOperationList(dialog, voc, base_layout, FR);

        sv = (ScrollView) dialog.findViewById(R.id.from_till_scrollview);
        new refresh(this.dialog_layout);
    }

    private void resize(float width) {              // width of stroke
        width -= convertDpToPixels(6 * 2 + 5 * 2 + 5 * 2 + 6 * 2 + 2 * 2);// width of stroke
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

    private float convertDpToPixels(int dp) {
        return dp * activity.getResources().getDisplayMetrics().density;
    }

    private ApplicationAttributes setDialogButtonsTheme(Vector<Button> buttons) {
        ApplicationAttributes attr = (ApplicationAttributes) new FileManager(base_layout.getContext()).readFromFile("attributes.bin");
        if (attr == null)
            attr = new ApplicationAttributes(base_layout.getContext());

        attr.setButtons(base_layout, buttons);
        return attr;
    }

    public class refresh {
        private RelativeLayout layout;

        public refresh(RelativeLayout layout) {
            this.layout = layout;

            layout.post(new Runnable() {
                public void run() {
                    ViewGroup.LayoutParams params = sv.getLayoutParams();
                    params.height = (int)((float)dialog_width*0.8f);
                }
            });
        }
    }


}
