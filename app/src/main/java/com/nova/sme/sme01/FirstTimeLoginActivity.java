package com.nova.sme.sme01;

import android.app.Dialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
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
import android.widget.ScrollView;
import android.widget.TextView;

import com.nova.sme.sme01.miscellanea.FileManager;
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
import java.util.List;
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

public class FirstTimeLoginActivity extends AppCompatActivity {

    private TextView                      user;
    private TextView                      company_name;
//    private TextView                      originator;
    private TextView                      role;
    private RelativeLayout                base_layout;
//    private LinearLayout                  base_layout;//base_layout_first_n;
    private LinearLayout                  sub_base;
    private FormResizing                  FR;
    private Vocabulary                    voc;
    private Vector<Button>                bt_vector = new <Button>Vector();

    private boolean                       block_login_button = false;
    private String                        url_request        = "http://103.6.239.242/sme/mobile/getoperations/?";
    private GetOperations                 operaions_list;
    private FileManager                   FM;

//    private String                        id;
//    private String                        companyID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_first_time_login);
        super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        FM = new FileManager(this);

        CommonClass c_c = (CommonClass)getIntent().getSerializableExtra(MainActivity.MAIN_INFO);

        url_request += "id=" + c_c.id + "&companyID=" + c_c.companyID;

        base_layout  = (android.widget.RelativeLayout) findViewById(R.id.base_layout_first);

        user         = (TextView) findViewById(R.id.user_name_id);
        company_name = (TextView) findViewById(R.id.company_name_id);
//        originator   = (TextView) findViewById(R.id.originator_id);
        role         = (TextView) findViewById(R.id.role_id);

        user.setText(c_c.name);
        company_name.setText(c_c.company);
//        originator.setText(c_c.originator);
        role.setText(c_c.role);

        FR  = new FormResizing(this, base_layout);
        voc = new Vocabulary();
        voc.setLanguage(c_c.curr_language);

        this.setTitle(voc.change_caption("First Time Login"));

        sub_base = (LinearLayout) findViewById(R.id.sub_base_id);

        View view;
        String type, bt = new String("Button");

        for (int i = 0; i < sub_base.getChildCount(); i ++) {//android.support.v7.widget.AppCompatButton
            view = sub_base.getChildAt(i);
            type = view.getClass().getName();

            if (type.substring(type.length() - bt.length()).equals("Button")) {
                voc.change_caption((Button) view);
                bt_vector.add((Button)view);
            }
        }

        ViewTreeObserver vto = base_layout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {
            base_layout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            FR.resize_n();
//            FR.resizeFirstRegularLogins(base_layout, sub_base, bt_vector, 0.092f);// height's button/total_height
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_first_time_login, menu);
        return true;
    }

    public void clickButton(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.sopl_id:
                if (block_login_button)
                    return;
                block_login_button  = true;
                this.operaions_list = null;
                new HttpRequestTask().execute();
                break;
            case R.id.lock_company_id:
                if (this.operaions_list != null)
                    lock_list();
                break;
            case R.id.log_out_id:

                break;
         }
    }

    private class HttpRequestTask extends AsyncTask<Void, String, GetOperations> {
        @Override
        protected GetOperations doInBackground(Void... params) {
            String error;

            GetOperations xml_operaions_list;
            URI uri;
            try {
                uri = new URI(url_request);

                RestTemplate restTemplate = new RestTemplate();
                StringHttpMessageConverter converter = new StringHttpMessageConverter();
                restTemplate.getMessageConverters().add(converter);

                String xml            = restTemplate.getForObject(uri, String.class);
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
        //Element 'operation' does not have a match in class com.nova.sme.sme01.transactions.SupportedOperations at line 1

        @Override
        protected void onPostExecute(GetOperations xml_operation_list) {
            block_login_button = false;
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
            if (code.equals("1"))
                error = "Mismatching Company ID";
            else if (code.equals("2"))
                error = "Authentication failed";
            else if (code.equals("3"))
                error = "Session expired";
            else
                error = code + " - unknown error";

            final Dialog dialog = new Dialog(base_layout.getContext());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.login_failed_layout);
            TextView text = (TextView) dialog.findViewById(R.id.dialog_text);
            text.setText(error);

            Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);

            voc.change_caption(text);
            voc.change_caption(dialogButton);

            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.show();

            return;
        }


        this.operaions_list = new GetOperations(xml_operation_list);

        ArrayList<Operation> list;
        int                  size;

        String ss;
        try {
            list = this.operaions_list.getOperationsList();

            if (list == null) {
                // do something
                return;
            }
            LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);

            size = list.size();
            LinearLayout sv = (LinearLayout) findViewById(R.id.op_list_scrollView);
            sv.removeAllViews();
            Operation operation;

            TextView  code_operation = (TextView)  findViewById(R.id.code_operation);
            TextView  name_operation = (TextView)  findViewById(R.id.name_opeartion);
            TextView  type_operation = (TextView)  findViewById(R.id.type_operation);
            ImageView inbound        = (ImageView) findViewById(R.id.inbound_icon);
            ImageView outbound       = (ImageView) findViewById(R.id.out_bound_icon);
            for (int i = 0; i < size; i ++) {
                operation = list.get(i);
                if (operation.getInbound().equals("true")) {
//                    inbound.setBackgroundResource(R.id);
                } else {

                }
                if (operation.getOutbound().equals("true")) {

                } else {

                }
                code_operation.setText(operation.getCode());
                name_operation.setText(operation.getName());
                type_operation.setText(operation.getType());


                LinearLayout ll = (LinearLayout) inflater.inflate(R.layout.operations_template, null);
                sv.addView(ll);
            }
            //@mipmap/ic_checked
            //@mipmap/ic_uncheck
            /*
            out_bound_icon
            inbound_icon
            code_operation
            name_opeartion
            type_operation
             */
        } catch(Exception err) {
            println (err.getMessage().toString());//java.lang.IllegalStateException: ScrollView can host only one direct child
        }
    }

    private void lock_list() {
        GetOperations local;

       if (FM.writeToFile("operations_list.bin", this.operaions_list)) {
           local = (GetOperations) FM.readFromFile("operations_list.bin");

           // compare
           if (!local.equals(this.operaions_list))
               println("error");

       } else {
           // error writing a file

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
