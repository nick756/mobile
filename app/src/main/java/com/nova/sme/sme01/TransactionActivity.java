package com.nova.sme.sme01;

import android.app.Activity;
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

        FM  = new FileManager(this);
        FR  = new FormResizing(this, base_layout);
        voc = new Vocabulary();

        this.operaions_list = (GetOperations) FM.readFromFile(this.operations_list_name);
        this.params         = (Parameters)    FM.readFromFile(this.params_file_name);
        url_logout         += "id=" + this.params.getId() + "&companyID=" + this.params.getcompanyID();


        year_spinner         = (Spinner)findViewById(R.id.year_spinner);
        month_spinner        = (Spinner)findViewById(R.id.month_spinner);
        day_spinner          = (Spinner)findViewById(R.id.day_spinner);
        base_calendar_layout = (RelativeLayout)findViewById(R.id.base_calendar);

        simple_calendar = new SimpleCalendar(this, this.year_spinner, this.month_spinner, this.day_spinner);

        String deb = "http://103.6.239.242/sme/mobile/addtransaction/?id=4&companyID=2&date=15/7/2015&operationCode=3&operationAmount=1500.60&operationDescription=This is a test";
        String ss = deb.substring(144);

        ViewTreeObserver vto = base_layout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {

                base_layout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                FR.resize();

                Button button = create_custom_bar();
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        logout_request();
                    }
                });

                FR.resizeLoginButton(base_layout, button, 0.062f);
                setSpinner();

                FR.resizeCalendar(base_layout, base_calendar_layout, year_spinner, month_spinner, day_spinner, 0.062f);
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
        String http = base_url;
        http += "id=" + this.params.getId() + "&companyID=" + this.params.getcompanyID();
        http += "&date=" + simple_calendar.getDateFormatted();
        http += "&operationCode="; //this.selected_item

        //------------//
        Operation       operation;
        List<Operation> operations_list = this.operaions_list.getOperationsList();
        operation                       = operations_list.get(this.selected_item);

        http += operation.getCode();
        //-----------//

        //-----------//
        EditText edit = (EditText)findViewById(R.id.sum_id);
        String sum = edit.getText().toString().trim();
        edit = (EditText)findViewById(R.id.sum_id);

        edit = (EditText)findViewById(R.id.sub_sum_id);
        sum += "." + edit.getText().toString().trim();

        http += "&operationAmount=" + sum;
        //-----------//

        edit = (EditText)findViewById(R.id.transaction_description_id);
        http += "&operationDescription='" + edit.getText().toString().trim() +"'";

        HttpRequestTransaction http_request = new HttpRequestTransaction(this, base_layout, voc, http);

        //http://103.6.239.242/sme/mobile/addtransaction/?id=4&companyID=2&date=15/7/2015&operationCode=2&operationAmount=59423.89&operationDescription=This is a test
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
