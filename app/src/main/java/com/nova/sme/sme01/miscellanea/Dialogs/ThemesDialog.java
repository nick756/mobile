package com.nova.sme.sme01.miscellanea.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
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
import android.widget.TextView;

import com.nova.sme.sme01.R;
import com.nova.sme.sme01.miscellanea.ApplicationAttributes;
import com.nova.sme.sme01.miscellanea.FileManager;
import com.nova.sme.sme01.miscellanea.MyColors;
import com.nova.sme.sme01.miscellanea.ViewsGroup;
import com.nova.sme.sme01.miscellanea.Vocabulary;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import static java.sql.DriverManager.println;

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

    private   Vector<ViewsGroup>     groups = new Vector<ViewsGroup>();

    public ThemesDialog() {

    }
    protected void setButtonHeight(Button button) {
        if (logout_button == null) return;
        ViewGroup.LayoutParams params = logout_button.getLayoutParams();
        int height                    = params.height;

        params = button.getLayoutParams();
        params.height = height;
    }

    public ThemesDialog(RelativeLayout base_layout, Vocabulary voc, FileManager FM, Button logout_button) {
        this.base_layout   = base_layout;
        this.voc           = voc;
        this.FM            = FM;
        this.logout_button = logout_button;
    }
    public void show() {
        final Dialog dialog = new Dialog(base_layout.getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.buttons);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0x88000000));


        ViewGroup.LayoutParams params = dialog.getWindow().getAttributes();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();

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
                ViewsGroup vg = selectedGroup();
                if (vg != null) {
                    ApplicationAttributes attr = (ApplicationAttributes) FM.readFromFile("attributes.bin");
                    if (attr == null)
                        attr = new ApplicationAttributes(base_layout.getContext());

                    attr.setSelectedButton(vg.index);
                    attr.setSelectedButtonColor(vg.btn.getCurrentTextColor());
                    attr.setButtonColors(groups);

//                    FM.writeToFile("attributes.bin", attr);
                    attr.setButtons(base_layout, logout_button);
                    FM.writeToFile("attributes.bin", attr);

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
        String       packageName = base_layout.getContext().getPackageName();
        if (ll != null) {
            ViewsGroup group;
            for (int i = 0; i < ll.getChildCount(); i ++) {
                inner = (LinearLayout)ll.getChildAt(i);
                group = new ViewsGroup();
                for (int j = 0; j < inner.getChildCount(); j ++) {
                    view      = inner.getChildAt(j);
                    className = view.getClass().getName().toString().toUpperCase();
                    if (className.indexOf("RADIOBUTTON") != -1) {
                        group.rbtn = (RadioButton) view;
                    } else if (className.indexOf("BUTTON") != -1) {
                        group.btn = (Button) view;
                    } else if(className.indexOf("SEEKBAR") != -1) {
                        sb = (SeekBar) view;
                        group.sb = (SeekBar)(view);

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
                                ((Button) seekBar.getTag()).setTextColor(color);

                            }
                        });
                    }
                }
                //
                group.index = groups.size();
                group.btn.setTag(group.rbtn);
                group.sb.setTag(group.btn);

 //               assignResourceId(packageName, base_layout.getContext(), group);

                groups.add(group);
            }
        }
        ViewsGroup vg;
        for (int i = 0; i < groups.size(); i ++) {
            vg = groups.get(i);
            vg.rbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RadioButton rbtn;
                    rbtn = (RadioButton) v;
                    resetRadiobuttons(rbtn);
                }
            });


            vg.btn.setOnClickListener(new View.OnClickListener() {
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

        int num = attr.getSelectedButton();
        for (int i = 0; i < groups.size(); i ++)
            groups.get(i).rbtn.setChecked(i == num);

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


        setButtonHeight(OkButton);
        setButtonHeight(CancelButton);
    }

    protected void resetRadiobuttons(RadioButton rb) {
        ViewsGroup vg;
        for (int j = 0; j < groups.size(); j ++) {
            vg = groups.get(j);

            if (rb == vg.rbtn)
                continue;
            if (vg.rbtn.isChecked())
                vg.rbtn.setChecked(false);
        }
    }

    protected ViewsGroup selectedGroup() {
        ViewsGroup vg;
        for (int i = 0; i < groups.size(); i ++) {
            vg = groups.get(i);
            if (vg.rbtn.isChecked())
                return vg;
        }

        return null;
    }

    protected RadioButton selectedItem() {
        ViewsGroup vg;
        for (int i = 0; i < groups.size(); i ++) {
            vg = groups.get(i);
            if (vg.rbtn.isChecked())
                return vg.rbtn;
        }
        return null;
    }
    protected ApplicationAttributes setDialogButtonsTheme(Vector<Button> buttons) {
        ApplicationAttributes attr = (ApplicationAttributes) FM.readFromFile("attributes.bin");
        if (attr == null)
            attr = new ApplicationAttributes(base_layout.getContext());

        attr.setButtons(base_layout, buttons);


        return attr;
    }
    protected void setSeekBars(ApplicationAttributes attr) {
        // set seekbar & buttons textcolor
        Vector<Integer> buttons_text_colors = attr.getButtonColors();
        int             color;
        SeekBar         sb;
        String          err;
        ViewsGroup      vg;

        for (int i = 0; i < buttons_text_colors.size(); i ++) {
            try {
                color = buttons_text_colors.get(i);
                vg    = groups.get(i);
                vg.sb.setProgress(color & 0xff);
                vg.btn.setTextColor(color);
            } catch(Exception e) {
                err = e.getMessage().toString();
            }
        }
    }


}
