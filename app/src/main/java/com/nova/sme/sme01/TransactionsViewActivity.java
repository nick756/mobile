package com.nova.sme.sme01;

import android.content.pm.ActivityInfo;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.nova.sme.sme01.miscellanea.CreateCustomBar;
import com.nova.sme.sme01.miscellanea.CustomAdapter;
import com.nova.sme.sme01.miscellanea.FileManager;
import com.nova.sme.sme01.miscellanea.FillWithOperationsList;
import com.nova.sme.sme01.miscellanea.FillWithTransactionsList;
import com.nova.sme.sme01.miscellanea.Http_Request_Logout;
import com.nova.sme.sme01.miscellanea.Parameters;
import com.nova.sme.sme01.miscellanea.Select_Language;
import com.nova.sme.sme01.miscellanea.Vocabulary;
import com.nova.sme.sme01.xml_reader_classes.Transactions;

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
    private Transactions                  xml_transactions;

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
        this.FM      = new FileManager(this);
        this.FR      = new FormResizing(this, base_layout);
        this.voc     = new Vocabulary();

        getParams();
        this.url_logout += "id=" + this.params.getId() + "&companyID=" + this.params.getcompanyID();

        this.xml_transactions = (Transactions) FM.readFromFile("transactions_view.bin");

        CommonClass c_c = (CommonClass)getIntent().getSerializableExtra(MainActivity.MAIN_INFO);

        TextView tv;
        tv = (TextView)findViewById(R.id.tv_date_from_id);
        tv.setText(c_c.dateFrom);
        tv = (TextView)findViewById(R.id.tv_date_till_id);
        tv.setText(c_c.dateTill);

        ViewTreeObserver vto = base_layout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {
                base_layout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                FR.resize();

                logout_button = create_custom_bar();
                voc.change_caption(logout_button);
                logout_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        logout_request();
                    }
                });

                FR.resizeLoginButton(base_layout, logout_button, 0.062f);
                fill_transactions_list();
                voc.TranslateAll(base_layout);
            }
        });


    }

    private void fill_transactions_list() {
        FillWithTransactionsList fwol =  new FillWithTransactionsList(this, this.xml_transactions, R.id.tv_list_transactions, voc, base_layout);
        // something else ?
    }


    private void logout_request() {
        new Http_Request_Logout(this, this.url_logout, this.FM, this.voc, this.base_layout, false);
    }
    private Button create_custom_bar() {
        return  (new CreateCustomBar(this, base_layout)).getButton();
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
