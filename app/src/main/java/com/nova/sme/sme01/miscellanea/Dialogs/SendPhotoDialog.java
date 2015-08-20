package com.nova.sme.sme01.miscellanea.Dialogs;

/*
 ******************************
 *                            *
 *  Send photo to the server  *
 *                            *
 ******************************
 */

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.nova.sme.sme01.FormResizing;
import com.nova.sme.sme01.R;
import com.nova.sme.sme01.miscellanea.ApplicationAttributes;
import com.nova.sme.sme01.miscellanea.FileManager;
import com.nova.sme.sme01.miscellanea.Parameters;
import com.nova.sme.sme01.miscellanea.TextResizing;
import com.nova.sme.sme01.miscellanea.UploadImage;
import com.nova.sme.sme01.miscellanea.Vocabulary;

import org.springframework.util.support.Base64;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

public class SendPhotoDialog extends MyDialog {
    private ApplicationAttributes attr;
    private Button                logout_button;
    private String                image_path;
    private Activity              activity;
    private Bitmap                bitmap = null;
    private Dialog                dialog;

    private android.view.WindowManager.LayoutParams lp = new WindowManager.LayoutParams();


    public SendPhotoDialog(Activity activity, FormResizing FR, Vocabulary voc, RelativeLayout base_layout, Button logout_button, String image_path) {
        super(FR, voc, base_layout);
        this.logout_button = logout_button;
        this.image_path    = image_path;
        this.activity      = activity;
    }
    public SendPhotoDialog(Activity activity, FormResizing FR, Vocabulary voc, RelativeLayout base_layout, Button logout_button, Bitmap bitmap) {
        super(FR, voc, base_layout);
        this.logout_button = logout_button;
        this.image_path    = "";
        this.activity      = activity;
        this.bitmap        = bitmap;
    }



    private void setButtonHeight(Button button) {
        if (logout_button == null) return;
        ViewGroup.LayoutParams params;

        params = button.getLayoutParams();
        params.height = logout_button.getHeight();//height;
    }

    public void show() {
        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.photo);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0x88000000));


        ViewGroup.LayoutParams params = dialog.getWindow().getAttributes();
//        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();

        lp.width  = (int)((float)base_layout.getWidth()*0.9f);
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        Button okButton = (Button) dialog.findViewById(R.id.send_photo);
        Button cancelButton = (Button) dialog.findViewById(R.id.cancel_photo);


        // COLORS //
        LinearLayout ll = (LinearLayout) dialog.findViewById(R.id.back_buttons_photo);ll.setTag("dialog_background_color");
        views.add(ll);
        dialog_layout   = (RelativeLayout) dialog.findViewById(R.id.photo_base);dialog_layout.setTag("dialog_background_color");
        views.add(dialog_layout);
        // COLORS //

        // set theme
        Vector<Button> btns = new Vector<Button>();
        btns.add(okButton);btns.add(cancelButton);
        attr = setDialogButtonsTheme(btns);
        voc.change_captions(btns);


        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // prepare url, data & send
                String url = getHttp();
                new UploadImage(bitmap, url, new GifDialog(base_layout, "file:///android_asset/gif_upload_image.html"));

                dialog.dismiss();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        ImageView img = (ImageView)dialog.findViewById(R.id.photo_id);

        if (bitmap == null)
            bitmap = BitmapFactory.decodeFile(image_path);


        img.setImageBitmap(bitmap);

        SetColors();

        dialog.show();

//        dialog.getWindow().setAttributes(lp);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        setButtonHeight(okButton);
        setButtonHeight(cancelButton);

        ViewTreeObserver vto = dialog_layout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {
                dialog.getWindow().setAttributes(lp);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                int width  = dialog_layout.getWidth();//320
                int height = dialog_layout.getHeight();//297

                ViewGroup.LayoutParams params = dialog_layout.getLayoutParams();
            }
        });

        //dialog_layout
    }

    private String getHttp() {
        String http = "";

        FileManager FM = new FileManager(activity);
        ApplicationAttributes attr = (ApplicationAttributes)FM.readFromFile("attributes.bin");
        if (attr == null) {
            attr = new ApplicationAttributes(activity);
            FM.writeToFile("attributes.bin", attr);
        }

        http = attr.getBaseUrl();
        http += "addInvoice/?";


        Parameters params = (Parameters) FM.readFromFile("parameters.bin");
        http += "id=" + params.getId() + "&companyID=" + params.getcompanyID();


        return http;
    }
/*
    private String getImageBytes() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, baos);
        byte[] ba = baos.toByteArray();

        return Base64.encodeBytes(ba);
    }
*/

}
