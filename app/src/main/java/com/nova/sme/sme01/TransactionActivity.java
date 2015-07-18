package com.nova.sme.sme01;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Time;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.nova.sme.sme01.miscellanea.CreateCustomBar;
import com.nova.sme.sme01.miscellanea.CustomAdapter;
import com.nova.sme.sme01.miscellanea.FileManager;
import com.nova.sme.sme01.miscellanea.HttpRequestTransaction;
import com.nova.sme.sme01.miscellanea.Http_Request_Logout;
import com.nova.sme.sme01.miscellanea.MyDialog;
import com.nova.sme.sme01.miscellanea.Parameters;
import com.nova.sme.sme01.miscellanea.SimpleCalendar;
import com.nova.sme.sme01.miscellanea.SpinnerModel;
import com.nova.sme.sme01.miscellanea.Vocabulary;
import com.nova.sme.sme01.xml_reader_classes.GetOperations;
import com.nova.sme.sme01.xml_reader_classes.Operation;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import static java.sql.DriverManager.println;


public class TransactionActivity extends AppCompatActivity /*implements View.OnClickListener*/ {
    private RelativeLayout                base_layout;
    private Parameters                    params               = new Parameters();
    private String                        params_file_name     = "parameters.bin";
    private String                        operations_list_name = "operations_list.bin";
    private GetOperations                 operaions_list;
    private FormResizing                  FR;
    private FileManager                   FM;
    private Vocabulary                    voc;
    private String                        url_logout      = "http://103.6.239.242/sme/mobile/logout/?";
    private String                        base_url        = "http://103.6.239.242/sme/mobile/addtransaction/?";
    private MyDialog                      my_dialog;


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

        this.FM        = new FileManager(this);
        this.FR        = new FormResizing(this, base_layout);
        this.voc       = new Vocabulary();
        this.my_dialog = new MyDialog(voc, base_layout);

        this.operaions_list = (GetOperations) FM.readFromFile(this.operations_list_name);
        this.params         = (Parameters)    FM.readFromFile(this.params_file_name);
        this.url_logout    += "id=" + this.params.getId() + "&companyID=" + this.params.getcompanyID();

        this.voc.setLanguage(this.params.getLanguage());

        this.year_spinner         = (Spinner)findViewById(R.id.year_spinner);
        this.month_spinner        = (Spinner)findViewById(R.id.month_spinner);
        this.day_spinner          = (Spinner)findViewById(R.id.day_spinner);
        this.base_calendar_layout = (RelativeLayout)findViewById(R.id.base_calendar);

        simple_calendar = new SimpleCalendar(this, this.year_spinner, this.month_spinner, this.day_spinner);

        ViewTreeObserver vto = base_layout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {
                base_layout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                FR.resize();

                Button button = create_custom_bar();
                voc.change_caption(button);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        logout_request();
                    }
                });

                FR.resizeLoginButton(base_layout, button, 0.062f);
                setSpinner();

                FR.resizeCalendar(base_layout, base_calendar_layout, year_spinner, month_spinner, day_spinner, 0.062f);
                FR.resizeAmounts(base_layout,
                        (RelativeLayout) findViewById(R.id.base_amount_id),
                        (EditText) findViewById(R.id.sum_id),
                        (EditText) findViewById(R.id.sub_sum_id),
                        (Button) findViewById(R.id.submit_transaction_button), 0.062f);

                voc.TranslateAll(base_layout);
            }
        });
    }

    void setSpinner() {
        spinner = (Spinner) findViewById(R.id.operations_list_spinner);
        fillSpinner();
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

        adapter = new CustomAdapter(this, base_layout, R.layout.operation_item, spinner_array, 0.062f);
        spinner.setAdapter(adapter);
    }
    void logout_request() {
        new Http_Request_Logout(this, this.url_logout, this.FM, this.voc, this.base_layout, false);
    }
    private Button create_custom_bar() {
        return  (new CreateCustomBar(this, base_layout)).getButton();
    }

    void fillSpinner() {
        if (this.operaions_list == null)
            return;

        List<Operation> operations_list = this.operaions_list.getOperationsList();

        if (operations_list == null)
            return;

        Operation       operation;
        for (int i = 0; i < operations_list.size(); i ++) {
            operation = operations_list.get(i);
            final SpinnerModel spinnner_model = new SpinnerModel();
            spinnner_model.setOperationName(operation.getName());

            if (operation.getInbound().equals("true"))
                spinnner_model.setimageId(R.mipmap.ic_in_bound);
            else
                spinnner_model.setimageId(R.mipmap.ic_out_bound);

            spinner_array.add(spinnner_model);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_transaction, menu);
        return true;
    }

    public void submitClick(View view) {
        Operation s_opearion;
        String    s_date;
        String    s_descr;
        String    s_amount;

        EditText edit;
        String http = base_url;
        http += "id=" + this.params.getId() + "&companyID=" + this.params.getcompanyID();
        http += "&date=" + simple_calendar.getDateFormatted();
        http += "&operationCode="; //this.selected_item

        s_date = simple_calendar.getDateFormatted();
        //------------//
        Operation       operation;
        List<Operation> operations_list = this.operaions_list.getOperationsList();
        operation                       = operations_list.get(this.selected_item);

        http += operation.getCode();

        s_opearion = operation;
        //-----------//

        //-----------//

        edit = (EditText)findViewById(R.id.transaction_description_id);
        String descr = edit.getText().toString().trim();
        if (descr.length() == 0) {
            my_dialog.show("Description can not be empty");
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
            my_dialog.show("Amount can not be empty");
            return;
        }

        http += "&operationAmount=" + sum;

        ConfirmTransaction ct = new ConfirmTransaction(this,
                                                       this.voc,
                                                       this.base_layout,
                                                       http,
                                                       s_opearion,
                                                       s_date,
                                                       descr,
                                                       s_amount);
        ct.show();
    }
/*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    }
*/

    private String getCents(String val) {
        return val.substring(val.length() - 2);
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
