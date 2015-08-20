package com.nova.sme.sme01.miscellanea.Dialogs;

/*
 ******************************
 *                            *
 *  Send photo to the server  *
 *                            *
 ******************************
 */

import android.app.Dialog;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.nova.sme.sme01.FormResizing;
import com.nova.sme.sme01.R;
import com.nova.sme.sme01.miscellanea.ApplicationAttributes;
import com.nova.sme.sme01.miscellanea.TextResizing;
import com.nova.sme.sme01.miscellanea.Vocabulary;

import java.util.Vector;

public class SendPhotoDialog extends MyDialog {
    private ApplicationAttributes attr;
    private Button                logout_button;
    private String                image_path;


    public SendPhotoDialog(FormResizing FR, Vocabulary voc, RelativeLayout base_layout, Button logout_button, String image_path) {
        super(FR, voc, base_layout);
        this.logout_button = logout_button;
        this.image_path    = image_path;
    }
    private void setButtonHeight(Button button) {
        if (logout_button == null) return;
        ViewGroup.LayoutParams params;

        params = button.getLayoutParams();
        params.height = logout_button.getHeight();//height;
    }

    public void show() {
        final Dialog dialog = new Dialog(base_layout.getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.photo);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0x88000000));


        ViewGroup.LayoutParams params = dialog.getWindow().getAttributes();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();

        lp.width  = (int)((float)base_layout.getWidth()*0.9f);
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        Button okButton = (Button) dialog.findViewById(R.id.send_photo);
        Button cancelButton = (Button) dialog.findViewById(R.id.cancel_photo);


        // COLORS //
        dialog_layout = (RelativeLayout) dialog.findViewById(R.id.photo_base);dialog_layout.setTag("dialog_background_color");
        views.add(dialog_layout);

        LinearLayout ll = (LinearLayout) dialog.findViewById(R.id.back_buttons_photo);ll.setTag("dialog_background_color");
        views.add(ll);
        // COLORS //

        // set theme
        Vector<Button> btns = new Vector<Button>();
        btns.add(okButton);btns.add(cancelButton);
        attr = setDialogButtonsTheme(btns);
        voc.change_captions(btns);

        dialog.show();
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


//        ViewGroup.LayoutParams prms;

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ///
                dialog.dismiss();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        ImageView img = (ImageView)dialog.findViewById(R.id.photo_id);
        img.setImageBitmap(BitmapFactory.decodeFile(image_path));

        SetColors();

        setButtonHeight(okButton);
        setButtonHeight(cancelButton);
    }

}
