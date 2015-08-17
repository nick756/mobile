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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.nova.sme.sme01.miscellanea.Dialogs.AboutDialog;
import com.nova.sme.sme01.miscellanea.ApplicationAttributes;
import com.nova.sme.sme01.miscellanea.Dialogs.ColorsDialog;
import com.nova.sme.sme01.miscellanea.CustomBar;
import com.nova.sme.sme01.miscellanea.CustomAdapter;
import com.nova.sme.sme01.miscellanea.Dialogs.ConfirmTransaction;
import com.nova.sme.sme01.miscellanea.FileManager;
import com.nova.sme.sme01.miscellanea.Dialogs.HttpDialog;
import com.nova.sme.sme01.miscellanea.MyColors;
import com.nova.sme.sme01.miscellanea.Dialogs.MyDialog;
import com.nova.sme.sme01.miscellanea.MyHttpRequest;
import com.nova.sme.sme01.miscellanea.Parameters;
import com.nova.sme.sme01.miscellanea.Dialogs.Select_Language;
import com.nova.sme.sme01.miscellanea.SimpleCalendar;
import com.nova.sme.sme01.miscellanea.SpinnerModel;
import com.nova.sme.sme01.miscellanea.Dialogs.ThemesDialog;
import com.nova.sme.sme01.miscellanea.Vocabulary;
import com.nova.sme.sme01.xml_reader_classes.ListOperations;
import com.nova.sme.sme01.xml_reader_classes.Operation;


import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import static java.sql.DriverManager.println;

/*
 ***************************************
 *                                     *
 *  Provides request for transaction   *
 *                                     *
 ***************************************
 */

public class TransactionActivity extends AppCompatActivity  {
    private RelativeLayout                base_layout;
    private Parameters                    params               = new Parameters();
    private String                        params_file_name     = "parameters.bin";
    private String                        operations_list_name = "operations_list.bin";
    private ListOperations                operationList;//operaions_list;
    private List<Operation>               operations_list;
    private FormResizing                  FR;
    private FileManager                   FM;
    private Vocabulary                    voc;

    private String                        base_http;
    private String                        url_logout;//      = "http://103.6.239.242/sme/mobile/logout/?";
    private String                        base_url;//        = "http://103.6.239.242/sme/mobile/addtransaction/?";
    private MyDialog                      my_dialog;
    private Button                        logout_button;


//    private GregorianCalendar             calendar = new GregorianCalendar();
    private RelativeLayout                base_calendar_layout;
    private Spinner                       year_spinner;
    private Spinner                       month_spinner;
    private Spinner                       day_spinner;
    private SimpleCalendar                simple_calendar;// = new SimpleCalendar();

    private Button                        submit_button;

    private ArrayList<SpinnerModel> spinner_array = new ArrayList<SpinnerModel>();
    private Spinner                 spinner;
    private int                     selected_item = 0;

    CustomAdapter                   adapter;

    private Vector<View> views = new Vector<View>();

    private CustomBar ccb;
//    private ApplicationAttributes         attr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } catch(Exception err) {
            println(err.getMessage().toString());
        }
        super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_transaction);

        base_layout  = (android.widget.RelativeLayout) findViewById(R.id.transaction_base_id);
        base_layout.setTag("main_background_color");

        this.FM        = new FileManager(this);
        this.FR        = new FormResizing(this, base_layout);
        this.voc       = new Vocabulary();
/*
        this.attr = (ApplicationAttributes)FM.readFromFile("attributes.bin");
        if (this.attr == null) {
            this.attr = new ApplicationAttributes(this);
            FM.writeToFile("attributes.bin", this.attr);
        }
*/

        this.operationList   = (ListOperations) FM.readFromFile(this.operations_list_name);
        this.operations_list = this.operationList.getOperationsList();

        this.params         = (Parameters)     FM.readFromFile(this.params_file_name);
 //       this.url_logout    += "id=" + this.params.getId() + "&companyID=" + this.params.getcompanyID();

        if (this.params != null)
            this.voc.setLanguage(this.params.getLanguage());

        this.year_spinner         = (Spinner)findViewById(R.id.year_spinner);
        this.month_spinner        = (Spinner)findViewById(R.id.month_spinner);
        this.day_spinner          = (Spinner)findViewById(R.id.day_spinner);
        this.base_calendar_layout = (RelativeLayout)findViewById(R.id.base_calendar);

