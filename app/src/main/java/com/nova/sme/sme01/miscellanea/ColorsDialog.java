package com.nova.sme.sme01.miscellanea;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.PaintDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
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
import java.util.List;
import java.util.Vector;

import static java.sql.DriverManager.println;


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
    private Vector<View>   views = new Vector<View>();
    private int            selected = 0;


    public ColorsDialog(RelativeLayout base_layout, Vocabulary voc, FileManager FM, Button logout_button) {
        super(base_layout, voc, FM, logout_button);
    }

    public ColorsDialog() {

    }

    public void show() {
        final Dialog dialog = new Dialog(base_layout.getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.color_selector);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0x88000000));

        ViewGroup.LayoutParams params = dialog.getWindow().getAttributes();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();

        lp.width = (int) ((float) base_layout.getWidth() * 0.95f);
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        Button OkButton     = (Button) dialog.findViewById(R.id.submit_colors);
        Button CancelButton = (Button) dialog.findViewById(R.id.cancel_colors);
        Button Bt           = (Button) dialog.findViewById(R.id.cl_button);

        // set theme
        Vector<Button> btns = new Vector<Button>();
        btns.add(OkButton);btns.add(CancelButton);btns.add(Bt);
        ApplicationAttributes attr = setDialogButtonsTheme(btns);

        voc.change_captions(btns);

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

        radioButtons.add((RadioButton) dialog.findViewById(R.id.action_bar_background_id));
        radioButtons.add((RadioButton) dialog.findViewById(R.id.main_back_ground_id));
        radioButtons.add((RadioButton) dialog.findViewById(R.id.text_background_id));
        radioButtons.add((RadioButton) dialog.findViewById(R.id.dialog_background_id));

        for (int i = 0; i < radioButtons.size(); i ++) {
            radioButtons.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String tag = (String) v.getTag();
                    selected   = Integer.parseInt(tag);
                    new cls(views.get(selected), sbars);
                }
            });

        }
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
                    changeColor();
                }
            });
        }

        views.add(dialog.findViewById(R.id.cl_actionbar));
        views.add(dialog.findViewById(R.id.cl_base));
        views.add(dialog.findViewById(R.id.cl_text));//
        views.add(dialog.findViewById(R.id.cl_dialog));

        voc.change_caption((TextView) views.get(2));

        radioButtons.get(selected).setChecked(true);

        dialog.show();
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


        // sizes
        //lp.width
        float width       = (float)lp.width - converDpToPixels(10);
        RelativeLayout rl = (RelativeLayout) dialog.findViewById(R.id.cl_base);
        params            = rl.getLayoutParams();

        params.width      = (int)(width*0.8f);

/*
            RelativeLayout l = (RelativeLayout) views.get(3);
            ViewGroup.LayoutParams prms            = l.getLayoutParams();
            int h = prms.height;
            params.height     = (int)(width*0.8f);//h*3;
            prms.height       = params.height/2;
*/
        rl                = (RelativeLayout) dialog.findViewById(R.id.base_rb_id);
        params            = rl.getLayoutParams();
        params.width      = (int)(width*0.2f);

        params            = Bt.getLayoutParams();
        params.height     = (int)(width/15.0f);

        selected    = attr.colors.getSelected_color_choise(); //0- 3
        new cls(views.get(selected), sbars);
//        radioButtons.get(selected).setChecked(true);
//        new cls(views.get(selected), sbars);
    }

    private int getColor() {
        return  Color.rgb(sbars.get(0).getProgress(), sbars.get(1).getProgress(), sbars.get(2).getProgress());
    }
    private void changeColor() {
        int      color      = Color.rgb(sbars.get(0).getProgress(), sbars.get(1).getProgress(), sbars.get(2).getProgress());
        View     view       = views.get(selected);
        String   class_name = view.getClass().getName().toUpperCase();

        if (class_name.indexOf("TEXTVIEW") != -1) {
            TextView text = (TextView) view;
            text.setBackgroundColor(color);
        } else if (class_name.indexOf("LINERALAYOUT") != -1) {
            if (view.getId() == R.id.cl_dialog) {
                GradientDrawable shape;

                shape = new GradientDrawable();
                shape.setColor(color);
                shape.setCornerRadius(6);
                shape.setStroke(2, Color.BLACK);
                view.setBackgroundDrawable(shape);
            } else {
                LinearLayout ll = (LinearLayout) view;
                ll.setBackgroundColor(color);
            }
        } else if (class_name.indexOf("RELATIVELAYOUT") != -1) {
            if (view.getId() == R.id.cl_dialog) {
                GradientDrawable shape;

                shape = new GradientDrawable();
                shape.setColor(color);
                shape.setCornerRadius(6);
                shape.setStroke(2, Color.BLACK);
                view.setBackgroundDrawable(shape);
            } else {
                RelativeLayout rl = (RelativeLayout) view;
                rl.setBackgroundColor(color);
            }
        } else {
            /*
            if (view.getId() == R.id.cl_dialog) {
                GradientDrawable shape;

                shape = new GradientDrawable();
                shape.setColor(color);
                shape.setCornerRadius(6);
                shape.setStroke(1, Color.BLACK);
                view.setBackgroundDrawable(shape);
            } else {
                Drawable drawable = view.getBackground();
                drawable.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_OVER));
            }*/
        }
    }

    private float converDpToPixels(int dp) {
        return dp * base_layout.getResources().getDisplayMetrics().density;
    }

    private void save() {
        ApplicationAttributes attr = (ApplicationAttributes) FM.readFromFile("attributes.bin");
        if (attr == null)
            attr = new ApplicationAttributes();

        attr.colors.setSelected_color_choise(selected);
        FM.writeToFile("attributes.bin", attr);
    }

    int width_height(View view) {
        int specWidth = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(specWidth, specWidth);
        int questionWidth = view.getMeasuredWidth();
        int questionHeight = view.getMeasuredHeight();

        return questionWidth*questionHeight;
    }

    public class cls {
        private View          view;
        private List<SeekBar> seek_bars;

        public cls(View v, List<SeekBar> sbars) {
            this.view      =  v;
            this.seek_bars = sbars;

            view.post(new Runnable() {
                public void run() {
                    int color = getBackgroundColor(view);
                    seek_bars.get(2).setProgress(color & 0x000000FF);        // BLUE    218
                    seek_bars.get(1).setProgress((color & 0x0000FF00) >> 8);  // GREEN   218
                    seek_bars.get(0).setProgress((color & 0x00FF0000) >> 16); // RED     218
                }
            });
        }
        private int getBackgroundColor(View view) {
            try {
                Drawable drawable;

                drawable   = view.getBackground();
                int width  = view.getWidth();
                int height = view.getHeight();

                Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
                Canvas canvas = new Canvas(bitmap);
                drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                drawable.draw(canvas);

                int pixel = bitmap.getPixel(width / 2, height / 2);

                return pixel;
            } catch(Exception e) {//java.lang.ClassCastException: android.graphics.drawable.ColorDrawable cannot be cast to android.graphics.drawable.PaintDrawable
                println(e.getMessage().toString());
            }
            return 0;
        }
    }

}