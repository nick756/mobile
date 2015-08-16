package com.nova.sme.sme01;

import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

public class HelpItemActivity extends AppCompatActivity {

    private RelativeLayout        base_layout;
    private Button                logout_button;
    private FormResizing          FR;
    private FileManager           FM;
    private Vocabulary            voc;
    private CustomBar             ccb;
    private Vector<View>          views = new Vector<View>();
    private String                url_logout;
    private Parameters            params               = new Parameters();
    private String                params_file_name     = "parameters.bin";
    private String                html_start           = "";
    private String                html_end             = "";
    private int                   help_id;

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
        setContentView(R.layout.activity_help_item);

        this.base_layout = (RelativeLayout) findViewById(R.id.base_help_item);
        this.FR          = new FormResizing(this, base_layout);
        this.FM          = new FileManager(this);
        this.voc         = new Vocabulary();
        this.params      = (Parameters)FM.readFromFile(params_file_name);this.voc.setLanguage(params.getLanguage());

        this.attr = (ApplicationAttributes)FM.readFromFile("attributes.bin");
        if (this.attr == null) {
            this.attr = new ApplicationAttributes(this);
            FM.writeToFile("attributes.bin", this.attr);
        }

        html_start += "<html>";
        html_start += "<body style='margin:0 auto; width:100%; text-align:center'>";
        html_start += "<div style='text-align:left; font-size:100%; line-height:200%'>";

        html_end += "</div>";
        html_end += "</body>";
        html_end += "</html>";

        help_id = getIntent().getIntExtra("help_id", 0);

         int [] ids = {
                R.id.help_trouble,
                R.id.help_colors,
                R.id.help_buttons,
                R.id.help_url,
                R.id.help_language,
                R.id.help_view_trans,
                R.id.help_per_trans,
                R.id.help_op_list,
                R.id.help_login,
                R.id.help_general

        };



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

                fillContent();

//                fillViews(base_layout);
                setAttributes();

//                tuneSizes();
            }
        });

    }

    private void fillContent() {
        switch(this.help_id) {
            case R.id.help_trouble:

                break;
            case R.id.help_colors:

                break;
            case R.id.help_buttons:

                break;
            case R.id.help_url:

                break;
            case R.id.help_language:

                break;
            case R.id.help_view_trans:

                break;
            case R.id.help_per_trans:

                break;
            case R.id.help_op_list:

                break;
            case R.id.help_login: // start from here

                break;
            case R.id.help_general:

                break;


        }


    }

    private void setAttributes() {
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
        String base_http = attr.getBaseUrl();

        url_logout       = base_http + "logout/?";
        url_logout      += "id=" + params.getId() + "&companyID=" + params.getcompanyID();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

}