//        simple_calendar = new SimpleCalendar(this, this.year_spinner, this.month_spinner, this.day_spinner);

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

                FR.resizeLoginButton(base_layout, logout_button, 0.062f);
                my_dialog = new MyDialog(FR, voc, base_layout);

                setSpinner();

                create_calendar();
                FR.resizeCalendar(base_layout, base_calendar_layout, year_spinner, month_spinner, day_spinner, 0.062f);
                FR.resizeAmounts(base_layout,
                        (RelativeLayout) findViewById(R.id.base_amount_id),
                        (EditText) findViewById(R.id.sum_id),
                        (EditText) findViewById(R.id.sub_sum_id),
                        (Button) findViewById(R.id.submit_transaction_button), 0.062f);

                voc.TranslateAll(base_layout);
                setAttributes();
            }
        });
    }
    public Vector<View> getViews() {return  views;}
    public void UpdateCustomBar(){
        if (ccb != null)
            ccb.setBackgound();
    }


    private void setAttributes() {
        ApplicationAttributes attr = (ApplicationAttributes)FM.readFromFile("attributes.bin");
        if (attr == null) {
            attr = new ApplicationAttributes(this);
            FM.writeToFile("attributes.bin", attr);
        }

        attr.setButtons(base_layout, logout_button);

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

        base_http  = attr.getBaseUrl();//
        base_url   = base_http + "addtransaction/?" + "id=" + params.getId() + "&companyID=" + params.getcompanyID();
        url_logout = base_http + "logout/?"         + "id=" + params.getId() + "&companyID=" + params.getcompanyID();
    }


    void create_calendar() {
        simple_calendar = new SimpleCalendar(this, this.year_spinner, this.month_spinner, this.day_spinner);
    }

    void fillSpinner() {
        if (this.operationList == null)
            return;

 //       List<Operation> operations_list = this.operationList.getOperationsList();

        if (this.operations_list == null)
            return;

        Operation       operation;
        for (int i = 0; i < this.operations_list.size(); i ++) {
            operation = this.operations_list.get(i);
            final SpinnerModel spinnner_model = new SpinnerModel();
            spinnner_model.setOperationName(operation.getName());


            if (operation.getInbound().equals("true"))
                spinnner_model.setimageId(R.mipmap.ic_in_bound);
            else
                spinnner_model.setimageId(R.mipmap.ic_out_bound);

            spinner_array.add(spinnner_model);
        }
    }

    void setSpinner() {
        fillSpinner();

        spinner = (Spinner) findViewById(R.id.operations_list_spinner);
        adapter = new CustomAdapter(this, base_layout, R.layout.operation_item_n, spinner_array, 0.062f);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View v, int position, long id) {
                selected_item = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });

        CommonClass c_c = (CommonClass)getIntent().getSerializableExtra(MainActivity.MAIN_INFO);
        int selected  = -1;
        if (c_c != null)
            selected = getIndex(c_c.operationName);

        if (selected != -1) {
            spinner.setSelection(selected);
            selected_item = selected;
        }
    }

    private int getIndex(String name) {
        for (int j = 0; j < spinner_array.size(); j ++)
            if (spinner_array.get(j).getOperationName().trim().equals(name.trim()))
                return j;

        return -1;
    }

    void logout_request() {
        updateURL();
        new MyHttpRequest(this.FR, this, base_layout, voc, url_logout, "BaseXML");
    }
    private Button create_custom_bar() {
        ccb = new CustomBar(this, base_layout);

        Button button = ccb.getButton();
        if (button != null)
            voc.change_caption(button);

        views.add(base_layout);
        views.add(ccb.getBase());
        views.add(ccb.getTitle());

        return button;

 //       return  (new CreateCustomBar(this, base_layout)).getButton();
    }

    @Override
    protected void onResume() {
        getParams();
        voc.TranslateAll(base_layout);
        if (logout_button != null)
            voc.change_caption(logout_button);
        setAttributes();
        super.onResume();
    }
    private void getParams() {
        this.params = (Parameters) FM.readFromFile(params_file_name);
        if (this.params == null)
            this.params = new Parameters();
        else
            voc.setLanguage(params.getLanguage());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_transaction, menu);
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
/*
    private void  writeParameters() {
        this.params.setLangauge(voc.getLanguage());
        FM.writeToFile(params_file_name, this.params);
    }
*/

    public void submitClick(View view) {
        Operation s_opearion;
        String    s_date;
        String    s_descr;
        String    s_amount;

        EditText edit;

        updateURL();
        String http = base_url;
 //       http += "id=" + this.params.getId() + "&companyID=" + this.params.getcompanyID();
        http += "&date=" + simple_calendar.getDateFormatted();
        http += "&operationCode="; //this.selected_item

        s_date = simple_calendar.getDateFormatted();
        //------------//
        Operation operation = this.operations_list.get(this.selected_item);

        http += operation.getCode();

        s_opearion = operation;
        //-----------//

        //-----------//

        edit = (EditText)findViewById(R.id.transaction_description_id);
        String descr = edit.getText().toString().trim();
        if (descr.length() == 0) {
            my_dialog.show(voc.getTranslatedString("Description is empty"), R.mipmap.ic_zero);
            return;
        }
        s_descr = descr;

        http += "&operationDescription='" + descr +"'";


        //-----------//
        edit = (EditText)findViewById(R.id.sum_id);
        String sum = edit.getText().toString().trim();

        if (sum.length() == 0)
            sum = "0";

        edit = (EditText)findViewById(R.id.sub_sum_id);
        sum += "." + getCents("00" + edit.getText().toString().trim());
        s_amount = sum;

        if (Double.parseDouble(sum) == 0) {
            my_dialog.show(voc.getTranslatedString("Amount is empty"), R.mipmap.ic_zero);
            return;
        }

        // check date
        if (!simple_calendar.validateDate()) {
            my_dialog.show(voc.getTranslatedString("Date is wrong"), R.mipmap.ic_transaction_date);
            return;
        }

        http += "&operationAmount=" + sum;

        ConfirmTransaction ct = new ConfirmTransaction(this,
                                                       this.voc,
                                                       this.FR,
                                                       this.base_layout,
                                                       http,
                                                       s_opearion,
                                                       s_date,
                                                       descr,
                                                       s_amount);
        ct.show();
    }

    private String getCents(String val) {
        return val.substring(val.length() - 2);
    }


}
