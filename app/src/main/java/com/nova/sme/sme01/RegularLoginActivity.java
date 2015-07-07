package com.nova.sme.sme01;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;


public class RegularLoginActivity extends AppCompatActivity {
    private TextView                      user;
    private TextView                      company_name;
    private TextView                      originator;
    private TextView                      role;
    private android.widget.RelativeLayout base_layout;
    private FormResizing                  FR;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regular_login);

        CommonClass c_c = (CommonClass)getIntent().getSerializableExtra(MainActivity.MAIN_INFO);

        base_layout  = (android.widget.RelativeLayout) findViewById(R.id.base_layout_reg);
        user         = (TextView) findViewById(R.id.reg_user_name_id);
        company_name = (TextView) findViewById(R.id.reg_company_name_id);
        originator   = (TextView) findViewById(R.id.reg_originator_id);
        role         = (TextView) findViewById(R.id.reg_role_id);

        user.setText(c_c.name);
        company_name.setText(c_c.company);
        originator.setText(c_c.originator);
        role.setText(c_c.role);

        FR = new FormResizing(this, base_layout);

        ViewTreeObserver vto = base_layout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {
                base_layout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                FR.resize_n();
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
