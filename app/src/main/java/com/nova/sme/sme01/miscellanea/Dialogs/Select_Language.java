package com.nova.sme.sme01.miscellanea.Dialogs;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nova.sme.sme01.R;
import com.nova.sme.sme01.miscellanea.ApplicationAttributes;
import com.nova.sme.sme01.miscellanea.FileManager;
import com.nova.sme.sme01.miscellanea.Parameters;
import com.nova.sme.sme01.miscellanea.Vocabulary;

import java.util.Vector;

/*
 ***************************************
 *                                     *
 *  Dialog for changing the language   *
 *                                     *
 ***************************************
 */
public class Select_Language {

    private RelativeLayout base_layout;
    private Vocabulary voc;
    private FileManager FM;
    private Parameters params;
    private Button         logout_button;
    private String         params_file_name;


    public Select_Language(RelativeLayout base_layout, Vocabulary voc, FileManager FM, Parameters params, Button  logout_button, String params_file_name) {
        this.base_layout      = base_layout;
        this.voc              = voc;
        this.FM               = FM;
        this.params           = params;
        this.logout_button    = logout_button;
        this.params_file_name = params_file_name;

        show();
    }
    public Select_Language() {

    }

    private void setButtonHeight(Button button) {
        if (logout_button == null) return;
        ViewGroup.LayoutParams params;// = logout_button.getLayoutParams();
//        int height                    = params.height;

        params = button.getLayoutParams();
        params.height = logout_button.getHeight();//height;
    }


    private void show() {
        final Dialog dialog = new Dialog(base_layout.getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_select__language);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0x88000000));

        final TextView text     = (TextView)    dialog.findViewById(R.id.select_language);
        final RadioButton en_rb = (RadioButton) dialog.findViewById(R.id.EngRB);
        final RadioButton my_rb = (RadioButton) dialog.findViewById(R.id.MalayRB);

        voc.change_caption(text);
        voc.change_caption(en_rb);
        voc.change_caption(my_rb);

        Button dialogButton = (Button) dialog.findViewById(R.id.ok_select_language);
        voc.change_caption(dialogButton);

        if (voc.getLanguage().equals("EN"))
            en_rb.setChecked(true);
        else
            my_rb.setChecked(true);

        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (en_rb.isChecked())
                    voc.setLanguage("EN");
                else
                    voc.setLanguage("MY");

                writeParameters();
                voc.TranslateAll(base_layout);
                if (logout_button != null)
                    voc.change_caption(logout_button);
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        ApplicationAttributes attr = (ApplicationAttributes) new FileManager(base_layout.getContext()).readFromFile("attributes.bin");
        if (attr == null) return;

        Vector<View> views = new Vector<View>();
        TextView txt = (TextView)dialog.findViewById(R.id.select_language);txt.setTag("text_background_color");

        LinearLayout ll = (LinearLayout) dialog.findViewById(R.id.linearLayout); ll.setTag("dialog_background_color");

        LinearLayout l_l = (LinearLayout) dialog.findViewById(R.id.sel_lang_b);l_l.setTag("dialog_background_color");

        views.add(txt);
        views.add(ll);
        views.add(l_l);

        attr.getColors().setColors(views);

        Vector<Button> btns = new Vector<Button>();btns.add(dialogButton);
        attr.setButtons(base_layout, btns);

        setButtonHeight(dialogButton);
    }

    private void  writeParameters() {
        this.params.setLangauge(voc.getLanguage());
        FM.writeToFile(params_file_name, this.params);
    }


}
