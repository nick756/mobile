package com.nova.sme.sme01.miscellanea.Dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.Html;
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
    private Button                logout_button;

    public AboutDialog(FormResizing FR, Vocabulary voc, RelativeLayout base_layout, Button logout_button) {
        super(FR, voc, base_layout);

        this.textFit       = new TextResizing(base_layout.getContext());
        this.logout_button = logout_button;
    }
    private void setButtonHeight(Button button) {
        if (logout_button == null) return;
        ViewGroup.LayoutParams params = logout_button.getLayoutParams();
        int height                    = params.height;

        params = button.getLayoutParams();
        params.height = height;
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
            public void onClick(View v) {
                dialog.dismiss();
            }
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

        String html = "";
        html = "";
        html += "<html>";
        html += "<body style='margin:0 auto; width:100%; text-align:center'>";

        html += "SIFAR refers to <b>Simplified Financial and Recordkeeping</b> system. It is one of the modules in the BinaPavo Data Intelligence suite of software and system.";
        html += "SIFAR is developed based on years of applied research by our data and social scientists in the field of entrepreneurship, innovation, family business, financial management and strategy. The R&D was driven to find solutions for the pain faced by SMEs and lending agencies.";
        html += "Our scientists found that SMEs in general, lack financial management skills and accounting knowledge to manage their business finance. They also lack assistance when problems arise. Existing accounting software are too technical for them.  Lending agencies on the other hand, lack monitoring tools to assess SMEs' performance.";
        html += "In today’s world, even micro enterprises and small businesses are using smart phones for business purpose. SIFAR’s mobile application allows users to enter data with their Android smart phones wherever they are, 24/7. In addition, SIFAR does not only capture data and information but is designed to enable data analytics with monitoring system and diagnostic tools for monitoring and identifying SMEs' progress and problems periodically and automatically.";
        html += "The interface display daily cash flow transactions but in the background the transactions are captured and integrated into an accounting system with automated business analytics tools to assess the SMEs with flagging ability when problems are detected.";

        html += "</div>";
        html += "</body>";
        html += "</html>";

        TextView tv = (TextView) dialog.findViewById(R.id.about_text);
        tv.setText(Html.fromHtml(html));


        setButtonHeight(about);
    }

    public class FitText {
        private TextView tv;

        public FitText(TextView tv) {
            this.tv =  tv;

            tv.post(new Runnable() {
                public void run() {
                float textsize = textFit.getSizeWidth(aboutText, sample, 1.2f, base_layout.getWidth());
                aboutText.setTextSize(textsize);
                }
            });
        }

    }

}
