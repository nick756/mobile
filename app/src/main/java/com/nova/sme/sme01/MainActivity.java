package com.nova.sme.sme01;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.nova.sme.sme01.miscellanea.FileManager;
import com.nova.sme.sme01.miscellanea.MyDialog;
import com.nova.sme.sme01.miscellanea.Parameters;
import com.nova.sme.sme01.miscellanea.Select_Language;
import com.nova.sme.sme01.miscellanea.Vocabulary;
import com.nova.sme.sme01.xml_reader_classes.GetOperations;
import com.nova.sme.sme01.xml_reader_classes.Operator;
import com.nova.sme.sme01.xml_reader_classes.XML_Login;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.xml.SimpleXmlHttpMessageConverter;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

import static java.sql.DriverManager.println;

/*
 **************************************************
 *                                                *
 *   MainActivity provides login's functionality  *
 *                                                *
 **************************************************
 */

public class MainActivity extends AppCompatActivity /*implements View.OnClickListener */{//AppCompatActivity
    public static String  MAIN_INFO;
    public static String  LANGUAGE = "EN";// "MY"

    private android.widget.RelativeLayout base_layout;
    public  static boolean 		          first_entry  = true;
    private static int                    autorotation = 1;

    private EditText                      user_name;
    private EditText                      password;
    private PlaceViews                    pv;
    private FormResizing                  FR;
    private Vocabulary                    voc;// = new Vocabulary();
    private Parameters                    params;
    private FileManager                   FM;
    private MyDialog                      my_dialog;
    private String                        url_logout;
    private String                        base_url_logout      = "http://103.6.239.242/sme/mobile/logout/?";

    private String                        base_url = "http://103.6.239.242:80/sme/mobile/login/?";//name=vlad&passw=1234";
//    private String                        base_url = "http://667.6.239.242:8080/sme/mobile/login/?";//name=vlad&passw=1234";

    private String                        login_request;
    private String                        debug_request = "http://103.6.239.242:80/sme/mobile/login/?name=vlad&passw=1234";

    private boolean                       block_login_button   = false;
    private String                        params_file_name     = "parameters.bin";
    private String                        operations_list_name = "operations_list.bin";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        try {
            setContentView(R.layout.activity_main);
        } catch(Exception err) {
            println(err.getMessage().toString());//android.util.AndroidRuntimeException: You cannot combine custom titles with other title features
        }

        this.setTitle("SME Cashflow Management System");

        if (first_entry) {// try to put this on "onStart"
            if (Settings.System.getInt(getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 0) == 1)
                autorotation = 1;
            else
                autorotation = 0;

            first_entry = false;
        }
        setAutoOrientationEnabled(0);
        super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

         // to hide keyboard, that appeares without our permiossion as default
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        base_layout = (android.widget.RelativeLayout) findViewById(R.id.base_layout);

        FR        = new FormResizing(this, base_layout);
        pv        = new PlaceViews(this, base_layout);
        user_name = pv.getUserName();
        password  = pv.getPassword();

        MAIN_INFO = getApplicationContext().getPackageName();

        voc       = new Vocabulary();
        FM        = new FileManager(this);

        params = (Parameters) FM.readFromFile(params_file_name);
        if (params == null)
            params = new Parameters();

        voc.setLanguage(params.getLanguage());

        my_dialog        = new MyDialog(voc, base_layout);
        this.url_logout  = this.base_url_logout +  "id=" + this.params.getId() + "&companyID=" + this.params.getcompanyID();


