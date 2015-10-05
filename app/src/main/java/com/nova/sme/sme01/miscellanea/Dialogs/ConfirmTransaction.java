package com.nova.sme.sme01.miscellanea.Dialogs;



import android.app.Activity;
import android.app.Dialog;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nova.sme.sme01.FormResizing;
import com.nova.sme.sme01.R;
import com.nova.sme.sme01.miscellanea.ApplicationAttributes;
import com.nova.sme.sme01.miscellanea.FileManager;
import com.nova.sme.sme01.miscellanea.MyColors;
import com.nova.sme.sme01.miscellanea.MyHttpRequest;
import com.nova.sme.sme01.miscellanea.Vocabulary;
import com.nova.sme.sme01.xml_reader_classes.Operation;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import static java.sql.DriverManager.println;

/*
 ********************************
 *                              *
 *   Confirmation transaction   *
 *   before sending request     *
 *   to server                  *
 *                              *
 ********************************
 */

public class ConfirmTransaction {
    private Activity       activity;
    private Vocabulary     voc;
    private FormResizing   FR;
    private RelativeLayout base_layout;
    private String         http_request;

    private Operation opearion;
    private String    date;
    private String    descr;
    private String    amount;

    private Button    okButton;
    private Button    cancelButton;
    private Button    logout_button;

    private String    photoPath;// to be attached
    private Bitmap    bitmap = null;
    private Bitmap    preparedBitmap;// to send

    private RelativeLayout dialog_layout;
    private Dialog         dialog;
    private boolean        cash;


