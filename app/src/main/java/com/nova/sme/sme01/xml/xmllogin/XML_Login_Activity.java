package com.nova.sme.sme01.xml.xmllogin;

import android.content.pm.ActivityInfo;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.nova.sme.sme01.CommonClass;
import com.nova.sme.sme01.MainActivity;
import com.nova.sme.sme01.R;

import java.util.HashMap;
import java.util.Map;

/*
 ***********************************************************
 *                                                         *
 *   XML_Login_Activity provides login procedure testing   *
 *                                                         *
 ***********************************************************
 */

public class XML_Login_Activity extends AppCompatActivity {

    private TextView code;
    private TextView id;// 3
    private TextView originator;
    private TextView descr;

//    Operator operator = xml_login.getOperator();
    private TextView name;
    private TextView role;
    private TextView company;
    private TextView companyID;

    Map responds = new HashMap();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.xml_login_info);

        super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        this.setTitle("XML Login Feedback");

        responds.put("0", " - Normal login");
        responds.put("1", " - The role of the user is not supported for the mobile interface");
        responds.put("2" ," - Authentication failed (user name or password is not found)");
        responds.put("3", " - Elapsed time out");

        CommonClass c_c = (CommonClass)getIntent().getSerializableExtra(MainActivity.MAIN_INFO);

        code       = (TextView) findViewById(R.id.xml_login_code);
        id         = (TextView) findViewById(R.id.xml_login_id);
        originator = (TextView) findViewById(R.id.xml_login_orididnator);
        descr      = (TextView) findViewById(R.id.xml_login_descr);
        name       = (TextView) findViewById(R.id.xml_login_name);
        role       = (TextView) findViewById(R.id.xml_login_role);
        company    = (TextView) findViewById(R.id.xml_login_company);
        companyID  = (TextView) findViewById(R.id.xml_login_company_id);

        try {
            code.setText(code.getText().toString() + " " + c_c.code + responds.get(c_c.code) );//0
            id.setText(id.getText().toString() + " " + c_c.id);// 3
            originator.setText(originator.getText().toString() + " " + c_c.originator);
            descr.setText(descr.getText().toString() + " " + c_c.descr);

            name.setText(name.getText().toString() + " " + c_c.name);
            role.setText(role.getText().toString() + " " + c_c.role);
            company.setText(company.getText().toString() + " " + c_c.company);
            companyID.setText(companyID.getText().toString() + " " + c_c.companyID);
        } catch (Exception err) {

        }
    }

    public void clickOkButton(View v) {
        this.finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_xml__login_, menu);
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
