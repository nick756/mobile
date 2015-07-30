package com.nova.sme.sme01.miscellanea;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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

    public void show() {
        final Dialog dialog = new Dialog(base_layout.getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.colors_selector);
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
/*
        radioButtons.add((RadioButton) dialog.findViewById(R.id.action_bar_background_id));
        radioButtons.add((RadioButton) dialog.findViewById(R.id.main_back_ground_id));
        radioButtons.add((RadioButton) dialog.findViewById(R.id.text_background_id));
        radioButtons.add((RadioButton) dialog.findViewById(R.id.dialog_background_id));
*/
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



        //action_bar_background_id
/*
        LinearLayout ll = (LinearLayout) dialog.findViewById(R.id.buttons_base_scroll);//.themes_base_layout);
        LinearLayout inner;
        View view;
        String className, err;
        RadioButton rb;
        Button btn;
        SeekBar sb;
        if (ll != null) {
            for (int i = 0; i < ll.getChildCount(); i++) {
                inner = (LinearLayout) ll.getChildAt(i);
                for (int j = 0; j < inner.getChildCount(); j++) {
                    view = inner.getChildAt(j);
                    className = view.getClass().getName().toString().toUpperCase();
                    if (className.indexOf("RADIOBUTTON") != -1) {
                        radioButtons.add((RadioButton) view);
                    } else if (className.indexOf("BUTTON") != -1) {
                        buttons.add((Button) view);
                    } else if (className.indexOf("SEEKBAR") != -1) {
                        sb = (SeekBar) view;
                        sbars.add(sb);

                        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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
                                // 0- 255
                                int color = Color.rgb(progress, progress, progress);
                                setButtonTextColor(seekBar, color);
                            }
                        });
                    }
                }
            }
        }
        for (int i = 0; i < radioButtons.size(); i++) {
            rb = radioButtons.get(i);
            rb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RadioButton rbtn;
                    rbtn = (RadioButton) v;
                    resetRadiobuttons(rbtn);
                }
            });

            btn = buttons.get(i);
            btn.setTag(rb);

            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Button bt;
                    RadioButton rb;

                    bt = (Button) v;
                    rb = (RadioButton) v.getTag();
                    rb.setChecked(true);
                    resetRadiobuttons(rb);
                }
            });

        }

        rb = radioButtons.get(attr.getSelectedButton());
        rb.setChecked(true);
        resetRadiobuttons(rb);
        setSeekBars(attr);
*/
        dialog.show();
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        // sizes
        //lp.width
        float width  = (float)lp.width - converDpToPixels(10);
        ImageView img = (ImageView) dialog.findViewById(R.id.main_colors);
        params        = img.getLayoutParams();

        params.width  = (int)(width*0.8f);
        params.height = params.width;

        RelativeLayout rl = (RelativeLayout) dialog.findViewById(R.id.base_rb_id);
        params            = rl.getLayoutParams();
        params.width      = (int)(width*0.2f);

    }
    private float converDpToPixels(int dp) {
        return dp * base_layout.getResources().getDisplayMetrics().density;
    }

}
