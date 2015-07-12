package com.nova.sme.sme01;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nova.sme.sme01.miscellanea.BaseXML;
import com.nova.sme.sme01.miscellanea.CreateCustomBar;
import com.nova.sme.sme01.miscellanea.FileManager;
import com.nova.sme.sme01.miscellanea.FillWithOperationsList;
import com.nova.sme.sme01.miscellanea.Http_Request_Logout;
import com.nova.sme.sme01.miscellanea.MyDialog;
import com.nova.sme.sme01.miscellanea.Parameters;
import com.nova.sme.sme01.miscellanea.Vocabulary;
import com.nova.sme.sme01.transactions.GetOperations;
import com.nova.sme.sme01.transactions.Operation;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.xml.SimpleXmlHttpMessageConverter;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.ArrayList;
import java.util.Vector;

import static java.sql.DriverManager.println;

/*
 **************************************************
 *                                                *
 *   First time login assumes pre-registration    *
 *   of User and Business on the server side      *
 *   before any operations on the mobile          *
 *   application. MainActivity provides           *
 *   login's functionality                        *
 *                                                *
 **************************************************
 */

public class FirstTimeLoginActivity extends AppCompatActivity implements View.OnClickListener {//Activity

    private TextView                      user;
    private TextView                      company_name;
    private TextView                      role;
    private RelativeLayout                base_layout;
    private LinearLayout                  buttons_set;
    private FormResizing                  FR;
    private Vocabulary                    voc;
    private Vector<Button>                bt_vector = new <Button>Vector();

    private boolean                       block_button    = false;
    private String                        url_request     = "http://103.6.239.242/sme/mobile/getoperations/?";
    private String                        url_logout      = "http://103.6.239.242/sme/mobile/logout/?";
    private FileManager                   FM;
    private int                           selected_id;
    private Parameters                    params = new Parameters();
    private MyDialog                      my_dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
 //           requestWindowFeature(Window.FEATURE_NO_TITLE);
        } catch(Exception err) {
            println(err.getMessage().toString());
        }

        setContentView(R.layout.activity_first_time_login);
        super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        FM = new FileManager(this);

        CommonClass c_c = (CommonClass)getIntent().getSerializableExtra(MainActivity.MAIN_INFO);
        this.params.getFromCommonClass(c_c);

        url_request += "id=" + c_c.id + "&companyID=" + c_c.companyID;
        url_logout  += "id=" + c_c.id + "&companyID=" + c_c.companyID;

        base_layout  = (android.widget.RelativeLayout) findViewById(R.id.base_layout_first);

        user         = (TextView) findViewById(R.id.user_name_id);
        company_name = (TextView) findViewById(R.id.company_name_id);
        role         = (TextView) findViewById(R.id.role_id);

        user.setText(c_c.name);
        company_name.setText(c_c.company);
        role.setText(c_c.role);

        FR  = new FormResizing(this, base_layout);
        voc = new Vocabulary();
        voc.setLanguage(c_c.curr_language);

        this.setTitle(voc.change_caption("First Time Login"));

        buttons_set = (LinearLayout) findViewById(R.id.buttons_set);

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
        my_dialog = new MyDialog(voc, base_layout);

        ViewTreeObserver vto = base_layout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {
            base_layout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            FR.resize();
            FR.resizeFirstRegularLogins(base_layout, bt_vector, 0.062f);// height's button/total_height

            create_custom_bar();
            }
        });

    }
    private void create_custom_bar() {
        Button button = (new CreateCustomBar(this, base_layout)).getButton();
        if (button != null)
            button.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.logout_button) {
            new Http_Request_Logout(this, this.url_logout, this.FM, this.voc, this.base_layout, true);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_first_time_login, menu);
        return true;
    }

    public void clickButton(View view) {
        if (block_button)
            return;

        switch (view.getId()) {
            case R.id.synchronize_operation_list:
                block_button        = true;
                new HttpRequestTask().execute();
                break;
        }
    }

    private class HttpRequestTask extends AsyncTask<Void, String, GetOperations> {
        @Override
        protected GetOperations doInBackground(Void... params) {
            String error;

            GetOperations xml_operaions_list;
            String xml;//operationsList
            URI uri;
            try {
                uri = new URI(url_request);//http://103.6.239.242/sme/mobile/getoperations/?id=4&companyID=2 //andrea
                                           //http://103.6.239.242/sme/mobile/getoperations/?id=3&companyID=1
                RestTemplate restTemplate = new RestTemplate();
                StringHttpMessageConverter converter = new StringHttpMessageConverter();
                restTemplate.getMessageConverters().add(converter);

                xml                   = restTemplate.getForObject(uri, String.class);
                Serializer serializer = new Persister();//new Format("<?xml version=\"1.0\" encoding=\"utf-8\" ?>"));
                SimpleXmlHttpMessageConverter xml_converter = new SimpleXmlHttpMessageConverter(serializer);

                xml_operaions_list = serializer.read(GetOperations.class, xml);

                return xml_operaions_list;
            } catch (java.net.URISyntaxException e) {
                error = e.getMessage();
                Log.e("FirstTimeLoginActivity", error, e);
            } catch (RestClientException e){
                error = e.getMessage();
                Log.e("FirstTimeLoginActivity", error, e);
            } catch (Exception e) {
                error = e.getMessage();
                Log.e("FirstTimeLoginActivity", error, e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(GetOperations xml_operation_list) {
            block_button = false;
            if (xml_operation_list == null) {
                // todo something
                return;
            }

            implementXMLRespond(xml_operation_list);
        }
    }

    private void implementXMLRespond(GetOperations xml_operation_list) {
        String code = xml_operation_list.getCode();
        String error;
        if (!code.equals("0")) {
            if (code.equals("1")) {
                error = "Mismatching Company ID";
            } else if (code.equals("2")) {
                error = "Authentication failed";
            } else if (code.equals("3")) {
                error = "Session expired";
                finish();
            } else {
                error = code + " - unknown error";
            }

            my_dialog.show(error);
            return;
        }
        if (xml_operation_list.getOperationsList().size() == 0) {
            my_dialog.show("Operations List is empty");
            return;
        } else {
            if (lock_list(xml_operation_list)) {
                Intent resultIntent;
                resultIntent    = new Intent(base_layout.getContext(), RegularLoginActivity.class);
                CommonClass c_c = (CommonClass)getIntent().getSerializableExtra(MainActivity.MAIN_INFO);

                c_c.sender        = "FirstTimeLoginActive";
                c_c.curr_language = voc.getLanguage();
                resultIntent.putExtra(MainActivity.MAIN_INFO, c_c);
                startActivity(resultIntent);
             }
        }
    }

    private boolean lock_list(GetOperations xml_operation_list) {
        String        confirmed_message = "";
        GetOperations local;
        boolean       success = true;

        if (FM.writeToFile("parameters.bin", this.params)) {
            if (FM.writeToFile("operations_list.bin", xml_operation_list)) {
                // validating
                local = (GetOperations) FM.readFromFile("operations_list.bin");
                if (!local.equals(xml_operation_list)) {
                    confirmed_message = "Error of saving Operations List";
                    success = false;
                } else {
                    confirmed_message = "Operations List has been saved successfully";
                    success = true;
                }
            } else {
                confirmed_message = "Error of saving Operations List";

            }
        } else {
            success           = false;
            confirmed_message = "Error of saving Operations List";
        }
        if (!success)
            my_dialog.show(confirmed_message);

        return success;
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
