package com.nova.sme.sme01;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
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
import com.nova.sme.sme01.miscellanea.Parameters;
import com.nova.sme.sme01.miscellanea.Vocabulary;
import com.nova.sme.sme01.xml.xmllogin.Operator;
import com.nova.sme.sme01.xml.xmllogin.XML_Login;
import com.nova.sme.sme01.xml.xmllogin.XML_Login_Activity;

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

public class MainActivity extends AppCompatActivity {//AppCompatActivity
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

    private String                        base_url = "http://103.6.239.242:8080/sme/mobile/login/?";//name=vlad&passw=1234";
//    private String                        base_url = "http://667.6.239.242:8080/sme/mobile/login/?";//name=vlad&passw=1234";

    private String                        login_request;
    private String                        debug_request = "http://103.6.239.242:8080/sme/mobile/login/?name=vlad&passw=1234";

    private boolean                       block_login_button = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

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

        //SME Cashflow Management System

        // to hide keyboard, that appeares without our permiossion as default
       this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

//        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build(); // to see later
//        StrictMode.setThreadPolicy(policy);

        base_layout = (android.widget.RelativeLayout) findViewById(R.id.base_layout);

        FR        = new FormResizing(this, base_layout);
        pv        = new PlaceViews(this, base_layout);
        user_name = pv.getUserName();
        password  = pv.getPassword();

        MAIN_INFO = getApplicationContext().getPackageName();

        voc       = new Vocabulary();
        params    = new Parameters();
        FM        = new FileManager(this);

        FM.readData(params);
        voc.setLanguage(params.getLanguage());



        ViewTreeObserver vto = base_layout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {
                base_layout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                FR.resize_n();
                pv.set_parent_margins(FR.get_width_margin(), FR.get_height_margin());
                pv.Placing();

                set_new_language();
            }
        });
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
        }
    }
//http://103.6.239.242:8080/sme/mobile/login/?name=andrea&passw=1234
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

//                String xml = restTemplate.getForObject(login_request, String.class, "SpringSource");
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
            params.getFromXML(xml_login);
            params.setLangauge(voc.getLanguage());
            FM.writeData(params);
            FM.readData(params);

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
                    resultIntent = new Intent(base_layout.getContext(), XML_Login_Activity.class);
                    c_c          = new CommonClass(code, id, originator, descr, name, role, company, companyID);
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

                    final Dialog dialog = new Dialog(base_layout.getContext());
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.login_failed_layout);
                    TextView text = (TextView) dialog.findViewById(R.id.dialog_text);
                    text.setText(error_message);

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
            } catch(Exception err) {
                println (err.getMessage().toString());
            }
        }
    }//java.lang.RuntimeException: Parcelable encountered IOException writing serializable object (name = com.nova.sme.sme01.CommonClass)


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu (Menu menu) {
        MenuItem item = menu.findItem(R.id.action_language);
        if (menu != null)
            voc.change_caption(item);

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_language) {

            final Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.activity_select__language);

            final TextView text     = (TextView)    dialog.findViewById(R.id.select_language);
            final RadioButton en_rb = (RadioButton) dialog.findViewById(R.id.EngRB);
            final RadioButton my_rb = (RadioButton) dialog.findViewById(R.id.MalayRB);

            voc.change_caption(text);
            voc.change_caption(en_rb);
            voc.change_caption(my_rb);

            Button dialogButton = (Button) dialog.findViewById(R.id.ok_select_language);
            voc.change_caption(dialogButton);

            if (voc.getLanguage().equals("EN"))
                en_rb.setChecked(true);
            else
                my_rb.setChecked(true);

             dialogButton.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     if (en_rb.isChecked())
                         voc.setLanguage("EN");
                     else
                         voc.setLanguage("MY");

                     set_new_language();
                     dialog.dismiss();
                 }
             });

            dialog.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void set_new_language() {
         try {
            voc.change_caption(pv.getUserName());
            voc.change_caption(pv.getPassword());
            voc.change_caption(pv.getCaption());
            voc.change_caption(pv.getLoginButton());
        } catch(Exception err) {

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        params.setLangauge(voc.getLanguage());
        FM.writeData(params); // here temporarily, in sake of test

        setAutoOrientationEnabled(autorotation);
        super.onStop();
    }
/*
    @Override
    public void onDestroy()
    {
        super.onDestroy();
        setAutoOrientationEnabled(autorotation);
    }
*/

    private void setAutoOrientationEnabled(int enabled) {
        try {
            Settings.System.putInt(getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, enabled);
        } catch(Exception err) {
            System.out.println(err.getMessage());
        }
    }

}
