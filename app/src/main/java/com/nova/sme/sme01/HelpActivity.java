package com.nova.sme.sme01;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nova.sme.sme01.miscellanea.ApplicationAttributes;
import com.nova.sme.sme01.miscellanea.CustomBar;
import com.nova.sme.sme01.miscellanea.Dialogs.MyDialog;
import com.nova.sme.sme01.miscellanea.FileManager;
import com.nova.sme.sme01.miscellanea.MyColors;
import com.nova.sme.sme01.miscellanea.MyHttpRequest;
import com.nova.sme.sme01.miscellanea.Parameters;
import com.nova.sme.sme01.miscellanea.Vocabulary;

import org.w3c.dom.Text;

import java.util.Vector;

import static java.sql.DriverManager.println;

public class HelpActivity extends AppCompatActivity {
    private RelativeLayout base_layout;
    private Button         logout_button;
    private FormResizing   FR;
    private FileManager    FM;
    private Vocabulary     voc;
    private CustomBar      ccb;
    private Vector<View>   views = new Vector<View>();
    private String         url_logout;
    private Parameters     params               = new Parameters();
    private String         params_file_name     = "parameters.bin";
    private String         html_start           = "";
    private String         html_end             = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
         } catch(Exception err) {
            println(err.getMessage().toString());
        }
        super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        try {
            setContentView(R.layout.activity_help);
        } catch(Exception e) {//Binary XML file line #228: Error inflating class <unknown>
            println(e.getMessage().toString());
        }


        this.base_layout = (RelativeLayout) findViewById(R.id.base_help);
        this.FR          = new FormResizing(this, base_layout);
        this.FM          = new FileManager(this);
        this.voc         = new Vocabulary();
        this.params      = (Parameters)FM.readFromFile(params_file_name);

        html_start += "<html>";
        html_start += "<body style='margin:0 auto; width:100%; text-align:center'>";
        html_start += "<div style='text-align:left; font-size:100%; line-height:200%'>";

        html_end += "</div>";
        html_end += "</body>";
        html_end += "</html>";



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

                FR.resizeRegularLogins(base_layout, null, logout_button, 0.062f);// height's button/total_height

                fillViews(base_layout);
                setAttributes();

                tuneSizes();
            }
        });
    }

    private float convertDpToPixels(int dp) {
        return dp * getResources().getDisplayMetrics().density;
    }
    public float convertPxToDp(float px) {
        return px / getResources().getDisplayMetrics().density;
    }

    private void tuneSizes() {
        float factor     = 455.0f/747.0f;
        int   real_width = FR.getRealWidth();
        float imgWidth   = real_width*0.4f;
        float imgHeight  = imgWidth/factor;

        Vector<Integer> imageIds = new Vector<Integer>();
        imageIds.add(R.id.help_1);
        imageIds.add(R.id.help_2);
        imageIds.add(R.id.help_3);
        imageIds.add(R.id.help_4);
        imageIds.add(R.id.help_5);
        imageIds.add(R.id.help_6);
        imageIds.add(R.id.help_7);
        imageIds.add(R.id.help_8);
        imageIds.add(R.id.help_9);
        imageIds.add(R.id.help_10);
        imageIds.add(R.id.help_11);
        imageIds.add(R.id.help_12);
        setImgSize(imageIds, (int) imgWidth, (int) imgHeight);

        htmls();
    }

    private void htmls() {
        TextView tv;
        String   html;

        // I
        html  = html_start;
        html += "First time login assumes pre-registration of User  and Business on the server side before any operations on the mobile application.";
        html += html_end;

        tv = (TextView)findViewById(R.id.help_text_1);
        tv.setText(Html.fromHtml(html));
        // I

        // II
        html  = html_start;
        html += "Successful first time login on an android device  is followed by screen with a single option:";
        html += "'Synchronize Operations List'";
        html += "<br>";
        html += "This option entails transfer of list of allowed operations to mobile client.";

        html += html_end;

        tv = (TextView)findViewById(R.id.help_text_2);
        tv.setText(Html.fromHtml(html));
        // II

        // III
        html  = html_start;
        html += "The important thing is that the only a single Company (Business) can be managed from Mobile Application.  ";
        html += "If a User needs to manage more than one company using android device, two login names must be used ";
        html += "(In other words  if it is necessary to login with OTHER username/login), and each time re-synchronization ";
        html += "to be performed by  pressing button RESET OPERATIONS  LIST.";
        html += html_end;

        tv = (TextView)findViewById(R.id.help_text_3);
        tv.setText(Html.fromHtml(html));
        // III


        // IV
        html = html_start;

        html += " You can get updating <b>Operations List</b> again at any time by pressing button '<b>Synchronize Operations List</b>'.";
        html += " The Type of the Operation is placed on the right side of either icon <img src='data_import.png'/>";
        html += "<br>";
        html += "or <img src='data_export.png'/>";
        html += "<br>";
        html += " The description of the operation is placed on the right side of icon <img src='gold.png'/>";

        html += html_end;


        ImageGetter imageGetter = new ImageGetter();

        tv = (TextView)findViewById(R.id.help_text_4);
        tv.setText(Html.fromHtml(html, imageGetter, null));
        // IV


        // V
        html = html_start;
        html += "There are two ways to perform transaction(s) such as pressing on button PERFORM TRANSACTION and on the ";
        html += "next form make necessary selection and ";
        html += "<br>";
        html += "press image button <img src='action_button.png'/>";
        html += "<br><br>";
        html += "with preselected type of operation (in image it is 'Advance from Directors').";

        html += html_end;

        tv = (TextView)findViewById(R.id.help_text_5);
        tv.setText(Html.fromHtml(html, imageGetter, null));
        // V


        // VI
        html = html_start;
        html += "To obtain the full list of available Operations touch the area  showed by arrow. Then using scrolling to make  a choice.";
        html += "<br>";
        html += "<br>";
        html += "The application makes control of the filled data.";
        html += "So the transaction is being considered if the Description & Amount field is not empty. The Date can not be more then current.";
        html += html_end;

        tv = (TextView)findViewById(R.id.help_text_6);
        tv.setText(Html.fromHtml(html));
        // VI

        // VII
        html = html_start;
        html += "Before sending a request to cash flow procedure the dialog box asks confirmation.";
        html += html_end;

        tv = (TextView)findViewById(R.id.help_text_7);
        tv.setText(Html.fromHtml(html));
        // VII

        // VIII
        html = html_start;
        html += "If everything goes well, you get the positive feedback message, otherwise negative one informs the transaction failed.";
        html += html_end;

        tv = (TextView)findViewById(R.id.help_text_8);
        tv.setText(Html.fromHtml(html));
        // VIII

        // IX
        html = html_start;
        html += "To view transactions go to the main page and press button VIEW TRANSACTIONS";
        html += html_end;

        tv = (TextView)findViewById(R.id.help_text_9);
        tv.setText(Html.fromHtml(html));
        // IX

        // X
        html = html_start;
        html += "On the appeared dialog box you can set necessary filter as follow:";
        html += "<br>";
        html += "<br>";
        html += "<b>Select types of operations.</b>";
        html += "<br>";
        html += "Possible to select all of them or part.";
        html += "To do that use check box ‘Select/Unselect All’ and checkboxes on scrolling list of operations";
        html += "<br>";
        html += "<br>";
        html += "<b>The date range</b>";
        html += "<br>";
        html += "Use dropdawn components to set <b>From Till</b>  Date.";

        html += html_end;

        tv = (TextView)findViewById(R.id.help_text_10);
        tv.setText(Html.fromHtml(html));
        // X

        // XI
        html = html_start;
        html += "If there is data on the selected criteria, you will get a list of the transactions.";
        html += "In the parentheses indicates the number of the transactions";
        html += "<br>";
        html += "<br>";
        html += "It includes:";
        html += "<br>";
        html += "<br>";
        html += "1. The Code of transaction.";
        html += "<br>";
        html += "<br>";
        html += "2. The Date of the transaction.";
        html += "<br>";
        html += "<br>";
        html += "3. The Type of the Operation.";
        html += "<br>";
        html += "   The left icon shows if it is inbound or outbound";
        html += "<br>";
        html += "<br>";
        html += "4. The Amount of money";
        html += "<br>";
        html += "<br>";
        html += "5. The Description";
        html += "<br>";
        html += "<br>";
        html += "6. The name of the person who made the transaction";


        html += html_end;

        tv = (TextView)findViewById(R.id.help_text_11);
        tv.setText(Html.fromHtml(html));
        // XI

        // XII
        html = html_start;

        html += "If there is no data matched to selected criteria, you receive an appropriate message";

        html += html_end;

        tv = (TextView)findViewById(R.id.help_text_12);
        tv.setText(Html.fromHtml(html));
        // XII

    }

    private class ImageGetter implements Html.ImageGetter {

        public Drawable getDrawable(String source) {
            int id = 0;
            if (source.equals("data_import.png")) {
                id = R.mipmap.ic_in_bound;
            } else if (source.equals("data_export.png")) {
                id = R.mipmap.ic_out_bound;
            } else if (source.equals("gold.png")) {
                id = R.mipmap.ic_financial_type;
            } else if (source.equals("action_button.png")) {
                id = R.mipmap.ic_transaction_button;
            } else {
                return null;
            }

            Drawable d = getResources().getDrawable(id);
            d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
            return d;
        }
    }

    private void setImgSize(Vector<Integer> imageIds, int imgWidth, int imgHeight) {
        ViewGroup.LayoutParams params;
        ImageView              img;
        int                    id;

        for (int i = 0; i < imageIds.size(); i ++) {
            id            = imageIds.get(i);
            img           = (ImageView) findViewById(id);
            params        = img.getLayoutParams();
            params.width  = imgWidth;
            params.height = imgHeight;
        }
    }

    private void setAttributes() {
        ApplicationAttributes attr = (ApplicationAttributes)FM.readFromFile("attributes.bin");
        if (attr == null)
            attr = new ApplicationAttributes(this);

        attr.setButtons(base_layout, logout_button);

        MyColors colors = attr.getColors();
        colors.setColors(views);

        if (ccb != null)
            ccb.setBackgound();
    }

    private Button create_custom_bar() {
        ccb = new CustomBar(this, base_layout);

        Button button = ccb.getButton();
        if (button != null)
            voc.change_caption(button);

        views.add(ccb.getBase());
        views.add(ccb.getTitle());

        return button;
    }

    private void logout_request() {
        updateURL();
        new MyHttpRequest(this.FR, this, base_layout, voc, url_logout, "BaseXML");
    }

    private void updateURL() {
        ApplicationAttributes attr = (ApplicationAttributes) FM.readFromFile("attributes.bin");
        if (attr == null)
            attr = new ApplicationAttributes(this);

        String base_http = attr.getBaseUrl();

        url_logout       = base_http + "logout/?";
        url_logout      += "id=" + params.getId() + "&companyID=" + params.getcompanyID();
    }

    private void fillViews(View view) {
        String className = view.getClass().getSimpleName();

        if (className.indexOf("RelativeLayout") != -1 || className.indexOf("LinearLayout") != -1 || className.indexOf("TextView") != -1) {
            view.setTag("main_background_color");
            views.add(view);
        }

        if ((view instanceof ViewGroup)) {
            ViewGroup vg = (ViewGroup) view;
            for (int i = 0; i < vg.getChildCount(); i++)
                fillViews(vg.getChildAt(i));
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_help, menu);
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
