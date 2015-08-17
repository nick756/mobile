package com.nova.sme.sme01;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.nova.sme.sme01.miscellanea.Dialogs.AboutDialog;
import com.nova.sme.sme01.miscellanea.ApplicationAttributes;
import com.nova.sme.sme01.miscellanea.Dialogs.ColorsDialog;
import com.nova.sme.sme01.miscellanea.CustomBar;
import com.nova.sme.sme01.miscellanea.FileManager;
import com.nova.sme.sme01.miscellanea.Dialogs.HttpDialog;
import com.nova.sme.sme01.miscellanea.MyColors;
import com.nova.sme.sme01.miscellanea.Dialogs.MyDialog;
import com.nova.sme.sme01.miscellanea.MyHttpRequest;
import com.nova.sme.sme01.miscellanea.Parameters;
import com.nova.sme.sme01.miscellanea.Dialogs.Select_Language;
import com.nova.sme.sme01.miscellanea.Dialogs.ThemesDialog;
import com.nova.sme.sme01.miscellanea.Vocabulary;
import com.nova.sme.sme01.miscellanea.WindowMetrics;
import com.nova.sme.sme01.xml_reader_classes.ListOperations;
import com.nova.sme.sme01.xml_reader_classes.Operator;
import com.nova.sme.sme01.xml_reader_classes.XML_Login;

import java.util.Vector;

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
    private String                        base_http;
    private CustomBar ccb;
//    private String                        url_logout;

    private String                        base_url_login;// = "http://103.6.239.242:80/sme/mobile/login/?";//name=vlad&passw=1234";

    private String                        login_request;
    private TextView                      cap_text;

    private boolean                       block_login_button   = false;
    private String                        params_file_name     = "parameters.bin";
    private String                        operations_list_name = "operations_list.bin";

    private Vector<View>                  views = new Vector<View>(); // to change background
    private Button                        logout_button;
//    private ApplicationAttributes         attr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        try {
            setContentView(R.layout.activity_main);
        } catch(Exception err) {
            println(err.getMessage().toString());//android.util.AndroidRuntimeException: You cannot combine custom titles with other title features
        }

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



        cap_text    = (TextView)findViewById(R.id.cap_text);cap_text.setTag("text_background_color");
        base_layout = (android.widget.RelativeLayout) findViewById(R.id.base_layout);base_layout.setTag("main_background_color");

        FR        = new FormResizing(this, base_layout);
        pv        = new PlaceViews(this, base_layout);
        user_name = pv.getUserName();
        password  = pv.getPassword();

        MAIN_INFO = getApplicationContext().getPackageName();

        voc       = new Vocabulary();
        FM        = new FileManager(this);
