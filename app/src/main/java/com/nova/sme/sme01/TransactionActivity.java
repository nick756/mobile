package com.nova.sme.sme01;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.nova.sme.sme01.miscellanea.CreateCustomBar;
import com.nova.sme.sme01.miscellanea.CustomAdapter;
import com.nova.sme.sme01.miscellanea.FileManager;
import com.nova.sme.sme01.miscellanea.Http_Request_Logout;
import com.nova.sme.sme01.miscellanea.Parameters;
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

    private DatePicker                    datePicker;
    private GregorianCalendar             calendar;

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


        datePicker    = (DatePicker)findViewById(R.id.date_picker);
        this.calendar = new GregorianCalendar();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            this.calendar.add(Calendar.DATE, -365);
            this.datePicker.setMinDate(this.calendar.getTimeInMillis());
        } else {
            final int minYear  = calendar.get(Calendar.YEAR);
            final int minMonth = calendar.get(Calendar.MONTH);
            final int minDay   = calendar.get(Calendar.DAY_OF_MONTH);

            this.datePicker.init(minYear, minMonth, minDay,
                    new DatePicker.OnDateChangedListener() {

                        public void onDateChanged(DatePicker view, int year,
                                                  int month, int day) {
                            Calendar newDate = Calendar.getInstance();
                            newDate.set(year, month, day);

                            if (calendar.after(newDate)) {
                                view.init(minYear - 1, minMonth, minDay, this);
                            }
                        }
                    });
        }

//        DateTime minDate = new DateTime();
//        calendar.setMinDate(System.currentTimeMillis() - 1000);//*60*60*24*365);

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
