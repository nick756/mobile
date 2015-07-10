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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nova.sme.sme01.miscellanea.FileManager;
import com.nova.sme.sme01.miscellanea.FillWithOperationsList;
import com.nova.sme.sme01.miscellanea.Http_Request_Logout;
import com.nova.sme.sme01.miscellanea.Vocabulary;
import com.nova.sme.sme01.transactions.GetOperations;

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
    private GetOperations                 operaions_list;
    private FileManager                   FM;
    private String                        url_logout      = "http://103.6.239.242/sme/mobile/logout/?";

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
        url_logout  += "id=" + c_c.id + "&companyID=" + c_c.companyID;

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

        this.setTitle(voc.change_caption("Regular Login"));
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
        this.operaions_list = (GetOperations) FM.readFromFile("operations_list.bin");


        ViewTreeObserver vto = base_layout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {
            base_layout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            FR.resize();
            FR.resizeFirstRegularLogins(base_layout, bt_vector, 0.062f);// height's button/total_height
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_regular_login, menu);
        return true;
    }

    public void clickButton(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.perform_transaction:

                break;
            case R.id.view_transactions:

                break;
            case R.id.synch_oper_list:
                FillWithOperationsList fwl = new FillWithOperationsList(this, this.operaions_list, R.id.reg_op_list_scrollView, voc, base_layout);
                break;
            case R.id.reg_log_out_id:
                new Http_Request_Logout(this, this.url_logout, this.FM, this.voc, this.base_layout, true);
                break;
        }
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