    public ConfirmTransaction(Activity activity,
                              Vocabulary     voc,
                              FormResizing   FR,
                              RelativeLayout base_layout,
                              String         http_request,
                              Operation      s_opearion,
                              String         s_date,
                              String         s_descr,
                              String         s_sum,
                              boolean        cash,
                              Button         logout_button,
                              String         photoPath) {

        this.activity      = activity;
        this.voc           = voc;
        this.FR            = FR;
        this.base_layout   = base_layout;
        this.http_request  = http_request;
        this.opearion      = s_opearion;
        this.date          = s_date;
        this.descr         = s_descr;
        this.amount        = s_sum;
        this.logout_button = logout_button;
        this.photoPath     = photoPath;
        this.cash          = cash;

    }
    void send_request() {
        if (preparedBitmap != null) {
            new MyHttpRequest(FR, activity, base_layout, voc, http_request, "AddTransaction", new GifDialog(base_layout), this.photoPath, this.preparedBitmap);
        } else {
            new MyHttpRequest(FR, activity, base_layout, voc, http_request, "AddTransaction");
        }
    }
    public void show() {
        dialog = new Dialog(base_layout.getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.before_transaction);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0x88000000));

        ViewGroup.LayoutParams params = dialog.getWindow().getAttributes();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.width  = (int)((float)base_layout.getWidth()*0.9f);
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        TextView text = (TextView) dialog.findViewById(R.id.date_transaction_id);
        text.setText(this.date);


        //----------------
        text             = (TextView) dialog.findViewById(R.id.amount_transaction_id);
        boolean      err = false;
        DecimalFormat df = new DecimalFormat("###,###,###.00");
        try {
            double  val   = Double.parseDouble(this.amount);
            String money  = df.format(val);
            text.setText(money);
        } catch(Exception e) {
            err = true;
        }
        if (err)
            text.setText(this.amount);
        //----------------

        text = (TextView) dialog.findViewById(R.id.before_description);//before_operation_name
        text.setText(this.descr);

        text = (TextView) dialog.findViewById(R.id.before_operation_name);
        text.setText(this.opearion.getName());

        ImageView img = (ImageView)dialog.findViewById(R.id.before_in_out);
        if (opearion.getInbound().equals("true"))
            img.setImageResource(R.mipmap.ic_in_bound);
         else
            img.setImageResource(R.mipmap.ic_out_bound);


        cancelButton = (Button) dialog.findViewById(R.id.cancel_button_trans_id);
        voc.change_caption(cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        okButton = (Button) dialog.findViewById(R.id.ok_transaction);
        voc.change_caption(okButton);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            send_request();
            dialog.dismiss();
             }
        });

        CheckBox cb = (CheckBox) dialog.findViewById(R.id.cashId);

        if (cb != null) {
            cb.setChecked(cash);
            cb.setEnabled(false);
            voc.change_caption(cb);
        }

        // set theme
        Vector<Button> btns = new Vector<Button>();
        btns.add(okButton);btns.add(cancelButton);
        setDialogButtonsTheme(btns);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();

        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        ApplicationAttributes attr = (ApplicationAttributes) new FileManager(base_layout.getContext()).readFromFile("attributes.bin");
        if (attr == null)
            attr = new ApplicationAttributes(activity);

        Vector<View> views = new Vector<View>();

        dialog_layout = (RelativeLayout) dialog.findViewById(R.id.base_layout_before_transaction_id);
        dialog_layout.setTag("dialog_background_color");
        views.add(dialog_layout);

        TextView tv1        = (TextView)       dialog.findViewById(R.id.date_transaction_id);               tv1.setTag("dialog_background_color");
        TextView tv2        = (TextView)       dialog.findViewById(R.id.amount_transaction_id);             tv2.setTag("dialog_background_color");
        TextView tv3        = (TextView)       dialog.findViewById(R.id.before_description);                tv3.setTag("dialog_background_color");
        RelativeLayout rll  = (RelativeLayout) dialog.findViewById(R.id.relativeLayout);                    rll.setTag("dialog_background_color");
        RelativeLayout brll = (RelativeLayout) dialog.findViewById(R.id.base_layout_before_transaction_id); brll.setTag("dialog_background_color");

        views.add(tv1);
        views.add(tv2);
        views.add(tv3);
        views.add(rll);
        views.add(brll);

        attr.getColors().setColors(views);

        setButtonsSize();

        imageStuff();
        //
        ViewTreeObserver vto = dialog_layout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {
                dialog_layout.getViewTreeObserver().removeGlobalOnLayoutListener(this);

                if (photoPath.length() == 0)
                    return;

                imageStuff();
            }
        });
    }

    private void imageStuff() {
        float base_layout_height   = (float)base_layout.getHeight();
        float height               = (float)dialog_layout.getHeight();
        float max_available_height = base_layout_height - height;
        float width                = (float)dialog_layout.getWidth();

        if (max_available_height < 0)
            return;

        ImageView photo = (ImageView) dialog.findViewById(R.id.photo_attachment);

        BitmapFactory.Options options  = new BitmapFactory.Options();
        options.inJustDecodeBounds     = true;

        bitmap = BitmapFactory.decodeFile(photoPath, options);
        float imageHeight = (float) options.outHeight;    //1836 3264
        float imageWidth  = (float) options.outWidth;     //3264 1836
        float factor      = imageHeight/imageWidth;

        if (max_available_height > options.outHeight && width > options.outWidth) {
            try {
                bitmap         = BitmapFactory.decodeFile(photoPath);
                preparedBitmap = BitmapFactory.decodeFile(photoPath);
            } catch(OutOfMemoryError e) {
                Toast.makeText(dialog.getContext(), "OUT OF MEMORY", Toast.LENGTH_LONG).show();
                bitmap = null;
            } catch(Exception e) {
                Toast.makeText(dialog.getContext(), e.getMessage().toString(), Toast.LENGTH_LONG).show();
                bitmap = null;
            }
        } else {
            float reqWidth  = width;
            float reqHeight = reqWidth*factor;

            if (reqHeight > max_available_height) {
                reqHeight = max_available_height*.9f;
                reqWidth  = reqHeight/factor;
            }

            try {
                bitmap = decodeSampledBitmapFromResource((int)reqWidth, (int)reqHeight);
            } catch(OutOfMemoryError e) {
                Toast.makeText(dialog.getContext(), "OUT OF MEMORY", Toast.LENGTH_LONG).show();
                bitmap = null;
            } catch(Exception e) {
                Toast.makeText(dialog.getContext(), e.getMessage().toString(), Toast.LENGTH_LONG).show();
                bitmap = null;
            }

            try {
                preparedBitmap = decodeSampledBitmapFromResource((int)reqWidth, (int)reqHeight, 4);
            } catch(OutOfMemoryError e) {
                Toast.makeText(dialog.getContext(), "OUT OF MEMORY", Toast.LENGTH_LONG).show();
                preparedBitmap = null;
            } catch(Exception e) {
                Toast.makeText(dialog.getContext(), e.getMessage().toString(), Toast.LENGTH_LONG).show();
                preparedBitmap = null;
            }
        }

        if (bitmap != null) {
            photo.setImageBitmap(bitmap);
            new post_me(dialog_layout);
        }
    }

    class post_me {

        public post_me(RelativeLayout rl) {
            rl.post(new Runnable() {
                public void run() {
                    addCorrection();
                }
            });
        }
    }

    private void addCorrection() {
        ImageView photo = (ImageView) dialog.findViewById(R.id.photo_attachment);
        float height, base_height;

        height      = (float) dialog_layout.getHeight();
        base_height = (float) base_layout.getHeight();

        if (height > base_height) {
            float factor = height / base_height;

            float photo_height = (float) photo.getHeight();
            float new_height   = photo_height / factor;
            photo.setMaxHeight((int) new_height);
        }
    }

    private void setButtonsSize() {
        if (logout_button == null) return;

        int height = logout_button.getHeight();
        if (height > 0) {
            ViewGroup.LayoutParams prms = okButton.getLayoutParams();
            prms.height                 = height;
            prms.width                  = ViewGroup.LayoutParams.WRAP_CONTENT;//###

            prms        = cancelButton.getLayoutParams();
            prms.height = height;
            prms.width  = ViewGroup.LayoutParams.WRAP_CONTENT;//###
        }
    }

    private ApplicationAttributes setDialogButtonsTheme(Vector<Button> buttons) {
        FileManager FM = new FileManager(base_layout.getContext());
        ApplicationAttributes attr = (ApplicationAttributes) FM.readFromFile("attributes.bin");
        if (attr == null)
            attr = new ApplicationAttributes(base_layout.getContext());

        attr.setButtons(base_layout, buttons);
        FM.writeToFile("attributes.bin", attr);
        return attr;
    }
    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        int height       = options.outHeight;
        int width        = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            int halfHeight = height / 2;
            int halfWidth  = width  / 2;

            while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth)
                inSampleSize *= 2;

        }

        return inSampleSize;
    }
    public Bitmap decodeSampledBitmapFromResource(int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds    = true;

        BitmapFactory.decodeFile(photoPath, options);
         // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(photoPath, options);
    }
    public Bitmap decodeSampledBitmapFromResource(int reqWidth, int reqHeight, int inSampleSize) {

        // First decode with inJustDecodeBounds=true to check dimensions
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds    = true;

        BitmapFactory.decodeFile(photoPath, options);
        // Calculate inSampleSize
        options.inSampleSize = inSampleSize;

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(photoPath, options);
    }

}
