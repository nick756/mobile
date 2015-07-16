package com.nova.sme.sme01;



import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nova.sme.sme01.miscellanea.HttpRequestTransaction;
import com.nova.sme.sme01.miscellanea.Vocabulary;
import com.nova.sme.sme01.xml_reader_classes.Operation;

/*
 ********************************
 *                              *
 *   Confirmation transaction   *
 *   Before sending request     *
 *   to server                  *
 *                              *
 ********************************
 */
public class ConfirmTransaction {
    private Activity       activity;
    private Vocabulary     voc;
    private RelativeLayout base_layout;
    private String         http_request;

    private Operation opearion;
    private String    date;
    private String    descr;
    private String    amount;

    public ConfirmTransaction(Activity activity, Vocabulary voc, RelativeLayout base_layout, String http_request, Operation s_opearion, String s_date, String s_descr, String s_sum) {
        this.activity     = activity;
        this.voc          = voc;
        this.base_layout  = base_layout;
        this.http_request = http_request;

        this.opearion = s_opearion;
        this.date     = s_date;
        this.descr    = s_descr;
        this.amount   = s_sum;
    }
    void send_request() {
        new HttpRequestTransaction(activity, base_layout, voc, http_request);
    }
    public void show() {
        final Dialog dialog = new Dialog(base_layout.getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.before_transaction);

        TextView text = (TextView) dialog.findViewById(R.id.date_transaction_id);
        text.setText(this.date);

        text = (TextView) dialog.findViewById(R.id.amount_transaction_id);
        text.setText(this.amount);

        text = (TextView) dialog.findViewById(R.id.before_description);//before_operation_name
        text.setText(this.descr);

        text = (TextView) dialog.findViewById(R.id.before_operation_name);
        text.setText(this.opearion.getName());

        ImageView img = (ImageView)dialog.findViewById(R.id.before_in_out);
        if (opearion.getInbound().equals("true")) {
            img.setImageResource(R.mipmap.ic_in_bound);
        } else {
            img.setImageResource(R.mipmap.ic_out_bound);
        }

        Button cancelButton = (Button) dialog.findViewById(R.id.cancel_button_trans_id);
        voc.change_caption(cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        Button okButton = (Button) dialog.findViewById(R.id.ok_transaction);
        voc.change_caption(okButton);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send_request();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

}