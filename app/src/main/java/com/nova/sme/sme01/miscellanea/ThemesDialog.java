package com.nova.sme.sme01.miscellanea;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;

import com.nova.sme.sme01.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/*
 ********************************
 *                              *
 *  Dialog for selecting themes *
 *  (so far buttons only        *
 *                              *
 ********************************
 */
public class ThemesDialog {
    protected RelativeLayout    base_layout;
    protected Vocabulary        voc;
    protected FileManager       FM;
    protected Button            logout_button;
    protected List<RadioButton> radioButtons = new ArrayList<RadioButton>();
    protected List<Button>      buttons      = new ArrayList<Button>();
    protected List<SeekBar>     sbars        = new ArrayList<SeekBar>();

    public ThemesDialog() {

    }
    public ThemesDialog(RelativeLayout base_layout, Vocabulary voc, FileManager FM, Button logout_button) {
        this.base_layout   = base_layout;
        this.voc           = voc;
        this.FM            = FM;
        this.logout_button = logout_button;

 //       show();
    }
    public void show() {
        final Dialog dialog = new Dialog(base_layout.getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.buttons);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0x88000000));


        ViewGroup.LayoutParams params = dialog.getWindow().getAttributes();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
//themes_base_layout
        lp.width  = (int)((float)base_layout.getWidth()*0.95f);
        lp.height =  WindowManager.LayoutParams.WRAP_CONTENT;

        Button OkButton     = (Button) dialog.findViewById(R.id.ok_button);
        Button CancelButton = (Button) dialog.findViewById(R.id.cancel_button);

        voc.change_caption(OkButton);
        voc.change_caption(CancelButton);


        // set theme
        Vector<Button> btns = new Vector<Button>();
        btns.add(OkButton);btns.add(CancelButton);
        ApplicationAttributes attr = setDialogButtonsTheme(btns);

        OkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //find selected item
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

                }
                dialog.dismiss();
            }
        });
        CancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        LinearLayout ll = (LinearLayout)dialog.findViewById(R.id.buttons_base_scroll);//.themes_base_layout);
        LinearLayout inner;
        View         view;
        String       className, err;
        RadioButton  rb;
        Button       btn;
        SeekBar      sb;
        if (ll != null) {
            for (int i = 0; i < ll.getChildCount(); i ++) {
                inner = (LinearLayout)ll.getChildAt(i);
                for (int j = 0; j < inner.getChildCount(); j ++) {
                    view      = inner.getChildAt(j);
                    className = view.getClass().getName().toString().toUpperCase();
                    if (className.indexOf("RADIOBUTTON") != -1) {
                        radioButtons.add((RadioButton) view);
                    } else if (className.indexOf("BUTTON") != -1) {
                        buttons.add((Button)view);
                    } else if(className.indexOf("SEEKBAR") != -1) {
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
        for (int i = 0; i < radioButtons.size(); i ++) {
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
                    Button      bt;
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

        dialog.show();
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


        MyColors colors = attr.getColors();

        ll = (LinearLayout) dialog.findViewById(R.id.themes_base_layout);ll.setTag("dialog_background_color");
        colors.setColor(ll);

        ScrollView sv = (ScrollView) dialog.findViewById(R.id.tv_spinner_id);
        ViewGroup.LayoutParams prms = sv.getLayoutParams();
        prms.height = (int)((float)lp.width*1.2f);

    }
    private int getSelectedColor(int num) {
        Button bt = buttons.get(num);
        return bt.getCurrentTextColor();
    }
    private void setButtonTextColor(SeekBar sb, int color) {
        for (int i = 0; i < sbars.size(); i ++) {
            if (sb == sbars.get(i)) {
                Button bt = buttons.get(i);
                bt.setTextColor(color);
                return;
            }
        }

    }
    protected void resetRadiobuttons(RadioButton rb) {
        RadioButton current;
        for (int j = 0; j < radioButtons.size(); j ++) {
            current = radioButtons.get(j);
            if (rb == current) continue;
            if (current.isChecked())
                current.setChecked(false);
        }
    }
    protected RadioButton selectedItem() {
        RadioButton rb;
        for (int i = 0; i < radioButtons.size(); i ++) {
            rb = radioButtons.get(i);
            if (rb.isChecked())
                return rb;
        }

        return null;
    }
    protected ApplicationAttributes setDialogButtonsTheme(Vector<Button> buttons) {
        ApplicationAttributes attr = (ApplicationAttributes) FM.readFromFile("attributes.bin");
        if (attr == null)
            attr = new ApplicationAttributes();

        attr.setButtons(base_layout, buttons);


        return attr;
    }
    protected void setSeekBars(ApplicationAttributes attr) {
        // set seekbar & buttons textcolor
        Vector<Integer> buttons_text_colors = attr.getButtonColors();
        int             color;
        SeekBar         sb;
        String          err;
        for (int i = 0; i < buttons_text_colors.size(); i ++) {
            try {
                color = buttons_text_colors.get(i);
                sb = sbars.get(i);
                sb.setProgress(color&0xff);
                buttons.get(i).setTextColor(color);
            } catch(Exception e) {
                err = e.getMessage().toString();
            }
        }
    }
}
