package com.nova.sme.sme01;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nova.sme.sme01.miscellanea.Dialogs.AboutDialog;
import com.nova.sme.sme01.miscellanea.ApplicationAttributes;
import com.nova.sme.sme01.miscellanea.Dialogs.ColorsDialog;
import com.nova.sme.sme01.miscellanea.CustomBar;
import com.nova.sme.sme01.miscellanea.FileManager;
import com.nova.sme.sme01.miscellanea.FillWithTransactionsList;
import com.nova.sme.sme01.miscellanea.Dialogs.HttpDialog;
import com.nova.sme.sme01.miscellanea.MyColors;
import com.nova.sme.sme01.miscellanea.MyHttpRequest;
import com.nova.sme.sme01.miscellanea.Parameters;
import com.nova.sme.sme01.miscellanea.Dialogs.Select_Language;
import com.nova.sme.sme01.miscellanea.Dialogs.ThemesDialog;
import com.nova.sme.sme01.miscellanea.Vocabulary;
import com.nova.sme.sme01.xml_reader_classes.ListTransactions;

import java.util.Vector;

import static java.sql.DriverManager.println;


public class TransactionsViewActivity extends AppCompatActivity {
    private Vocabulary                    voc;
    private FormResizing                  FR;
    private RelativeLayout                base_layout;
    private FileManager                   FM;
    private Button                        logout_button;
    private Parameters                    params;
    private String                        params_file_name     = "parameters.bin";
    private String                        operations_list_name = "operations_list.bin";

    private String                        url_logout      = "http://103.6.239.242/sme/mobile/logout/?";
    private String                        base_http;

    private ListTransactions xml_List_transactions;
    private FillWithTransactionsList      fwt;

    private Vector<View> views = new Vector<View>();
    private CustomBar ccb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            //           requestWindowFeature(Window.FEATURE_NO_TITLE);
        } catch(Exception err) {
            println(err.getMessage().toString());
        }
        super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_transactions_view);

        base_layout  = (android.widget.RelativeLayout) findViewById(R.id.tv_base_layout);
        base_layout.setTag("main_background_color");
        this.FM      = new FileManager(this);
        this.FR      = new FormResizing(this, base_layout);
        this.voc     = new Vocabulary();

        getParams();
 //       this.url_logout += "id=" + this.params.getId() + "&companyID=" + this.params.getcompanyID();

        this.xml_List_transactions = (ListTransactions) FM.readFromFile("transactions_view.bin");

        CommonClass c_c = (CommonClass)getIntent().getSerializableExtra(MainActivity.MAIN_INFO);

        TextView tv;
        tv = (TextView)findViewById(R.id.tv_date_from_id);
        tv.setText(c_c.dateFrom);
        tv = (TextView)findViewById(R.id.tv_date_till_id);
        tv.setText(c_c.dateTill);

        fwt =  new FillWithTransactionsList(this, this.xml_List_transactions, R.id.tv_list_transactions, voc, base_layout);
        int real_number = fwt.getActualTransactions();
        TextView t_v = (TextView)findViewById(R.id.trans_count);
        t_v.setText("(" + Integer.toString(real_number) + ")");

        ViewTreeObserver vto = base_layout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {
                base_layout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                FR.resize();

                logout_button = create_custom_bar();
//                voc.change_caption(logout_button);
                logout_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        logout_request();
                    }
                });

                FR.resizeLoginButton(base_layout, logout_button, 0.062f);
                voc.TranslateAll(base_layout);
//                fwt.setFontSize();

                 setAttributes();

            }
        });
    }
    public CustomBar getCustomBar(){return ccb;}

    public void UpdateCustomBar(){
        if (ccb != null)
            ccb.setBackgound();
    }


    public Vector<View> getViews() {return  views;}

    private void setAttributes() {
        ApplicationAttributes attr = (ApplicationAttributes)FM.readFromFile("attributes.bin");
        if (attr == null)
            attr = new ApplicationAttributes(this);
        attr.setButtons(base_layout, logout_button);

        attr.setButtons(base_layout, logout_button);
        MyColors colors = attr.getColors();
        colors.setColors(views);

        if (ccb != null)
            ccb.setBackgound();
    }

    private void updateURL() {
        ApplicationAttributes attr = (ApplicationAttributes)FM.readFromFile("attributes.bin");
        if (attr == null)
            attr = new ApplicationAttributes(this);

        base_http  = attr.getBaseUrl();
        url_logout = base_http + "logout/?" + "id=" + params.getId() + "&companyID=" + params.getcompanyID();
    }

    private void logout_request() {
        updateURL();
        new MyHttpRequest(this.FR, this, base_layout, voc, url_logout, "BaseXML");
    }
    private Button create_custom_bar() {
        ccb = new CustomBar(this, base_layout);

        Button button = ccb.getButton();
        if (button != null)
            voc.change_caption(button);

        views.add(base_layout);
        views.add(ccb.getBase());
        views.add(ccb.getTitle());

        return button;

//        return  (new CreateCustomBar(this, base_layout)).getButton();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_transactions_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_language) {
            new Select_Language(base_layout, voc, FM, params, logout_button, params_file_name);
            return true;
        } else if (id == R.id.action_themes) {
            new ThemesDialog(base_layout, voc, FM, logout_button).show();
            return true;
        } else if (id == R.id.action_url_address) {
            new HttpDialog(FR, voc, base_layout, logout_button).show();
            return true;
        } else if (id == R.id.colors_themes) {
            new ColorsDialog(this, base_layout, voc, FM, logout_button).show();
            return true;
        } else if (id == R.id.action_about) {
            new AboutDialog(FR, voc, base_layout, logout_button).show();
            return true;
        } else if (id == R.id.action_help) {
            startActivity(new Intent(this, HelpActivity.class));
            return true;
        }


        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onPrepareOptionsMenu (Menu menu) {
        MenuCaptions(menu);
        return super.onPrepareOptionsMenu(menu);
    }

    private void MenuCaptions(Menu menu) {
        MenuItem item;
        Menu     inner_menu;

        for (int i = 0; i < menu.size(); i ++) {
            item = menu.getItem(i);
            voc.change_caption(item);

            inner_menu = item.getSubMenu();
            if (inner_menu != null)
                MenuCaptions(inner_menu);
        }
    }
    @Override
    protected void onResume() {
        getParams();
        voc.TranslateAll(base_layout);
        if (logout_button != null)
            voc.change_caption(logout_button);

        setAttributes();
        super.onResume();
    }
    private void getParams() {
        this.params = (Parameters) FM.readFromFile(params_file_name);
        if (this.params == null)
            this.params = new Parameters();
        else
            voc.setLanguage(params.getLanguage());
    }


}
