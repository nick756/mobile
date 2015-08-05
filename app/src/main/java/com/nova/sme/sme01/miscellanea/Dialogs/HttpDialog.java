package com.nova.sme.sme01.miscellanea.Dialogs;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.nova.sme.sme01.FormResizing;
import com.nova.sme.sme01.R;
import com.nova.sme.sme01.miscellanea.ApplicationAttributes;
import com.nova.sme.sme01.miscellanea.Dialogs.MyDialog;
import com.nova.sme.sme01.miscellanea.FileManager;
import com.nova.sme.sme01.miscellanea.Vocabulary;

import java.util.Vector;

/*
 *********************************
 *                               *
 *  Set & save base url address  *
 *                               *
 *********************************
 */
public class HttpDialog extends MyDialog {
    private ApplicationAttributes attr;

    public HttpDialog(FormResizing FR, Vocabulary voc, RelativeLayout base_layout) {
        super(FR, voc, base_layout);
    }
    public void show() {
        final Dialog dialog = new Dialog(base_layout.getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.base_http);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0x88000000));

        ///
        dialog_layout = (RelativeLayout) dialog.findViewById(R.id.http_base);dialog_layout.setTag("dialog_background_color");
        views.add(dialog_layout);
        ///


        ViewGroup.LayoutParams params = dialog.getWindow().getAttributes();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();

        lp.width  = (int)((float)base_layout.getWidth()*0.9f);
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;


        Button submit = (Button) dialog.findViewById(R.id.submit_http);
        Button cancel = (Button) dialog.findViewById(R.id.cancel_http);

        // set theme
        Vector<Button> btns = new Vector<Button>();
        btns.add(submit);btns.add(cancel);
        attr = setDialogButtonsTheme(btns);

        EditText edit = (EditText) dialog.findViewById(R.id.http_id);
        edit.setText(attr.getBaseUrl().substring(7));//cutting prefix "http://"

        voc.change_caption(submit);
        voc.change_caption(cancel);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText edit = (EditText) dialog.findViewById(R.id.http_id);
                String   http = edit.getText().toString().trim();
                if (http.length() > 0) {
                    attr.setBaseUrl(http);
                    new FileManager(base_layout.getContext()).writeToFile("attributes.bin", attr);
                }
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
                ViewGroup.LayoutParams prms = submit.getLayoutParams();
                prms.height                 = height;

                prms = cancel.getLayoutParams();
                prms.height = height;
            }
        }
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        SetColors();
    }

}
