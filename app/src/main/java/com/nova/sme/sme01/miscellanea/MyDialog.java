package com.nova.sme.sme01.miscellanea;


import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nova.sme.sme01.FormResizing;
import com.nova.sme.sme01.R;

/*
 **************************************************
 *                                                *
 *  Simple dialog to show result of something     *
 *                                                *
 **************************************************
 */


public class MyDialog {
    private Vocabulary     voc;
    private RelativeLayout base_layout;
    private FormResizing   FR;

    public MyDialog(FormResizing FR, Vocabulary voc, RelativeLayout base_layout) {
        this.voc         = voc;
        this.base_layout = base_layout;
        this.FR          = FR;
    }
    public void show(String message) {
        final Dialog dialog = new Dialog(base_layout.getContext());

//        final Dialog dialog = new Dialog(base_layout.getContext(), android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.login_failed_layout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0x88000000));//0x88000000


 //       dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        //base_failed_layout
  //      LinearLayout rl = (LinearLayout) dialog.findViewById(R.id.base_failed_layout);
  //      rl.setBackground(new ColorDrawable(0));

        ViewGroup.LayoutParams params = dialog.getWindow().getAttributes();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();

        lp.width  = (int)((float)base_layout.getWidth()*0.9f);
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        TextView text = (TextView) dialog.findViewById(R.id.dialog_text);
        text.setText(message);

        Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);

        voc.change_caption(text);
        voc.change_caption(dialogButton);

        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();

        dialog.getWindow().setAttributes(lp);

        int height;
        if (FR != null) {
            height = FR.getLogButtonHeight();
            if (height > 0) {
                ViewGroup.LayoutParams prms = dialogButton.getLayoutParams();
                prms.height                 = height;
            }
        }
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        //custom_dialog_icon
    }
    public void show(String message, int id) {
        final Dialog dialog = new Dialog(base_layout.getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.login_failed_layout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0x88000000));

        ViewGroup.LayoutParams params = dialog.getWindow().getAttributes();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();

        lp.width  = (int)((float)base_layout.getWidth()*0.9f);
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;


        TextView text = (TextView) dialog.findViewById(R.id.dialog_text);
        text.setText(message);

        Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);

        voc.change_caption(text);
        voc.change_caption(dialogButton);

        ImageView img = (ImageView)dialog.findViewById(R.id.custom_dialog_icon);
        img.setImageResource(id);

        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();

        dialog.getWindow().setAttributes(lp);

        int height;
        if (FR != null) {
            height = FR.getLogButtonHeight();
            if (height > 0) {
                ViewGroup.LayoutParams prms = dialogButton.getLayoutParams();
                prms.height                 = height;
            }
        }

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    }

    private void changeIcon(int id) {
 //       ImageView img =

    }

}
