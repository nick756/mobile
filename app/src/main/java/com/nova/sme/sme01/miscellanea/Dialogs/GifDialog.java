package com.nova.sme.sme01.miscellanea.Dialogs;


import android.app.Dialog;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.RelativeLayout;

import com.nova.sme.sme01.R;
import com.nova.sme.sme01.miscellanea.FileManager;
import com.nova.sme.sme01.miscellanea.WindowMetrics;

import java.io.IOException;
import java.io.InputStream;

import static java.sql.DriverManager.println;

public class GifDialog {
    private Context        context;
    private RelativeLayout base_layout;
    private Dialog         dialog;
    private String         url_address = "";
    private RelativeLayout dialog_layout;
    private WebView        web;
    private FileManager    FM = null;
    private WindowMetrics  wm;

    public GifDialog(RelativeLayout base_layout) {
        this.base_layout = base_layout;
        this.context     = base_layout.getContext();
        this.FM          = new FileManager(context);
        show();
    }
    public GifDialog(RelativeLayout base_layout, String  url_address) {
        this.base_layout = base_layout;
        this.context     = base_layout.getContext();
        this.url_address = url_address;
        this.FM          = new FileManager(context);

        show();
    }

    public Context getContext() {return base_layout.getContext();}
    private void show() {
        wm = (WindowMetrics)FM.readFromFile("windowMetrics.bin");

        dialog = new Dialog(this.context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.gif_layout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0x88000000));

        ViewGroup.LayoutParams     params = dialog.getWindow().getAttributes();
        WindowManager.LayoutParams lp     = new WindowManager.LayoutParams();

        lp.width  = (int)((float)base_layout.getWidth()*0.9f);
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        web = (WebView)dialog.findViewById(R.id.web_view_loader);
        if (url_address.length() == 0)
            web.loadUrl("file:///android_asset/gif.html");
        else
            web.loadUrl(url_address);


//        setWebSize();

        dialog.show();
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        dialog_layout = (RelativeLayout) dialog.findViewById(R.id.base_gif);


//        dialog_layout.removeView(web);

        setWebSize();
/*
        ViewTreeObserver vto = dialog_layout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {
                dialog_layout.getViewTreeObserver().removeGlobalOnLayoutListener(this);

                WindowMetrics wm = (WindowMetrics)FM.readFromFile("windowMetrics.bin");
                if (wm == null) return;



                int width_margin, height_margin;
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) web.getLayoutParams();

                width_margin  = (wm.widthPixels  - params.width)/2;
                height_margin = (wm.heightPixels - params.height)/2;

                params.leftMargin   = width_margin;
                params.rightMargin  = width_margin;
                params.topMargin    = height_margin;
                params.bottomMargin = height_margin;

            }
        });
*/
    }
     private void setWebSize() {
        float gif_width;
        ViewGroup.LayoutParams params;
        if ((gif_width = getGifWidth((url_address.length() == 0)? "preloader_2.gif":"preloader.gif")) == 0) return;

         gif_width = gif_width*wm.scaledDensity;

        params       = dialog_layout.getLayoutParams();
        params.width = (int)gif_width;

        params       = web.getLayoutParams();
        params.width = (int)gif_width;

    }

    private float getGifWidth(String path) {
        AssetManager          assetManager = context.getAssets();
        BitmapFactory.Options options      = new BitmapFactory.Options();
        options.inJustDecodeBounds         = true;

        InputStream is = null;
        try {
            is = assetManager.open(path);
            BitmapFactory.decodeStream(is, null, options);
            return (float)options.outWidth;
        } catch (IOException e) {
            println(e.getMessage().toString());
        } catch(Exception e) {
            println(e.getMessage().toString());
        } finally {
            if (is != null) {
                try {is.close();} catch (IOException ignored) {}
            }
        }

        return 0;
    }

    public void dismiss() {
        dialog.dismiss();
    }

}
