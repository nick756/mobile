package com.nova.sme.sme01.miscellanea;

import android.app.Dialog;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nova.sme.sme01.R;

/*
 ***************************************
 *                                     *
 *  Dialog for changing the language   *
 *                                     *
 ***************************************
 */
public class Select_Language {

    private RelativeLayout base_layout;
    private Vocabulary     voc;
    private FileManager    FM;
    private Parameters     params;
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

    private void show() {
        final Dialog dialog = new Dialog(base_layout.getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_select__language);

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

    }

    private void  writeParameters() {
        this.params.setLangauge(voc.getLanguage());
        FM.writeToFile(params_file_name, this.params);
    }


}
