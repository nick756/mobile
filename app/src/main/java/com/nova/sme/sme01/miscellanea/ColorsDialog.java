package com.nova.sme.sme01.miscellanea;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
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
import android.widget.TextView;

import com.nova.sme.sme01.R;

import java.lang.reflect.Field;
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
    private TextView       tv_actionbar_background;
    private RelativeLayout rl_main_background;
    private TextView       tv_text_background;
    private RelativeLayout rl_dialog_background;

    private int            actionbar_background_color;
    private int            main_background_color;
    private int            text_background_color;
    private int            dialog_background_color;

    private Vector<Integer> selected_actual = new Vector<Integer>();

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


/*
int value = image.getRGB(x,y);
R = (byte)(value & 0x000000FF);
G = (byte)((value & 0x0000FF00) >> 8);
B = (byte)((value & 0x00FF0000) >> 16);
A = (byte)((value & 0xFF000000) >> 24);
 */

/*
loat r = Color.red(color) / 255f;
float g = Color.green(color) / 255f;
float b = Color.blue(color) / 255f;

ColorMatrix cm = new ColorMatrix(new float[] {
        // Change red channel
        r, 0, 0, 0, 0,
        // Change green channel
        0, g, 0, 0, 0,
        // Change blue channel
        0, 0, b, 0, 0,
        // Keep alpha channel
        0, 0, 0, 1, 0,
});
ColorMatrixColorFilter cf = new ColorMatrixColorFilter(cm);
myDrawable.setColorFilter(cf);
////////////////


Drawable mDrawable = context.getResources().getDrawable(R.drawable.balloons);
mDrawable.setColorFilter(new
PorterDuffColorFilter(0xffff00,PorterDuff.Mode.MULTIPLY));




/*
    private TextView       tv_actionbar_background;
    private RelativeLayout rl_main_background;
    private TextView       tv_text_background;
    private RelativeLayout rl_dialog_background;
    cl_actionbar
    cl_base
    cl_text
    cl_dialog

         */

        OkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
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
        radioButtons.add((RadioButton) dialog.findViewById(R.id.action_bar_background_id));
        radioButtons.add((RadioButton) dialog.findViewById(R.id.main_back_ground_id));
        radioButtons.add((RadioButton) dialog.findViewById(R.id.text_background_id));
        radioButtons.add((RadioButton) dialog.findViewById(R.id.dialog_background_id));

        for (int i = 0; i < radioButtons.size(); i ++) {
            rb = radioButtons.get(i);
            rb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RadioButton rbtn;
                    rbtn = (RadioButton) v;
                    switch (rbtn.getId()) {
                        case R.id.action_bar_background_id:
                            switcher(0);
                            break;
                        case R.id.main_back_ground_id:
                            switcher(1);
                            break;
                        case R.id.text_background_id:
                            switcher(2);
                            break;
                        case R.id.dialog_background_id:
                            switcher(3);
                            break;
                    }
                }
            });

        }
        //action_bar_background_id
        //main_back_ground_id
        //text_background_id
        //dialog_background_id

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
        tv_actionbar_background    = (TextView) dialog.findViewById(R.id.cl_actionbar);
        rl_main_background         = (RelativeLayout) dialog.findViewById(R.id.cl_base);
        tv_text_background         = (TextView) dialog.findViewById(R.id.cl_text);
        rl_dialog_background       = (RelativeLayout) dialog.findViewById(R.id.cl_dialog);

        actionbar_background_color = getBackgroundColor(tv_actionbar_background);
        main_background_color      = getBackgroundColor(rl_main_background);
        text_background_color      = getBackgroundColor(tv_text_background);
        dialog_background_color    = getBackgroundColor(rl_dialog_background);

        selected_actual.add(actionbar_background_color);
        selected_actual.add(main_background_color);
        selected_actual.add(text_background_color);
        selected_actual.add(dialog_background_color);

        //
        int num    = attr.colors.getSelected_color_choise(); //0- 3
        switcher(num);

        radioButtons.get(num).setChecked(true);

/*
int value = image.getRGB(x,y);
R = (byte)(value & 0x000000FF);
G = (byte)((value & 0x0000FF00) >> 8);
B = (byte)((value & 0x00FF0000) >> 16);
A = (byte)((value & 0xFF000000) >> 24);
 */

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
    private void switcher (int num) {
        sbars.get(0).setProgress(selected_actual.get(num)  & 0x000000FF);
        sbars.get(1).setProgress((selected_actual.get(num) & 0x0000FF00) >> 8);
        sbars.get(2).setProgress((selected_actual.get(num) & 0x00FF0000) >> 16);
    }
    private float converDpToPixels(int dp) {
        return dp * base_layout.getResources().getDisplayMetrics().density;
    }
    private int getBackgroundColor(View view) {
        Drawable drawable = view.getBackground();
        if (drawable instanceof ColorDrawable) {
            ColorDrawable colorDrawable = (ColorDrawable) drawable;
            if (Build.VERSION.SDK_INT >= 11) {
                return colorDrawable.getColor();
            }
            try {
                Field field = colorDrawable.getClass().getDeclaredField("mState");
                field.setAccessible(true);
                Object object = field.get(colorDrawable);
                field = object.getClass().getDeclaredField("mUseColor");
                field.setAccessible(true);
                return field.getInt(object);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    private void save() {
        ApplicationAttributes attr = (ApplicationAttributes) FM.readFromFile("attributes.bin");
        if (attr == null)
            attr = new ApplicationAttributes();

        attr.colors.setSelected_color_choise(getSelectedRadioButton());
        FM.writeToFile("attributes.bin", attr);
    }

    private int getSelectedRadioButton() {
        for (int i = 0; i < radioButtons.size(); i ++)
            if (radioButtons.get(i).isChecked())
                return i;

        return 0;
    }

}
