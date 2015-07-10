package com.nova.sme.sme01;

import android.app.Dialog;
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
import android.widget.TextView;

import com.nova.sme.sme01.miscellanea.BaseXML;
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

public class FirstTimeLoginActivity extends AppCompatActivity {//Activity

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
    private GetOperations                 operaions_list;
    private FileManager                   FM;
    private int                           selected_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
 //           requestWindowFeature(Window.FEATURE_NO_TITLE);
        } catch(Exception err) {
            println(err.getMessage().toString());
        }
 //       final ActionBar bar = getSupportActionBar();
 //       bar.setDisplayShowTitleEnabled(false);

        setContentView(R.layout.activity_first_time_login);
        super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        FM = new FileManager(this);

        CommonClass c_c = (CommonClass)getIntent().getSerializableExtra(MainActivity.MAIN_INFO);

        url_request += "id=" + c_c.id + "&companyID=" + c_c.companyID;
        url_logout  += "id=" + c_c.id + "&companyID=" + c_c.companyID;

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
        getMenuInflater().inflate(R.menu.menu_first_time_login, menu);
        return true;
    }

    public void clickButton(View view) {
        if (block_button)
            return;

        switch (view.getId()) {
            case R.id.synchronize_operation_list:
                block_button        = true;
                this.operaions_list = null;
                new HttpRequestTask().execute();
                break;
            case R.id.lock_company:
                if (this.operaions_list == null) {
                    dialog("Operations List is empty");
                    break;
                }
                lock_list();
                break;
            case R.id.logout_id:
                block_button = true;
                new HttpRequestLogout().execute();
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
                uri = new URI(url_request);//http://103.6.239.242/sme/mobile/getoperations/?id=4&companyID=2
                                           //http://103.6.239.242/sme/mobile/getoperations/?id=3&companyID=1
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
            if (code.equals("1"))
                error = "Mismatching Company ID";
            else if (code.equals("2"))
                error = "Authentication failed";
            else if (code.equals("3"))
                error = "Session expired";
            else
                error = code + " - unknown error";

            dialog(error);
            return;
        }

        this.operaions_list = new GetOperations(xml_operation_list);

        ArrayList<Operation> list;
        try {
            list = this.operaions_list.getOperationsList();

            if (list == null) {
                dialog("Operations List is empty");
                return;
            }
            if (list.size() == 0) {
                dialog("Operations List is empty");
                return;
            }

            LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
            LinearLayout   sv       = (LinearLayout) findViewById(R.id.op_list_scrollView);
            sv.removeAllViews();

            for (int i = 0; i < list.size(); i ++) {
                LinearLayout ll = (LinearLayout) inflater.inflate(R.layout.operations_template, null);
                sv.addView(ll);
                setValues(ll, list.get(i));
            }
        } catch(Exception err) {
            println (err.getMessage().toString());
        }
    }

    void setValues(LinearLayout layout, Operation operation) {
        View         view;
        LinearLayout inner_layout;
        String       tag;
        TextView     text;
        ImageView    img;
        for (int i = 0; i < layout.getChildCount(); i ++) {
            inner_layout = (LinearLayout) layout.getChildAt(i);
            for (int j = 0; j < inner_layout.getChildCount(); j ++) {
                view = inner_layout.getChildAt(j);
                tag  = (String) view.getTag();
                if (tag != null) {
                    if (tag.equals("code")) {
                        text = (TextView) view;
                        text.setText(operation.getCode());
                    } else if (tag.equals("name")) {
                        text = (TextView) view;
                        text.setText(operation.getName());
                    } else if (tag.equals("inbound")) {
                        img = (ImageView) view;//R.drawable.someImageId
                        if (operation.getInbound().equals("true"))
                            img.setImageResource(R.mipmap.ic_checked);
                        else
                            img.setImageResource(R.mipmap.ic_uncheck);
                    } else if (tag.equals("outbound")) {
                        img = (ImageView) view;
                        if (operation.getOutbound().equals("true"))
                            img.setImageResource(R.mipmap.ic_checked);
                        else
                            img.setImageResource(R.mipmap.ic_uncheck);
                    } else if (tag.equals("type")) {
                        text = (TextView) view;
                        text.setText(operation.getType());
                    }
                }
            }
        }
    }

    private void lock_list() {
        String        confirmed_message;
        GetOperations local;
        if (this.operaions_list.getOperationsList().size() == 0) {
            dialog("Operations List is empty");
            return;
        }


        if (FM.writeToFile("operations_list.bin", this.operaions_list)) {
            // validating
            local = (GetOperations) FM.readFromFile("operations_list.bin");
            if (!local.equals(this.operaions_list))
                confirmed_message = "Error of saving Operations List";
            else
                confirmed_message = "Operations List has been saved successfully";
        } else {
           confirmed_message = "Error of saving Operations List";
        }

        dialog(confirmed_message);
    }

    private void dialog(String message) {
        final Dialog dialog = new Dialog(base_layout.getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.login_failed_layout);
        TextView text = (TextView) dialog.findViewById(R.id.dialog_text);
        text.setText(message);

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

    private class HttpRequestLogout extends AsyncTask<Void, String, BaseXML> {
        @Override
        protected BaseXML doInBackground(Void... params) {
            String error;

            BaseXML xml_logout;
            URI uri;
            try {
                uri = new URI(url_logout);

                RestTemplate restTemplate = new RestTemplate();
                StringHttpMessageConverter converter = new StringHttpMessageConverter();
                restTemplate.getMessageConverters().add(converter);

                String xml            = restTemplate.getForObject(uri, String.class);
                Serializer serializer = new Persister();
                SimpleXmlHttpMessageConverter xml_converter = new SimpleXmlHttpMessageConverter(serializer);

                xml_logout = serializer.read(BaseXML.class, xml);

                return xml_logout;
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
        protected void onPostExecute(BaseXML xml_logout) {
            block_button = false;
            if (xml_logout != null) {
                if (xml_logout.getCode().equals("0")) {
                    // erase the file and go to the first page
                    //"parameters.bin";
                    //operations_list.bin
                    FM.deleteFile("parameters.bin");
                    FM.deleteFile("operations_list.bin");
                    finish();
                }
            }
            dialog("Error occured");
        }
    }

}
