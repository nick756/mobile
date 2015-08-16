package com.nova.sme.sme01;

import android.content.Intent;
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
import android.widget.RelativeLayout;

import com.nova.sme.sme01.miscellanea.ApplicationAttributes;
import com.nova.sme.sme01.miscellanea.CustomBar;
import com.nova.sme.sme01.miscellanea.FileManager;
import com.nova.sme.sme01.miscellanea.MyColors;
import com.nova.sme.sme01.miscellanea.MyHttpRequest;
import com.nova.sme.sme01.miscellanea.Parameters;
import com.nova.sme.sme01.miscellanea.Vocabulary;

import java.util.Vector;

import static java.sql.DriverManager.println;

public class HelpNActivity extends AppCompatActivity {
    private RelativeLayout       base_layout;
    private Button               logout_button;
    private FormResizing         FR;
    private FileManager          FM;
    private Vocabulary           voc;
    private CustomBar            ccb;
    private String               url_logout;
    private Parameters           params               = new Parameters();
    private String               params_file_name     = "parameters.bin";
    private Vector<View>         views                = new Vector<View>();
    private Vector<Button>       bt_vector            = new <Button>Vector();
    private ApplicationAttributes attr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } catch(Exception err) {
            println(err.getMessage().toString());
        }
        super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_help_n);

        this.base_layout = (RelativeLayout) findViewById(R.id.base_help_n);
        this.FR          = new FormResizing(this, base_layout);
        this.FM          = new FileManager(this);
        this.voc         = new Vocabulary();
        this.params      = (Parameters)FM.readFromFile(params_file_name);

        if (this.params != null)
            this.voc.setLanguage(params.getLanguage());

        attr = (ApplicationAttributes)FM.readFromFile("attributes.bin");
        if (attr == null)
            attr = new ApplicationAttributes(this);


        ViewTreeObserver vto = base_layout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {
                base_layout.getViewTreeObserver().removeGlobalOnLayoutListener(this);

                // translation
                addTextViews(base_layout);
                voc.change_views_captions(views);

                // colors
                addLayouts(base_layout);

                FR.resize();

                logout_button = create_custom_bar();
                logout_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                 logout_request();
                    }
                });

                FR.resizeRegularLogins(base_layout, null, logout_button, 0.062f);// height's button/total_height

                setAttributes();

            }
        });

//b.setCompoundDrawablesWithIntrinsicBound(null,R.drawable.home_icon_test,null,nu‌​ll)

    }

    private void setAttributes() {
//        ApplicationAttributes attr = (ApplicationAttributes)FM.readFromFile("attributes.bin");

        attr.setButtons(base_layout, logout_button);
        attr.setButtons(base_layout, bt_vector);

        MyColors colors = attr.getColors();
        colors.setColors(views);

        if (ccb != null)
            ccb.setBackgound();
        //android:drawableLeft="@mipmap/ic_action"
    }

    private void addTextViews(View view) {
        ViewGroup.LayoutParams params;
        Button                 btn;
        String                 className = view.getClass().getSimpleName();
        int                    height;
        int                    width;

        if (className.indexOf("TextView") != -1) {
            view.setTag("main_background_color");
            views.add(view);
        }

        if (className.indexOf("Button") != -1) {
            btn = (Button)view;
            bt_vector.add(btn);

            height = btn.getHeight();
            if (height == 0) {
                btn.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                height = btn.getMeasuredHeight();
            }
            width        = (int)((float)height*2.0f);
            params       = btn.getLayoutParams();
            params.width = width;
        }


        if ((view instanceof ViewGroup)) {
            ViewGroup vg = (ViewGroup) view;
            for (int i = 0; i < vg.getChildCount(); i++)
                addTextViews(vg.getChildAt(i));
        }

    }
    private void addLayouts(View view) {
        String className = view.getClass().getSimpleName();

        if (className.indexOf("RelativeLayout") != -1 || className.indexOf("LinearLayout") != -1) {
            view.setTag("main_background_color");
            views.add(view);
        }

        if ((view instanceof ViewGroup)) {
            ViewGroup vg = (ViewGroup) view;
            for (int i = 0; i < vg.getChildCount(); i++)
                addLayouts(vg.getChildAt(i));
        }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
 //       getMenuInflater().inflate(R.menu.menu_help_n, menu);
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

    public void onClick(View view) {
        int id          = view.getId();
//        CommonClass c_c = new CommonClass();
//        c_c.help_id     = id;
        Intent intent   = new Intent(this, HelpItemActivity.class);

        intent.putExtra("help_id", id);
        startActivity(intent);
        /*
        R.id.help_trouble
        R.id.help_colors
        R.id.help_buttons
        R.id.help_url
        R.id.help_language
        R.id.help_view_trans
        R.id.help_per_trans
        R.id.help_op_list
        R.id.help_login
        R.id.help_general

         */
    }

 }
