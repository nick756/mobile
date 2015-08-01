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

import java.util.Vector;

/*
 **************************************************
 *                                                *
 *  Simple dialog to show result of something     *
 *                                                *
 **************************************************
 */


public class MyDialog {
    protected Vocabulary     voc;
    protected RelativeLayout base_layout;
    protected FormResizing   FR;

    protected TextView       text_message  = null;
    protected RelativeLayout dialog_layout = null;
    protected Vector<View>   views = new Vector<View>();

    public MyDialog(FormResizing FR, Vocabulary voc, RelativeLayout base_layout) {
        this.voc         = voc;
        this.base_layout = base_layout;
        this.FR          = FR;

    }
    public void show(String message) {
        final Dialog dialog = new Dialog(base_layout.getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.login_failed_layout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0x88000000));

        ///
        text_message  = (TextView) dialog.findViewById(R.id.dialog_text);text_message.setTag("text_background_color");
        dialog_layout = (RelativeLayout) dialog.findViewById(R.id.base_failed_layout);dialog_layout.setTag("dialog_background_color");
        views.add(text_message);
        views.add(dialog_layout);
        ///

        ViewGroup.LayoutParams params = dialog.getWindow().getAttributes();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();

        lp.width  = (int)((float)base_layout.getWidth()*0.9f);
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        TextView text = (TextView) dialog.findViewById(R.id.dialog_text);
        text.setText(message);

        Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);

        // set theme
        Vector<Button> btns = new Vector<Button>();
        btns.add(dialogButton);
        setDialogButtonsTheme(btns);

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
        SetColors();
        //custom_dialog_icon
    }

    public void show(String message, int id) {
        final Dialog dialog = new Dialog(base_layout.getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.login_failed_layout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0x88000000));

        ///
        text_message  = (TextView) dialog.findViewById(R.id.dialog_text);text_message.setTag("text_background_color");
        dialog_layout = (RelativeLayout) dialog.findViewById(R.id.base_failed_layout);dialog_layout.setTag("dialog_background_color");
        views.add(text_message);
        views.add(dialog_layout);
        ///


        ViewGroup.LayoutParams params = dialog.getWindow().getAttributes();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();

        lp.width  = (int)((float)base_layout.getWidth()*0.9f);
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;


        TextView text = (TextView) dialog.findViewById(R.id.dialog_text);
        text.setText(message);

        Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);

        // set theme
        Vector<Button> btns = new Vector<Button>();
        btns.add(dialogButton);
        setDialogButtonsTheme(btns);


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
        SetColors();
    }

     protected ApplicationAttributes setDialogButtonsTheme(Vector<Button> buttons) {
        ApplicationAttributes attr = (ApplicationAttributes) new FileManager(base_layout.getContext()).readFromFile("attributes.bin");
        if (attr == null)
            attr = new ApplicationAttributes();


        attr.setButtons(base_layout, buttons);
/*
        MyColors colors = attr.getColors();
        Vector<View> views = new Vector<View>();
        views.add(text_message);
        views.add(dialog_layout);
*/
        return attr;
     }
     protected void SetColors() {
         ApplicationAttributes attr = (ApplicationAttributes) new FileManager(base_layout.getContext()).readFromFile("attributes.bin");
         if (attr == null) return;

         MyColors colors = attr.getColors();
         colors.setColors(views);
     }

}
