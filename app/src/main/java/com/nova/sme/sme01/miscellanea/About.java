package com.nova.sme.sme01.miscellanea;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.nova.sme.sme01.FormResizing;
import com.nova.sme.sme01.R;

import java.util.Vector;

/*
 *****************
 *               *
 *  About Dialog *
 *               *
 *****************
 */

public class About extends MyDialog{
    private ApplicationAttributes attr;

    public About(FormResizing FR, Vocabulary voc, RelativeLayout base_layout, Button logout) {
        super(FR, voc, base_layout);
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

        ScrollView sv = (ScrollView) dialog.findViewById(R.id.about_scroll);
        ViewGroup.LayoutParams prms = sv.getLayoutParams();
//        prms.height = (int)((float)lp.width*1.2f);

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

    }
}
