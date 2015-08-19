package com.nova.sme.sme01.miscellanea.Dialogs;



import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nova.sme.sme01.FormResizing;
import com.nova.sme.sme01.R;
import com.nova.sme.sme01.miscellanea.ApplicationAttributes;
import com.nova.sme.sme01.miscellanea.FileManager;
import com.nova.sme.sme01.miscellanea.MyColors;
import com.nova.sme.sme01.miscellanea.MyHttpRequest;
import com.nova.sme.sme01.miscellanea.Vocabulary;
import com.nova.sme.sme01.xml_reader_classes.Operation;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Vector;

/*
 ********************************
 *                              *
 *   Confirmation transaction   *
 *   before sending request     *
 *   to server                  *
 *                              *
 ********************************
 */

public class ConfirmTransaction {
    private Activity       activity;
    private Vocabulary     voc;
    private FormResizing FR;
    private RelativeLayout base_layout;
    private String         http_request;

    private Operation opearion;
    private String    date;
    private String    descr;
    private String    amount;

    private Button    okButton;
    private Button    cancelButton;
    private Button    logout_button;


    public ConfirmTransaction(Activity activity, Vocabulary voc, FormResizing   FR, RelativeLayout base_layout, String http_request, Operation s_opearion, String s_date, String s_descr, String s_sum, Button logout_button) {
        this.activity      = activity;
        this.voc           = voc;
        this.FR            = FR;
        this.base_layout   = base_layout;
        this.http_request  = http_request;
        this.opearion      = s_opearion;
        this.date          = s_date;
        this.descr         = s_descr;
        this.amount        = s_sum;
        this.logout_button = logout_button;

    }
    void send_request() {
        new MyHttpRequest(FR, activity, base_layout, voc, http_request, "AddTransaction");
    }
    public void show() {
        final Dialog dialog = new Dialog(base_layout.getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.before_transaction);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0x88000000));

        ViewGroup.LayoutParams params = dialog.getWindow().getAttributes();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.width  = (int)((float)base_layout.getWidth()*0.9f);
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;


        TextView text = (TextView) dialog.findViewById(R.id.date_transaction_id);
        text.setText(this.date);

        //----------------
        text             = (TextView) dialog.findViewById(R.id.amount_transaction_id);
        boolean      err = false;
        DecimalFormat df = new DecimalFormat("###,###,###.00");
        try {
            double  val   = Double.parseDouble(this.amount);
            String money  = df.format(val);
            text.setText(money);
        } catch(Exception e) {
            err = true;
        }
        if (err)
            text.setText(this.amount);
        //----------------

        text = (TextView) dialog.findViewById(R.id.before_description);//before_operation_name
        text.setText(this.descr);

        text = (TextView) dialog.findViewById(R.id.before_operation_name);
        text.setText(this.opearion.getName());

        ImageView img = (ImageView)dialog.findViewById(R.id.before_in_out);
        if (opearion.getInbound().equals("true"))
            img.setImageResource(R.mipmap.ic_in_bound);
         else
            img.setImageResource(R.mipmap.ic_out_bound);


        cancelButton = (Button) dialog.findViewById(R.id.cancel_button_trans_id);
        voc.change_caption(cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        okButton = (Button) dialog.findViewById(R.id.ok_transaction);
        voc.change_caption(okButton);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            send_request();
            dialog.dismiss();
            }
        });

        // set theme
        Vector<Button> btns = new Vector<Button>();
        btns.add(okButton);btns.add(cancelButton);
        setDialogButtonsTheme(btns);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();

        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        ApplicationAttributes attr = (ApplicationAttributes) new FileManager(base_layout.getContext()).readFromFile("attributes.bin");
        if (attr == null)
            attr = new ApplicationAttributes(activity);

        Vector<View> views = new Vector<View>();

        RelativeLayout rl = (RelativeLayout) dialog.findViewById(R.id.base_layout_before_transaction_id);
        rl.setTag("dialog_background_color");
        views.add(rl);

        TextView tv1        = (TextView)       dialog.findViewById(R.id.date_transaction_id);               tv1.setTag("dialog_background_color");
        TextView tv2        = (TextView)       dialog.findViewById(R.id.amount_transaction_id);             tv2.setTag("dialog_background_color");
        TextView tv3        = (TextView)       dialog.findViewById(R.id.before_description);                tv3.setTag("dialog_background_color");
        RelativeLayout rll  = (RelativeLayout) dialog.findViewById(R.id.relativeLayout);                    rll.setTag("dialog_background_color");
        RelativeLayout brll = (RelativeLayout) dialog.findViewById(R.id.base_layout_before_transaction_id); brll.setTag("dialog_background_color");

        views.add(tv1);
        views.add(tv2);
        views.add(tv3);
        views.add(rll);
        views.add(brll);

        attr.getColors().setColors(views);

        setButtonsSize();

    }
    private void setButtonsSize() {
        if (logout_button == null) return;

        int height = logout_button.getHeight();
        if (height > 0) {
            ViewGroup.LayoutParams prms = okButton.getLayoutParams();
            prms.height                 = height;
            prms.width                  = ViewGroup.LayoutParams.WRAP_CONTENT;//###

            prms        = cancelButton.getLayoutParams();
            prms.height = height;
            prms.width  = ViewGroup.LayoutParams.WRAP_CONTENT;//###
        }
    }

    private ApplicationAttributes setDialogButtonsTheme(Vector<Button> buttons) {
        FileManager FM = new FileManager(base_layout.getContext());
        ApplicationAttributes attr = (ApplicationAttributes) FM.readFromFile("attributes.bin");
        if (attr == null)
            attr = new ApplicationAttributes(base_layout.getContext());

        attr.setButtons(base_layout, buttons);
        FM.writeToFile("attributes.bin", attr);
        return attr;
    }

}