/*
        this.attr = (ApplicationAttributes)FM.readFromFile("attributes.bin");
        if (this.attr == null) {
            this.attr = new ApplicationAttributes(this);
            FM.writeToFile("attributes.bin", this.attr);
        }
*/
        params = (Parameters) FM.readFromFile(params_file_name);
        if (params == null)
            params = new Parameters();

        voc.setLanguage(params.getLanguage());

        my_dialog        = new MyDialog(null, voc, base_layout);
 //       this.url_logout  = this.base_url_logout +  "id=" + this.params.getId() + "&companyID=" + this.params.getcompanyID();

        WindowMetrics wm = new WindowMetrics();wm.init(this);
        FM.writeToFile("windowMetrics.bin", wm);

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


            logout_button         = create_custom_bar();
            logout_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            setAttributes();

            }
        });
    }
    public void UpdateCustomBar(){
        if (ccb != null)
            ccb.setBackgound();
    }

    public Vector<View> getViews() {return  views;}

    private Button create_custom_bar() {
        ccb = new CustomBar(this, base_layout);

        Button button = ccb.getButton();
        if (button != null)
            voc.change_caption(button);

        views.add(base_layout);
        views.add(cap_text);
        views.add(ccb.getBase());
        views.add(ccb.getTitle());

        int                    height;
        ViewGroup.LayoutParams params;

        params = pv.getLoginButton().getLayoutParams();
        height = params.height;

        params        = button.getLayoutParams();
        params.height = height;

        FR.setLogButtonHeight(height);

        return button;
    }

    private void setAttributes() {
        ApplicationAttributes attr = (ApplicationAttributes)FM.readFromFile("attributes.bin");
        if (attr == null) {
            attr = new ApplicationAttributes(this);
            FM.writeToFile("attributes.bin", attr);
        }

        attr.setButtons(base_layout, logout_button);
        MyColors colors = attr.getColors();
        colors.setColors(views);

        if (ccb != null)
            ccb.setBackgound();
    }
    private void updateURL() {
        ApplicationAttributes attr = (ApplicationAttributes)FM.readFromFile("attributes.bin");
        if (attr == null) {
            attr = new ApplicationAttributes(this);
            FM.writeToFile("attributes.bin", attr);
        }

        base_http      = attr.getBaseUrl();
        base_url_login = base_http + "login/?";
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
        setAttributes();
        super.onResume();
    }


    public void clickLoginButton(View v) {
        if (block_login_button) {
            my_dialog.show(voc.getTranslatedString("Please, wait for Server response"));
            return;
        }
        String user = user_name.getText().toString();
        String pswd = password.getText().toString();

        updateURL();
        if (user.length() > 0 && pswd.length() > 0) {
            this.login_request = this.base_url_login + "name=" + user + "&passw=" + pswd;

            block_login_button = true;
            password.setText("");
            user_name.setText("");


            new MyHttpRequest(null, this, base_layout, voc, login_request, "XML_Login");
        } else { // debugging, temporarily
            this.login_request = this.base_url_login + "name=andrea&passw=1234";
            block_login_button = true;
            password.setText("");
            user_name.setText("");

            new MyHttpRequest(null, this, base_layout, voc, login_request, "XML_Login");
            //http//103.6.239.242/sme/mobile//login/?name=andrea&passw=1234
            //http://103.6.239.242/sme/mobile/login/?name=vlad&passw=1234
            //http://103.6.239.242/sme/mobile//login/?name=andrea&passw=1234
         }
    }

    public void reset_block_login_button() {
        block_login_button = false;
    }
    public void passExecute(XML_Login xml_login) {
        block_login_button = false;
        if (xml_login == null) {
            my_dialog.show(voc.getTranslatedString("Unknown error"));
            return;
        }

        String code       = xml_login.getCode();
        String id         = xml_login.getId();
        String originator = xml_login.getOriginator();
        String descr      = xml_login.getResDescription();

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

        String error_message = "";
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
            } else {
/*
                Николай писал:
                LOGIN
                ----------
                Code = 0  Successful operations
                Code = 1 User Role is not supported for Mobile Interface
                Code = 2 Authentication Failed

                Код 3 для данного метода НИКОГДА не должен быть возвращен - он просто не имеет смысла, хотя на сервере отрабатывается. Логин
                чаще всего и нужен именно при истекшей сессии.

*/
                if (code.equals("1"))
                    error_message = "User Role is not supported for Mobile Interface";
                else if (code.equals("2"))
                    error_message = "Authentication Failed";// (user name or password is not found)";
                else
                    error_message = "Unknown error";

                my_dialog.show(voc.getTranslatedString(error_message));
            }
        } catch(Exception err) {
            println (err.getMessage().toString());
        }
    }

    private boolean isFirstLogin() {
        // criteria: if we have got operations list
        ListOperations local = (ListOperations) FM.readFromFile("operations_list.bin");
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
            new Select_Language(base_layout, voc, FM, params, logout_button, params_file_name);
            return true;
        } else if (id == R.id.action_themes) {
            new ThemesDialog(base_layout, voc, FM, logout_button).show();
            return true;
        } else if (id == R.id.action_url_address) {
            new HttpDialog(FR, voc, base_layout, logout_button).show();
            return true;
        } else if (id == R.id.colors_themes) {
            new ColorsDialog(this, base_layout, voc, FM, logout_button).show();
            return true;
        } else if (id == R.id.action_about) {
            new AboutDialog(FR, voc, base_layout, logout_button).show();
            return true;
        } else if (id == R.id.action_help) {
            startActivity(new Intent(this, HelpNActivity.class));
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
