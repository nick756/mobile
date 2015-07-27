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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nova.sme.sme01.miscellanea.CreateCustomBar;
import com.nova.sme.sme01.miscellanea.FileManager;
import com.nova.sme.sme01.miscellanea.FillWithOperationsList;
import com.nova.sme.sme01.miscellanea.MyDialog;
import com.nova.sme.sme01.miscellanea.MyHttpRequest;
import com.nova.sme.sme01.miscellanea.Parameters;
import com.nova.sme.sme01.miscellanea.Select_Language;
import com.nova.sme.sme01.miscellanea.Vocabulary;
import com.nova.sme.sme01.xml_reader_classes.ListOperations;

import java.util.Vector;

import static java.sql.DriverManager.println;

/*
 **************************************************
 *                                                *
 *   Regular login implies that company ID is     *
 *   already saved locally (smart phone is bound  *
 *   with a company). Upon successful login,      *
 *   mobile application shows screen with         *
 *   options (main menu):                         *
 *                                                *
 **************************************************
 */

public class RegularLoginActivity extends AppCompatActivity {
    private TextView                      user;
    private TextView                      company_name;
    private TextView                      role;
    private RelativeLayout                base_layout;
    private LinearLayout                  buttons_set;
    private FormResizing                  FR;
    private Vocabulary                    voc;
    private Vector<Button>                bt_vector = new <Button>Vector();
    private ListOperations operaions_list;
    private FileManager                   FM;
    private String                        url_logout      = "http://103.6.239.242/sme/mobile/logout/?";
    private String                        url_request     = "http://103.6.239.242/sme/mobile/getoperations/?";
    private MyDialog                      my_dialog;
    private String                        sender = "";
    private boolean                       block_button;
    private Parameters                    params               = new Parameters();
    private String                        params_file_name     = "parameters.bin";
    private String                        operations_list_name = "operations_list.bin";
    private Button                        logout_button;


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
        setContentView(R.layout.regular_login);

        CommonClass c_c = (CommonClass)getIntent().getSerializableExtra(MainActivity.MAIN_INFO);// do we need this? we can read info from the file actually
        this.params.getFromCommonClass(c_c);
        this.sender     = c_c.sender;
        url_logout  += "id=" + c_c.id + "&companyID=" + c_c.companyID;
        url_request += "id=" + c_c.id + "&companyID=" + c_c.companyID;

        base_layout  = (android.widget.RelativeLayout) findViewById(R.id.base_layout_regular);
        user         = (TextView) findViewById(R.id.reg_user_name_id);
        company_name = (TextView) findViewById(R.id.reg_company_name_id);
        role         = (TextView) findViewById(R.id.reg_role_id);

        user.setText(c_c.name);
        company_name.setText(c_c.company);
        role.setText(c_c.role);

        FR = new FormResizing(this, base_layout);
        voc = new Vocabulary();
        voc.setLanguage(c_c.curr_language);

//        this.setTitle(voc.change_caption("Regular Login"));
        buttons_set = (LinearLayout) findViewById(R.id.reg_buttons_set);

        View view;
        String type, bt = new String("Button");

        for (int i = 0; i < buttons_set.getChildCount(); i ++) {//android.support.v7.widget.AppCompatButton
            view = buttons_set.getChildAt(i);
            type = view.getClass().getName();

            if (type.substring(type.length() - bt.length()).equals("Button")) {
                voc.change_caption((Button) view);
                bt_vector.add((Button)view);
            }
        }

        FM                  = new FileManager(this);
        this.operaions_list = (ListOperations) FM.readFromFile("operations_list.bin");
 //       my_dialog = new MyDialog(this.FR, voc, base_layout);

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

                FR.resizeRegularLogins(base_layout, bt_vector, logout_button, 0.062f);// height's button/total_height

                fill_operation_list();
                FR.resizeOperationListTemplate(R.id.reg_op_list_scrollView, 0.062f);

                my_dialog = new MyDialog(FR, voc, base_layout);
            }
        });

    }
    private void fill_operation_list() { // if empty - we hide some buttons
        FillWithOperationsList fwol =  new FillWithOperationsList(this, this.operaions_list, R.id.reg_op_list_scrollView, voc, base_layout);
        if (!fwol.implement()) {
            // hide some buttons
            Button button;
            for (int i = 0; i < bt_vector.size(); i ++) {
                button = bt_vector.get(i);
                if (button.getId() != R.id.synch_oper_list)
                    button.setVisibility(View.GONE);
            }
        }
    }
    private Button create_custom_bar() {
        Button button = (new CreateCustomBar(this, base_layout)).getButton();
        if (button != null)
            voc.change_caption(button);

        return button;
    }

    private void logout_request() {
        new MyHttpRequest(this.FR, this, base_layout, voc, url_request, "BaseXML");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_regular_login, menu);
        return true;
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
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_language) {
            new Select_Language(base_layout, voc, FM, params, logout_button, params_file_name);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void  writeParameters() {
        this.params.setLangauge(voc.getLanguage());
        FM.writeToFile(params_file_name, this.params);
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

    public void clickButton(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.perform_transaction:
                startActivity(new Intent(base_layout.getContext(), TransactionActivity.class));
                break;
            case R.id.view_transactions: {
                //http://103.6.239.242/sme/mobile/listtransactions/?id=3&companyID=1&dateFrom=21/07/2015&dateTill=21/07/2015
                String http = "http://103.6.239.242/sme/mobile/listtransactions/?id=" + this.params.getId();
                GetFilterViewTransactions get_transactions;
                get_transactions = new GetFilterViewTransactions(this, this.voc, this.base_layout, this.FR, http);
            }
                break;
            case R.id.synch_oper_list:
                this.operaions_list = null;
                fill_operation_list();
                new MyHttpRequest(this.FR,  this, base_layout, voc, url_request, "ListOperations");
                break;
            case R.id.reset_oper_list:
                // delete all
                FM.deleteFile(this.params_file_name);
                FM.deleteFile(this.operations_list_name);
                finish();
                break;
        }
    }
    public void passFunction(ListOperations xml_operation_list) {
        if (lock_list(xml_operation_list)) {
            for (int i = 0; i < bt_vector.size(); i ++)
                bt_vector.get(i).setVisibility(View.VISIBLE);
            this.operaions_list = xml_operation_list;
            fill_operation_list();
        }
    }

    private boolean lock_list(ListOperations xml_operation_list) {
        String        confirmed_message = "";
        ListOperations local;
        boolean       success = true;

        if (FM.writeToFile("parameters.bin", this.params)) {
            if (!FM.writeToFile("operations_list.bin", xml_operation_list))
                confirmed_message = "Error of saving Operations List";
        } else {
            success           = false;
            confirmed_message = "Error of saving Operations List";
        }
        if (!success)
            my_dialog.show(confirmed_message);

        return success;
    }


    @Override
    public void onBackPressed() {
        // forbid returning to the first page
    }
}
