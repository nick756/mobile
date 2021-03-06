package com.nova.sme.sme01.miscellanea.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;

import com.nova.sme.sme01.R;
import com.nova.sme.sme01.miscellanea.ApplicationAttributes;
import com.nova.sme.sme01.miscellanea.ButtonsSupport;
import com.nova.sme.sme01.miscellanea.FileManager;
import com.nova.sme.sme01.miscellanea.MyColors;
import com.nova.sme.sme01.miscellanea.ShapeBuilder;
import com.nova.sme.sme01.miscellanea.ViewsGroup;
import com.nova.sme.sme01.miscellanea.Vocabulary;
import com.nova.sme.sme01.miscellanea.WindowMetrics;

import java.io.Serializable;
import java.util.Vector;
import java.util.jar.Attributes;

import static java.sql.DriverManager.println;

/*
 ***********************************
 *                                 *
 *  To select button's background  *
 *                                 *
 ***********************************
 */
public class ButtonsDialog {
    private RelativeLayout   base_layout;
    private Vocabulary       voc;
    private FileManager      FM;
    private Button           logout_button;
    private items            itemsObject;
    private WindowMetrics    wm;

    public ButtonsDialog(RelativeLayout base_layout, Vocabulary voc, FileManager FM, Button logout_button) {
        this.base_layout   = base_layout;
        this.voc           = voc;
        this.FM            = FM;
        this.logout_button = logout_button;

        itemsObject = new items();

        wm = (WindowMetrics)FM.readFromFile("windowMetrics.bin");

    }
    protected void setButtonHeight(Button button) {
        if (logout_button == null) return;
        ViewGroup.LayoutParams params;
        params        = button.getLayoutParams();
        params.height = logout_button.getHeight();//height;
    }

    public void show() {
        final Dialog dialog = new Dialog(base_layout.getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.buttons_n);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0x88000000));

 //       ViewGroup.LayoutParams params = dialog.getWindow().getAttributes();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();

        lp.width  = (int)((float)base_layout.getWidth()*0.95f);
        lp.height =  WindowManager.LayoutParams.WRAP_CONTENT;

        Button OkButton     = (Button) dialog.findViewById(R.id.ok_button_theme);
        Button CancelButton = (Button) dialog.findViewById(R.id.cancel_button_theme);

        // set theme
        Vector<Button> btns = new Vector<Button>();
        btns.add(OkButton);btns.add(CancelButton);

        voc.change_captions(btns);
        ApplicationAttributes attr = setDialogButtonsTheme(btns);

        OkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //find selected item
