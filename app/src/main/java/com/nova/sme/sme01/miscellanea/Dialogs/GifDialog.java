package com.nova.sme.sme01.miscellanea.Dialogs;


import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.RelativeLayout;

import com.nova.sme.sme01.R;

public class GifDialog {
    private Context        context;
    private RelativeLayout base_layout;
    private Dialog         dialog;

    public GifDialog(RelativeLayout base_layout) {
        this.base_layout = base_layout;
        this.context     = base_layout.getContext();
        show();
    }
    private void show() {
        /*final Dialog */dialog = new Dialog(this.context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.gif_layout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0x88000000));

        ViewGroup.LayoutParams     params = dialog.getWindow().getAttributes();
        WindowManager.LayoutParams lp     = new WindowManager.LayoutParams();

        lp.width  = (int)((float)base_layout.getWidth()*0.9f);
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        WebView web = (WebView)dialog.findViewById(R.id.web_view_loader);
        web.loadUrl("file:///android_asset/gif.html");//preloader.gif");

        dialog.show();
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    }

    public void dismiss() {
        dialog.dismiss();
    }

}
