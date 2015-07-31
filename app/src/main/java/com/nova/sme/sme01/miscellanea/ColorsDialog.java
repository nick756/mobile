package com.nova.sme.sme01.miscellanea;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.Config;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import com.nova.sme.sme01.R;

import java.util.Vector;


/*
 *********************************
 *                               *
 *  Dialog for selecting colors  *
 *  1. Action bar color          *
 *  2. Main background           *
 *  3. Text background           *
 *  4. Dialog background         *
 *                               *
 *********************************
 */
public class ColorsDialog extends ThemesDialog {
/*
    protected RelativeLayout    base_layout;
    protected Vocabulary        voc;
    protected FileManager       FM;
    protected Button            logout_button;
    protected List<RadioButton> radioButtons = new ArrayList<RadioButton>();
    protected List<Button>      buttons      = new ArrayList<Button>();
    protected List<SeekBar>     sbars        = new ArrayList<SeekBar>();

 */

    public ColorsDialog(RelativeLayout base_layout, Vocabulary voc, FileManager FM, Button logout_button) {
        super(base_layout, voc, FM, logout_button);
    }

    public ColorsDialog() {

    }
    /*
    cl_actionbar
    cl_base
    cl_text
    cl_dialog
     */

    public void show() {
        final Dialog dialog = new Dialog(base_layout.getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.color_selector);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0x88000000));


        ViewGroup.LayoutParams params = dialog.getWindow().getAttributes();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();

        lp.width = (int) ((float) base_layout.getWidth() * 0.95f);
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;//(int)((float)base_layout.getHeight()*0.5f);// WindowManager.LayoutParams.WRAP_CONTENT;

        Button OkButton = (Button) dialog.findViewById(R.id.submit_colors);
        Button CancelButton = (Button) dialog.findViewById(R.id.cancel_colors);

        voc.change_caption(OkButton);
        voc.change_caption(CancelButton);


        // set theme
        Vector<Button> btns = new Vector<Button>();
        btns.add(OkButton);
        btns.add(CancelButton);
        ApplicationAttributes attr = setDialogButtonsTheme(btns);

        OkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //find selected item
/*
                RadioButton rb = selectedItem();
                int num;
                if (rb != null) {
                    num = Integer.parseInt(rb.getText().toString().trim()) - 1;


                    ApplicationAttributes attr = (ApplicationAttributes) FM.readFromFile("attributes.bin");
                    if (attr == null)
                        attr = new ApplicationAttributes();

                    attr.setSelectedButton(num);
                    attr.setSelectedButtonColor(getSelectedColor(num));
                    attr.setButtonColors(sbars);

                    FM.writeToFile("attributes.bin", attr);
                    attr.setButtons(base_layout, logout_button);

                }*/
                dialog.dismiss();
            }
        });
        CancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        RadioButton rb;

        sbars.add((SeekBar) dialog.findViewById(R.id.seekBar_red));
        sbars.add((SeekBar) dialog.findViewById(R.id.seekBar_green));
        sbars.add((SeekBar) dialog.findViewById(R.id.seekBar_blue));
        for (int i = 0; i < sbars.size(); i ++) {
            sbars.get(i).setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    // TODO Auto-generated method stub
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    // TODO Auto-generated method stub
                }

                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    switch (seekBar.getId()) {
                        case R.id.seekBar_red:

                            break;
                        case R.id.seekBar_green:

                            break;
                        case R.id.seekBar_blue:

                            break;
                    }
                }
            });
        }

        dialog.show();
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        // sizes
        //lp.width
        float width       = (float)lp.width - converDpToPixels(10);
        RelativeLayout rl = (RelativeLayout) dialog.findViewById(R.id.cl_base);
        params            = rl.getLayoutParams();

        params.width  = (int)(width*0.8f);
 //       params.height = params.width;

        rl           = (RelativeLayout) dialog.findViewById(R.id.base_rb_id);
        params       = rl.getLayoutParams();
        params.width = (int)(width*0.2f);


    }
    private float converDpToPixels(int dp) {
        return dp * base_layout.getResources().getDisplayMetrics().density;
    }

}