//                FM.writeToFile("ButtonsObject.bin", itemsObject);
                itemsObject.saveInFile();
                dialog.dismiss();
            }
        });
        CancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        fillScroll(dialog);

        dialog.show();
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        MyColors colors = attr.getColors();

        LinearLayout ll;//new_buttons_base
        ll = (LinearLayout) dialog.findViewById(R.id.new_buttons_base);ll.setTag("dialog_background_color");
        colors.setColor(ll);

        ScrollView sv = (ScrollView) dialog.findViewById(R.id.sv_spinner_id);
        ViewGroup.LayoutParams prms = sv.getLayoutParams();
        prms.height = (int)((float)lp.width*1.2f);

        setButtonHeight(OkButton);
        setButtonHeight(CancelButton);

    }

    private class itemObject {
        private items       parent;
        public  Button      btn;
        public  RadioButton rbtn;
        public  SeekBar     sbar;
        public  int         seek_position = 0;
        public  int         index;
        public  itemObject  me;

        public itemObject() {
            me = this;
        }

        public void init() {
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                rbtn.setChecked(true);
                arrange(me);
                }
            });
            rbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    arrange(me);
                }
            });
            sbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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
                    int color     = Color.rgb(progress, progress, progress);
                    seek_position = progress;
                    btn.setTextColor(color);
                }
            });

        }

        public void arrange(itemObject object) {
            itemObject         item;
            Vector<itemObject> vector = parent.getObjects();
            for (int i = 0; i < vector.size(); i ++) {
                item = vector.get(i);
                if (item == object) {
                    parent.setSelected(this.index);
                    continue;
                }
                if (item.rbtn.isChecked())
                    item.rbtn.setChecked(false);
            }
            if (!object.rbtn.isChecked())
                object.rbtn.setChecked(true);
        }
    }
    private class items {
        private Vector<itemObject> objects  = new Vector<itemObject>();
        private int                selected = 0;

        public void setSelected(int selected) {this.selected = selected;}
        public Vector<itemObject> getObjects() {return objects;}

        private ButtonsSupport savedData;

        public items() {
            savedData = (ButtonsSupport) FM.readFromFile("ButtonsObject.bin");
        }

        public void add(itemObject item) {
            item.parent = this;
            item.index  = objects.size();

            if (item.index == 0)
                item.rbtn.setChecked(true);

            objects.add(item);
        }
        public void init() {
            int position;
            if (savedData != null) {
                selected = savedData.selected;
                if (selected > objects.size() - 1)
                    selected = 0;
                for (int j = 0; j < savedData.positions.size(); j ++) {
                    try {
                        position = savedData.positions.get(j);
                        objects.get(j).seek_position = position;
                        objects.get(j).sbar.setProgress(position);
                    } catch(Exception e) {

                    }
                }
            }
            objects.get(selected).arrange(objects.get(selected));
        }
        public void saveInFile() {
            ButtonsSupport sd = new ButtonsSupport();
            sd.selected         = selected;

            for (int j = 0; j < objects.size(); j ++)
                sd.positions.add(objects.get(j).seek_position);


            FM.writeToFile("ButtonsObject.bin", sd);
        }
    }

    private void fillGroup(ShapeBuilder sb, LayoutInflater inflater, LinearLayout base, int start_color, int end_color, int start_incr, int end_incr) {
        int          stroke_color = Color.rgb(0x87, 0x87, 0x87);
        LinearLayout ll;
        itemObject   item;

        int start = start_color;
        int end   = end_color;
        int color_start, color_end;

        int strokeSize = (int)(3.0f*wm.density);
        int radius     = (int)(8.0f*wm.density);

        for (int j = 0; j < 3; j ++) {
            ll = (LinearLayout) inflater.inflate(R.layout.button_item, null);
            base.addView(ll);

            item      = new itemObject();
            item.btn  = (Button)      ll.getChildAt(0);
            item.rbtn = (RadioButton) ll.getChildAt(1);
            item.sbar = (SeekBar)     ll.getChildAt(2);
            item.init();

            itemsObject.add(item);

            setButtonHeight(item.btn);

            try {
                start += start_incr;
                end   += end_incr;
                color_start = Color.rgb(start, start, start);
                color_end   = Color.rgb(end, end, end);

                Drawable d = sb.buildSelectorShapeFromColors(color_start, color_end, stroke_color, color_end, color_start, stroke_color, strokeSize, radius);
                item.btn.setBackgroundDrawable(d);
            } catch (Exception e) {
                println(e.getMessage().toString());
            }
        }
    }

    private void fillScroll(Dialog dialog) {
        Context        context  =  base_layout.getContext();
        LinearLayout   base     = (LinearLayout) dialog.findViewById(R.id.buttons_base_scroll_n);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout   ll;
        itemObject     item;

        // default button\
        ll = (LinearLayout) inflater.inflate(R.layout.button_item, null);
        base.addView(ll);

        item      = new itemObject();
        item.btn  = (Button)      ll.getChildAt(0);
        item.rbtn = (RadioButton) ll.getChildAt(1);
        item.sbar = (SeekBar)     ll.getChildAt(2);
        item.init();
/*
        int nn = base_layout.getContext().getResources().getIdentifier("button_1_selector", "drawable", base_layout.getContext().getApplicationInfo().packageName);
        int nm = R.drawable.button_1_selector;

        if (nn != nm)
            println("");
*/
//        item.btn.setBackground(base_layout.getContext().getResources().getDrawable(R.drawable.button_2_selector));
        itemsObject.add(item);
        setButtonHeight(item.btn);

        ShapeBuilder sb = new ShapeBuilder();

        fillGroup(sb, inflater, base, 255, 0, -24, 24);




        itemsObject.init();
    }
    private ApplicationAttributes setDialogButtonsTheme(Vector<Button> buttons) {
        ApplicationAttributes attr = (ApplicationAttributes) FM.readFromFile("attributes.bin");
        if (attr == null)
            attr = new ApplicationAttributes(base_layout.getContext());

        attr.setButtons(base_layout, buttons);
        return attr;
    }
}
