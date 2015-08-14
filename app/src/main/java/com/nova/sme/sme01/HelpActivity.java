package com.nova.sme.sme01;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.nova.sme.sme01.miscellanea.ApplicationAttributes;
import com.nova.sme.sme01.miscellanea.CustomBar;
import com.nova.sme.sme01.miscellanea.Dialogs.MyDialog;
import com.nova.sme.sme01.miscellanea.FileManager;
import com.nova.sme.sme01.miscellanea.MyColors;
import com.nova.sme.sme01.miscellanea.MyHttpRequest;
import com.nova.sme.sme01.miscellanea.Parameters;
import com.nova.sme.sme01.miscellanea.Vocabulary;

import java.util.Vector;

import static java.sql.DriverManager.println;

public class HelpActivity extends AppCompatActivity {
    private RelativeLayout base_layout;
    private Button         logout_button;
    private FormResizing   FR;
    private FileManager    FM;
    private Vocabulary     voc;
    private CustomBar      ccb;
    private Vector<View>   views = new Vector<View>();
    private String         url_logout;
    private Parameters     params               = new Parameters();
    private String         params_file_name     = "parameters.bin";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
         } catch(Exception err) {
            println(err.getMessage().toString());
        }
        super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_help);


        this.base_layout = (RelativeLayout) findViewById(R.id.base_help);
        this.FR          = new FormResizing(this, base_layout);
        this.FM          = new FileManager(this);
        this.voc         = new Vocabulary();
        this.params      = (Parameters)FM.readFromFile(params_file_name);


        ViewTreeObserver vto = base_layout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {
                base_layout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                FR.resize();

                logout_button = create_custom_bar();
                logout_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        logout_request();
                    }
                });

                FR.resizeRegularLogins(base_layout, null, logout_button, 0.062f);// height's button/total_height

                fillViews(base_layout);
                setAttributes();

                tuneSizes();
            }
        });
    }

    private float convertDpToPixels(int dp) {
        return dp * getResources().getDisplayMetrics().density;
    }
    public float convertPxToDp(float px) {
        return px / getResources().getDisplayMetrics().density;
    }

    private void tuneSizes() {
        float factor    = 455.0f/747.0f;
        int real_width  = FR.getRealWidth();
        float imgWidth  = real_width*0.4f;
        float imgHeight = imgWidth/factor;

        Vector<Integer> imageIds = new Vector<Integer>();
        imageIds.add(R.id.help_1);
        imageIds.add(R.id.help_2);
        imageIds.add(R.id.help_3);
        imageIds.add(R.id.help_4);

        setImgSize(imageIds, (int)imgWidth, (int)imgHeight);
    }

    private void setImgSize(Vector<Integer> imageIds, int imgWidth, int imgHeight) {
        ViewGroup.LayoutParams params;
        ImageView              img;
        int                    id;

        for (int i = 0; i < imageIds.size(); i ++) {
            id            = imageIds.get(i);
            img           = (ImageView) findViewById(id);
            params        = img.getLayoutParams();
            params.width  = imgWidth;
            params.height = imgHeight;
        }
    }

    private void setAttributes() {
        ApplicationAttributes attr = (ApplicationAttributes)FM.readFromFile("attributes.bin");
        if (attr == null)
            attr = new ApplicationAttributes(this);

        attr.setButtons(base_layout, logout_button);

        MyColors colors = attr.getColors();
        colors.setColors(views);

        if (ccb != null)
            ccb.setBackgound();
    }

    private Button create_custom_bar() {
        ccb = new CustomBar(this, base_layout);

        Button button = ccb.getButton();
        if (button != null)
            voc.change_caption(button);


        views.add(ccb.getBase());
        views.add(ccb.getTitle());

        return button;
    }

    private void logout_request() {
        updateURL();
        new MyHttpRequest(this.FR, this, base_layout, voc, url_logout, "BaseXML");
    }

    private void updateURL() {
        ApplicationAttributes attr = (ApplicationAttributes) FM.readFromFile("attributes.bin");
        if (attr == null)
            attr = new ApplicationAttributes(this);

        String base_http = attr.getBaseUrl();

        url_logout       = base_http + "logout/?";
        url_logout      += "id=" + params.getId() + "&companyID=" + params.getcompanyID();
    }

    private void fillViews(View view) {
        String className = view.getClass().getSimpleName();

        if (className.indexOf("RelativeLayout") != -1 || className.indexOf("LinearLayout") != -1) {
            view.setTag("main_background_color");
            views.add(view);
            ViewGroup vg = (ViewGroup) view;
            for (int i = 0; i < vg.getChildCount(); i++)
                fillViews(vg.getChildAt(i));
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_help, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
