package com.nova.sme.sme01.miscellanea;

import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.nova.sme.sme01.R;

import java.util.ArrayList;
import java.util.List;

/*
 ********************************
 *                              *
 *  Dialog for selecting themes *
 *  (so far buttons only        *
 *                              *
 ********************************
 */
public class ThemesDialog {
    private RelativeLayout base_layout;
    private Vocabulary     voc;
    private FileManager    FM;
    private List<RadioButton> radioButtons = new ArrayList<RadioButton>();

    public ThemesDialog() {

    }
    public ThemesDialog(RelativeLayout base_layout, Vocabulary voc, FileManager FM) {
        this.base_layout = base_layout;
        this.voc         = voc;
        this.FM          = FM;

        show();
    }
    private void show() {
        final Dialog dialog = new Dialog(base_layout.getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.buttons);

        Button OkButton     = (Button) dialog.findViewById(R.id.ok_button);
        Button CancelButton = (Button) dialog.findViewById(R.id.cancel_button);

        OkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        CancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

//        RadioGroup   rg = new RadioGroup(dialog.getContext());
//        List<RadioButton> radioButtons = new ArrayList<RadioButton>();
        LinearLayout ll = (LinearLayout)dialog.findViewById(R.id.themes_base_layout);
        LinearLayout inner;
        View         view;
        String       className, err;
        RadioButton  rb;

        if (ll != null) {
            for (int i = 0; i < ll.getChildCount(); i ++) {
                inner = (LinearLayout)ll.getChildAt(i);
                for (int j = 0; j < inner.getChildCount(); j ++) {
                    view      = inner.getChildAt(j);
                    className = view.getClass().getName().toString().toUpperCase();
                    if (className.indexOf("RADIOBUTTON") != -1) {
                        radioButtons.add((RadioButton)view);
                    }
                }
            }
        }
        for (int i = 0; i < radioButtons.size(); i ++) {
            rb = radioButtons.get(i);
            rb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RadioButton rbtn;
                    rbtn = (RadioButton) v;
                    for (int j = 0; j < radioButtons.size(); j ++) {
                        if (rbtn == radioButtons.get(j)) continue;
                        radioButtons.get(j).setChecked(false);
                    }
                }
            });

        }

        dialog.show();


    }

}
