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
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.nova.sme.sme01.FormResizing;
import com.nova.sme.sme01.R;
import com.nova.sme.sme01.miscellanea.ApplicationAttributes;
import com.nova.sme.sme01.miscellanea.Dialogs.MyDialog;
import com.nova.sme.sme01.miscellanea.TextResizing;
import com.nova.sme.sme01.miscellanea.Vocabulary;

import java.util.List;
import java.util.Vector;

import static java.sql.DriverManager.println;

/*
 ******************
 *                *
 *  About Dialog  *
 *                *
 ******************
 */

public class AboutDialog extends MyDialog {
    private ApplicationAttributes attr;
    private TextResizing          textFit;
    private String                sample = "SIFAR refers to Simplified Financial";
    private TextView              aboutText;

    public AboutDialog(FormResizing FR, Vocabulary voc, RelativeLayout base_layout, Button logout) {
        super(FR, voc, base_layout);

        this.textFit   = new TextResizing(base_layout.getContext());
    }

    public void show() {
        final Dialog dialog = new Dialog(base_layout.getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.about);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0x88000000));


        ViewGroup.LayoutParams params = dialog.getWindow().getAttributes();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();

        lp.width  = (int)((float)base_layout.getWidth()*0.9f);
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        Button about = (Button) dialog.findViewById(R.id.about_button);

        // COLORS //
        dialog_layout = (RelativeLayout) dialog.findViewById(R.id.about_base);dialog_layout.setTag("dialog_background_color");
        views.add(dialog_layout);
        // COLORS //

        // set theme
        Vector<Button> btns = new Vector<Button>();
        btns.add(about);
        attr = setDialogButtonsTheme(btns);
        voc.change_caption(about);

        dialog.show();
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


        ViewGroup.LayoutParams prms;
        ScrollView sv = (ScrollView) dialog.findViewById(R.id.about_scroll);
        prms = sv.getLayoutParams();
        prms.height = (int)((float)lp.width*1.2f);

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {dialog.dismiss();}
        });


        int height;
        if (FR != null) {
            height = FR.getLogButtonHeight();
            if (height > 0) {
                prms = about.getLayoutParams();
                prms.height = height;
            }
        }
        SetColors();

        aboutText = (TextView) dialog.findViewById(R.id.about_text);
        new FitText(aboutText);
    }

    public class FitText {
        private TextView tv;

        public FitText(TextView tv) {
            this.tv      =  tv;

            tv.post(new Runnable() {
                public void run() {
                float textsize = textFit.getSizeWidth(aboutText, sample, 1.2f, base_layout.getWidth());
                aboutText.setTextSize(textsize);
                }
            });
        }

    }

}
