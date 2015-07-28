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
    private List<Button> buttons           = new ArrayList<Button>();

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

        LinearLayout ll = (LinearLayout)dialog.findViewById(R.id.themes_base_layout);
        LinearLayout inner;
        View         view;
        String       className, err;
        RadioButton  rb;
        Button       btn;

        if (ll != null) {
            for (int i = 0; i < ll.getChildCount(); i ++) {
                inner = (LinearLayout)ll.getChildAt(i);
                for (int j = 0; j < inner.getChildCount(); j ++) {
                    view      = inner.getChildAt(j);
                    className = view.getClass().getName().toString().toUpperCase();
                    if (className.indexOf("RADIOBUTTON") != -1) {
                        radioButtons.add((RadioButton) view);
                    } else {
                        buttons.add((Button)view);
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
                    resetRadiobuttons(rbtn);
                }
            });

            btn = buttons.get(i);
            btn.setTag(rb);

            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Button      bt;
                    RadioButton rb;

                    bt = (Button) v;
                    rb = (RadioButton) v.getTag();
                    rb.setChecked(true);
                    resetRadiobuttons(rb);
                 }
            });

        }
         dialog.show();


    }
    void resetRadiobuttons(RadioButton rb) {
        RadioButton current;
        for (int j = 0; j < radioButtons.size(); j ++) {
            current = radioButtons.get(j);
            if (rb == current) continue;
            if (current.isChecked())
                current.setChecked(false);
        }

    }

}