        ViewTreeObserver vto = base_layout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {
            base_layout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            FR.resize();
            pv.set_parent_margins(FR.get_width_margin(), FR.get_height_margin());
            pv.Placing();

            voc.TranslateAll(base_layout);
             }
        });
    }

    private void getParams() {
        this.params = (Parameters) FM.readFromFile(params_file_name);
        if (this.params == null)
            this.params = new Parameters();
        else
            voc.setLanguage(params.getLanguage());
    }

    @Override
    protected void onResume() {
        getParams();
        voc.TranslateAll(base_layout);
        super.onResume();
    }


    public void clickLoginButton(View v) {
        if (block_login_button) return;
        String user = user_name.getText().toString();
        String pswd = password.getText().toString();

        if (user.length() > 0 && pswd.length() > 0) {
            this.login_request = this.base_url + "name=" + user + "&passw=" + pswd;

            block_login_button = true;
            password.setText("");
            user_name.setText("");
            new HttpRequestTask().execute();
        } else { // debugging, temporarily
            this.login_request = this.base_url + "name=andrea&passw=1234";
            block_login_button = true;
            password.setText("");
            user_name.setText("");
            new HttpRequestTask().execute();
         }
    }

    private class HttpRequestTask extends AsyncTask<Void, String, XML_Login> {
        @Override
        protected XML_Login doInBackground(Void... params) {
            String error;

            XML_Login xml_login;
            URI uri;// = new URI(login_request);
            try {
                uri = new URI(login_request);

                RestTemplate restTemplate = new RestTemplate();
                StringHttpMessageConverter converter = new StringHttpMessageConverter();
                restTemplate.getMessageConverters().add(converter);

                String xml            = restTemplate.getForObject(uri, String.class);
                Serializer serializer = new Persister();//new Format("<?xml version=\"1.0\" encoding=\"utf-8\" ?>"));
                SimpleXmlHttpMessageConverter xml_converter = new SimpleXmlHttpMessageConverter(serializer);

                xml_login = serializer.read(XML_Login.class, xml);

                return xml_login;
            } catch (java.net.URISyntaxException e) {
                error = e.getMessage();
                Log.e("MainActivity", error, e);
            } catch (RestClientException e){
                error = e.getMessage();
                Log.e("MainActivity", error, e);
            } catch (Exception e) {
                error = e.getMessage();
                Log.e("MainActivity", error, e);
            }
            xml_login = new XML_Login(error);

            return xml_login;
        }


        @Override
        protected void onPostExecute(XML_Login xml_login) {
            block_login_button = false;
            if (xml_login == null) {
                // todo something
                return;
            }
            // debugging for
//            params.getFromXML(xml_login);
//            params.setLangauge(voc.getLanguage());
//            FM.writeToFile(params_file_name, params);

            String code       = xml_login.getCode();
            String id         = xml_login.getId();
            String originator = xml_login.getOriginator();
            String descr      = xml_login.getDescription();

            Operator operator = xml_login.getOperator();
            String name      = "no data";
            String role      = "no data";
            String company   = "no data";
            String companyID = "no data";

            if (operator != null) {
                name       = operator.getName();
                role       = operator.getRole();
                company    = operator.getCompany();
                companyID  = operator.getCompanyID();
            }

            Intent      resultIntent;
            CommonClass c_c;

            String error_message;
            try {
                // error?
                if (code.equals("0")) {
                    if (isFirstLogin()) {
                        resultIntent = new Intent(base_layout.getContext(), RegularLoginActivity.class);
                    } else {
                        // check indentity
                        if (checkIndentity(xml_login)) {
                            resultIntent = new Intent(base_layout.getContext(), RegularLoginActivity.class);
                        } else {
                            String msg = "Only a single Company (Business) can be managed from Mobile Application. If a User needs to manage more than one company using smart phone, two login names must be used, and each time re-synchronization to be performed.";
                            my_dialog.show(msg);
                            return;
                        }
                    }
                    writeParameters(xml_login);
                    c_c               = new CommonClass(code, id, originator, descr, name, role, company, companyID);
                    c_c.curr_language = voc.getLanguage();
                    resultIntent.putExtra(MainActivity.MAIN_INFO, c_c);
                    startActivity(resultIntent);
                } else {// everything is ok
                    if (code.equals("1"))
                        error_message = "User's role is not supported for mobile device";
                    else if (code.equals("2"))
                        error_message = "Authentication failed";// (user name or password is not found)";
                    else if (code.equals("3"))
                        error_message = "Timeout is over";
                    else
                        error_message = "Unknown error";

                    my_dialog.show(error_message);
                }
            } catch(Exception err) {
                println (err.getMessage().toString());
            }
        }
    }

    private boolean isFirstLogin() {
        // criteria is if we have operations list
        GetOperations local = (GetOperations) FM.readFromFile("operations_list.bin");
        if (local == null)
            return true;
        return local.getOperationsList().size() == 0;

    }

    private boolean checkIndentity(XML_Login xml_login) {
        String companyID       = xml_login.getOperator().getCompanyID();
        String inner_companyId = this.params.getcompanyID();

        return companyID.equals(inner_companyId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            new Select_Language(base_layout, voc, FM, params, null, params_file_name);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        params.setLangauge(voc.getLanguage());
        setAutoOrientationEnabled(autorotation);
        super.onStop();
    }
    void writeParameters(XML_Login xml_login) {
        this.params.setLangauge(voc.getLanguage());
        this.params.getFromXML(xml_login);
        FM.writeToFile(params_file_name, this.params);
    }
    void writeParameters() {
        this.params.setLangauge(voc.getLanguage());
        FM.writeToFile(params_file_name, this.params);
    }

    private void setAutoOrientationEnabled(int enabled) {
        try {
            Settings.System.putInt(getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, enabled);
        } catch(Exception err) {
            System.out.println(err.getMessage());
        }
    }

}
