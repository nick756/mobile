package com.nova.sme.sme01;

import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nova.sme.sme01.miscellanea.ApplicationAttributes;
import com.nova.sme.sme01.miscellanea.CustomBar;
import com.nova.sme.sme01.miscellanea.FileManager;
import com.nova.sme.sme01.miscellanea.MyColors;
import com.nova.sme.sme01.miscellanea.MyHttpRequest;
import com.nova.sme.sme01.miscellanea.Parameters;
import com.nova.sme.sme01.miscellanea.Vocabulary;

import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

import static java.sql.DriverManager.println;

/*
 ********************************
 *                              *
 *  Show selected help item     *
 *                              *
 ********************************
 */

public class HelpItemActivity extends AppCompatActivity {

    private RelativeLayout        base_layout;
    private Button                logout_button;
    private FormResizing          FR;
    private FileManager           FM;
    private Vocabulary            voc;
    private CustomBar             ccb;
    private Vector<View>          views = new Vector<View>();
    private String                url_logout;
    private Parameters            params               = new Parameters();
    private String                params_file_name     = "parameters.bin";
    private String                html_start           = "";
    private String                html_end             = "";
    private int                   help_id;

    private ApplicationAttributes attr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } catch(Exception err) {
            println(err.getMessage().toString());
        }
        super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_help_item);

        this.base_layout = (RelativeLayout) findViewById(R.id.base_help_item);
        this.FR          = new FormResizing(this, base_layout);
        this.FM          = new FileManager(this);
        this.voc         = new Vocabulary();

        getParams();
//        this.params      = (Parameters)FM.readFromFile(params_file_name);

//        if (this.params != null)
//            this.voc.setLanguage(params.getLanguage());

        this.attr = (ApplicationAttributes)FM.readFromFile("attributes.bin");
        if (this.attr == null) {
            this.attr = new ApplicationAttributes(this);
            FM.writeToFile("attributes.bin", this.attr);
        }

        html_start += "<html>";
        html_start += "<body style='margin:0 auto; width:100%; text-align:center'>";
        html_start += "<div style='text-align:left; font-size:100%; line-height:200%'>";

        html_end += "</div>";
        html_end += "</body>";
        html_end += "</html>";

        help_id = getIntent().getIntExtra("help_id", 0);

         int [] ids = {
                R.id.help_trouble,
                R.id.help_colors,
                R.id.help_buttons,
                R.id.help_url,
                R.id.help_language,
                R.id.help_view_trans,
                R.id.help_per_trans,
                R.id.help_op_list,
                R.id.help_login,
                R.id.help_general

        };



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

                fillContent();
                fillViews(base_layout);
                setAttributes();
            }
        });
    }

    private Drawable getImageFromAsset(String name) {
        Drawable    drawable = null;
        InputStream stream   = null;
        try  {
            stream   = getAssets().open(name);
            drawable = Drawable.createFromStream(stream, null);
        } catch(IOException ex)  {
            drawable = null;
        } finally {
            try
            {
                if(stream != null)
                    stream.close();

            } catch (Exception ignored) {}
        }
        return drawable;
    }
    private void fillItem(Vector<ImageView> images, LayoutInflater inflater, LinearLayout   l_layout, String html, String image_path, boolean use_icon) {
        TextView       content;
        ImageView      img;
        RelativeLayout r_layout;

        r_layout = (RelativeLayout) inflater.inflate(R.layout.help_item, null);
        l_layout.addView(r_layout);

        img     = (ImageView) r_layout.getChildAt(0);
        content = (TextView)  r_layout.getChildAt(1);

        if (image_path.length() == 0) {
            r_layout.removeView(img);
        } else {
//            try {
                img.setImageDrawable(getImageFromAsset(image_path));
                images.add(img);
//            } catch(Exception e) {

//            }
        }
        if (use_icon)
            content.setText(Html.fromHtml(html, new ImageGetter(), null));
        else
            content.setText(Html.fromHtml(html));

        content.setMovementMethod(LinkMovementMethod.getInstance());
    }
/*
    private void fillItem(Vector<ImageView> images, LayoutInflater inflater, LinearLayout   l_layout, String html, int image_id, boolean use_icon) {
        TextView       content;
        ImageView      img;
        RelativeLayout r_layout;

        r_layout = (RelativeLayout) inflater.inflate(R.layout.help_item, null);
        l_layout.addView(r_layout);

        img     = (ImageView) r_layout.getChildAt(0);
        content = (TextView)  r_layout.getChildAt(1);

        if (image_id == 0) {
            r_layout.removeView(img);
        } else {
            img.setImageResource(image_id);
            images.add(img);
        }
        if (use_icon)
            content.setText(Html.fromHtml(html, new ImageGetter(), null));
        else
            content.setText(Html.fromHtml(html));

        content.setMovementMethod(LinkMovementMethod.getInstance());
    }
*/
    private void fillContent() {
        LayoutInflater inflater  = (LayoutInflater) getSystemService(getBaseContext().LAYOUT_INFLATER_SERVICE);
        LinearLayout   l_layout  = (LinearLayout) findViewById(R.id.help_scroll_base);// to be filled
        TextView       caption   = (TextView) findViewById(R.id.textTopic);
        RelativeLayout r_layout;
        TextView       content;
        ImageView      img;
        String         html;


        float             factor     = 374.0f/630.0f;
        int               real_width = FR.getRealWidth();
        float             imgWidth   = real_width*0.4f;
        float             imgHeight  = imgWidth/factor;
        Vector<ImageView> images     = new Vector<ImageView>();
        String            br         = "<br>";


//        Html.ImageGetter imageGetter;

        switch(this.help_id) {
            case R.id.help_trouble:
                caption.setText("Troubleshooting");
                ////////////////////////////////////////////////////////////////////////////
                html = html_start;
                html += "If you get such a response after attempt of logging then it could be:";
                html += br + br;
                html += "<b>1.	Internet connection problem.</b>";
                html += br + br;
                html += "a. Check your mobile internet settings";
                html += br + br;
                html += "b. If you are at  the office (home) then try to reboot the rooter,  check for overheating, verify router cables are securely connected.";
                html += br + br;
                html += br + br;
                html += "<b>2.	Url Address is not correct</b>";
                html += br + br;
                html += "Be sure that url address is correct, look at the Menu->Application Setting->Url Address.";
                html += "Ask technical support (<a href='mailto:krasnikovn@yandex.ru'>krasnikovn@yandex.ru</a>) if necessary.";
                html += br + br;
                html += br + br;
                html += "<b>3.	Problem in the server side.</b>";
                html += br + br;
                html += "Connect to the technical support team.";

                html += html_end;

//                fillItem(images, inflater, l_layout, html, R.drawable.help21, false);
                fillItem(images, inflater, l_layout, html, "helpImages/help21.png", false);

                ////////////////////////////////////////////////////////////////////////////
                html  = html_start;
                html += "See Menu->Help->Login Procedure in details";
                html += br + br;
                html += "Necessary to login with old username+password";
                html += " and press";
                html += br;
                html += "<b>RESET OPERATION LIST</b> on the main form.";
                html += html_end;

//                fillItem(images, inflater, l_layout, html, R.drawable.help22, false);
                fillItem(images, inflater, l_layout, html, "helpImages/help22.png", false);

                ////////////////////////////////////////////////////////////////////////////
                html  = html_start;
                html += "If during the job you make a long pause, the server could stop the session.";
                html += br;
                html += "It leads to a breakdown of communication and the application  returns to the login form. Then you need to login again.";

                html += html_end;

//                fillItem(images, inflater, l_layout, html, 0, false);
                fillItem(images, inflater, l_layout, html, "", false);

                break;
            case R.id.help_colors:
                caption.setText("Colors Themes");
            ////////////////////////////////////////////////////////////////////////////
                html = html_start;
                html += "This dialog box lets to change color theme of the application.";
                html += br + br;
                html += "There are four items to be selected as:";
                html += br + br;
                html += "1. Action bar background (placed on the top)";
                html += br + br;
                html += "2.	Background of the application";
                html += br + br;
                html += "3.	The caption on dialog boxes";
                html += br + br;
                html += "4.	The background color of the dialog box";
                html += br + br;
                html += "The selected object is shown by <b> flickering border. </b>";
                html += br + br;
                html += "To set a color of the selected item use three sliders:";
                html += br + br;
                html += "<b> <font color='Red'> Red</font>, <font color='green'> Green</font></b> and <b><font color='blue'>Blue.</font></b>";
                html += br + br;
                html += "The final color is a mixing of these three components.";
                html += br + br;
                html += "To get default color theme use <b>Reset</b> button.";
                html += html_end;

//                fillItem(images, inflater, l_layout, html, R.drawable.help20, false);
                fillItem(images, inflater, l_layout, html, "helpImages/help20.png", false);

            ////////////////////////////////////////////////////////////////////////////
                html = html_start;
                html += "To change a color of the <b>Action Bar</b> select the first item.";
                html += html_end;

//                fillItem(images, inflater, l_layout, html, R.drawable.help16, false);
                fillItem(images, inflater, l_layout, html, "helpImages/help16.png", false);

            ////////////////////////////////////////////////////////////////////////////

                html = html_start;
                html += "To change a color of the <b> Application’s Background</b> select the second item.";
                html += html_end;

  //              fillItem(images, inflater, l_layout, html, R.drawable.help17, false);
                fillItem(images, inflater, l_layout, html, "helpImages/help17.png", false);

                ////////////////////////////////////////////////////////////////////////////

                html = html_start;
                html += "To change a color of the <b>Dialog Box’s Caption</b> select the third  item.";
                html += html_end;

//                fillItem(images, inflater, l_layout, html, R.drawable.help18, false);
                fillItem(images, inflater, l_layout, html, "helpImages/help18.png", false);

                ////////////////////////////////////////////////////////////////////////////

                html = html_start;
                html += "To change a background color of the <b>Dalog Box</b> select the fourth item.";
                html += html_end;

//                fillItem(images, inflater, l_layout, html, R.drawable.help19, false);
                fillItem(images, inflater, l_layout, html, "helpImages/help19.png", false);

                ////////////////////////////////////////////////////////////////////////////

                break;
            case R.id.help_buttons:
                caption.setText("Buttons Themes");

                html  = html_start;
                html += "To select a button press either a button or radio button on the left side.";
                html += br + br;
                html += "The text color of the button is setting by a slider";
                html += br + br;
                html += "If the selection is accepted, all buttons of the application obtain this color configuration.";
                html += html_end;

//                fillItem(images, inflater, l_layout, html, R.drawable.help15, false);
                fillItem(images, inflater, l_layout, html, "helpImages/help15.png", false);

                break;
            case R.id.help_url:
                caption.setText("URL Addrers");
                html = html_start;
                html += "You will be informed if the server’s url address need to be changed.";
                html += br + br;
                html += "Be very careful while filling out the url address, single incorrect symbol follows failure to connect.";
                html += html_end;

//                fillItem(images, inflater, l_layout, html, R.drawable.help16, false);
                fillItem(images, inflater, l_layout, html, "helpImages/help16.png", false);

                break;
            case R.id.help_settings_general:
                caption.setText("General Information");

                html  = html_start;
                html += "The <b>Application Setting</b> menu item includes four items as follow:";
                html += br;
                html += br;
                html += "1.    <b>Select Language</b>. Two languages are available – English and Malay.";
                html += br;
                html += br;
                html += "2.    <b>URL Address</b>. It should be used if by some reason the Server’s url address is changed.";
                html += br;
                html += br;
                html += "3/4.  <b>Buttons Themes & Colors Themes</b>. You can set your own background style.";
                html += html_end;

//                fillItem(images, inflater, l_layout, html, 0, false);
                fillItem(images, inflater, l_layout, html, "", false);
                break;
            case R.id.help_language:
                caption.setText("Select Language");

                html  = html_start;
                html += "The mobile application changes all texts and captions in accordance with the selected language on the fly.";
                html += br;
                html += br;
                html += "The selection is saved so that after restarting appropriated language is used.";
                html += html_end;

//                fillItem(images, inflater, l_layout, html, R.drawable.help13, false);
                fillItem(images, inflater, l_layout, html, "helpImages/help13.png", false);

                break;
            case R.id.help_view_trans:
                caption.setText("View Transactions");
                // IX
                html = html_start;
                html += "To view transactions go to the main page and press button VIEW TRANSACTIONS";
                html += html_end;

//                fillItem(images, inflater, l_layout, html, R.drawable.help9, false);
                fillItem(images, inflater, l_layout, html, "helpImages/help9.png", false);
                // IX

                // X
                html = html_start;
                html += "On the appeared dialog box you can set necessary filter as follow:";
                html += br;
                html += br;
                html += "<b>Select types of operations.</b>";
                html += br;
                html += "Possible to select all of them or part.";
                html += "To do that use check box ‘Select/Unselect All’ and checkboxes on scrolling list of operations";
                html += br;
                html += br;
                html += "<b>The date range</b>";
                html += br;
                html += "Use dropdawn components to set <b>From Till</b>  Date.";
                html += html_end;

//                fillItem(images, inflater, l_layout, html, R.drawable.help10, false);
                fillItem(images, inflater, l_layout, html, "helpImages/help10.png", false);
                // X

                // XI
                html = html_start;
                html += "If there is data on the selected criteria, you will get a list of the transactions. ";
                html += "In the parentheses indicates the number of the transactions";
                html += br;
                html += br;
                html += "It includes:";
                html += br;
                html += "1. The Code of transaction.";
                html += br;
                html += "2. The Date of the transaction.";
                html += br;
                html += "3. The Type of the Operation.";
                html += br;
                html += "   The left icon shows if it is inbound or outbound";
                html += br;
                html += "4. The Amount of money";
                html += br;
                html += "5. The Description";
                html += br;
                html += "6. The name of the person who made the transaction";
                html += html_end;

                //fillItem(images, inflater, l_layout, html, R.drawable.help11, false);
                fillItem(images, inflater, l_layout, html, "helpImages/help11.png", false);
                // XI

                // XII
                html = html_start;
                html += "If there is no data matched to selected criteria, you receive an appropriate message";
                html += html_end;

                //fillItem(images, inflater, l_layout, html, R.drawable.help12, false);
                fillItem(images, inflater, l_layout, html, "helpImages/help12.png", false);
                // XII

                break;
            case R.id.help_per_trans:
                caption.setText("Perform Transactions");
                // V
                html = html_start;
                html += "There are two ways to perform transaction(s) such as pressing on button PERFORM TRANSACTION and on the ";
                html += "next form make necessary selection and ";
                html += br;
                html += "press image button <img src='action_button.png'/>";
                html += "<br><br>";
                html += "with preselected type of operation (in image it is 'Advance from Directors').";
                html += html_end;

//                fillItem(images, inflater, l_layout, html, R.drawable.help5, true);
                fillItem(images, inflater, l_layout, html, "helpImages/help5.png", false);
                // V

                // VI
                html = html_start;
                html += "To obtain the full list of available Operations touch the area  showed by arrow. Then using scrolling to make  a choice.";
                html += br;
                html += br;
                html += "The application makes control of the filled data.";
                html += "So the transaction is being considered if the Description & Amount field is not empty. The Date can not be more then current.";
                html += html_end;

//                fillItem(images, inflater, l_layout, html, R.drawable.help6, false);
                fillItem(images, inflater, l_layout, html, "helpImages/help6.png", false);
                // VI

                // VII
                html = html_start;
                html += "Before sending a request to cash flow procedure the dialog box asks confirmation.";
                html += html_end;

                //fillItem(images, inflater, l_layout, html, R.drawable.help7, false);
                fillItem(images, inflater, l_layout, html, "helpImages/help7.png", false);
                // VII

                // VIII
                html = html_start;
                html += "If everything goes well, you get the positive feedback message, otherwise negative one informs the transaction failed.";
                html += html_end;

                //fillItem(images, inflater, l_layout, html, R.drawable.help8, false);
                fillItem(images, inflater, l_layout, html, "helpImages/help8.png", false);
                // VIII

                break;
            case R.id.help_op_list:
                caption.setText("Operations List");
                // IV
                html = html_start;

                html += " You can get updating <b>Operations List</b> again at any time by pressing button '<b>Synchronize Operations List</b>'.";
                html += " The Type of the Operation is placed on the right side of either ";
                html += "icon <img src='data_import.png'/>";
                html += " or <img src='data_export.png'/>";
                html += br;
                html += " The description of the operation is placed on the right side of ";
                html += "icon <img src='gold.png'/>";
                html += html_end;

//                fillItem(images, inflater, l_layout, html, R.drawable.help4, true);
                fillItem(images, inflater, l_layout, html, "helpImages/help4.png", false);
                // IV

                break;
            case R.id.help_login: // start from here, one item i sneeded
                caption.setText("Login Procedure");
                html  = html_start;
                html += "First time login assumes pre-registration of User and Business on the server side before any operations on the mobile application.";
                html += html_end;

                //fillItem(images, inflater, l_layout, html, R.drawable.help1, false);
                fillItem(images, inflater, l_layout, html, "helpImages/help1.png", false);
                // I

                // II
                html  = html_start;
                html += "Successful first time login on an android device  is followed by screen with a single option:";
                html += "'Synchronize Operations List'";
                html += br + br;
                html += "This option entails transfer of list of allowed operations to mobile client.";
                html += html_end;

//                fillItem(images, inflater, l_layout, html, R.drawable.help2, false);
                fillItem(images, inflater, l_layout, html, "helpImages/help2.png", false);
                // II

                // III
                html  = html_start;
                html += "The important thing is that the only a single Company (Business) can be managed from Mobile Application.  ";
                html += br + br;
                html += "If a User needs to manage more than one company using android device, two login names must be used ";
                html += "(In other words  if it is necessary to login with OTHER username/login), and each time re-synchronization ";
                html += "to be performed by  pressing button RESET OPERATIONS  LIST.";
                html += html_end;

                //fillItem(images, inflater, l_layout, html, R.drawable.help3, false);
                fillItem(images, inflater, l_layout, html, "helpImages/help3.png", false);
                // III
                break;
            case R.id.help_general:
                caption.setText("General Information");

                html  = html_start;
                html += "This is mobile version of Small and Medium Enterprise Cash Flow system.";
                html += br;
                html += br;
                html += "It allows an responsible person to closely monitor cash flow and ensure that it is adequate.";
                html += br;
                html += br;
                html += "The official site is: <a href='http://103.6.239.242/sme/'>";
                html += br;
                html += "www.103.6.239.242/sme/</a>";
                html += br;
                html += br;
                html += "Technical support: <a href='mailto:krasnikovn@yandex.ru'>";
                html += br;
                html += "krasnikovn@yandex.ru</a>";
                html += html_end;

//                fillItem(images, inflater, l_layout, html, 0, false);
                fillItem(images, inflater, l_layout, html, "", false);
                break;


        }
        if (images.size() > 0)
            setImgSize(images, (int) imgWidth, (int) imgHeight);
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
            d.setBounds(0, 0, d.getIntrinsicWidth()/2, d.getIntrinsicHeight()/2);
            return d;
        }
    }

    private void setImgSize(Vector<ImageView> images, int imgWidth, int imgHeight) {
        ViewGroup.LayoutParams params;
        ImageView              img;


        for (int i = 0; i < images.size(); i ++) {
            img           = images.get(i);
            params        = img.getLayoutParams();
            params.width  = imgWidth;
            params.height = imgHeight;
        }
    }


    private void setAttributes() {
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
        return true;
    }

    private void getParams() {
        this.params = (Parameters) FM.readFromFile(params_file_name);
        if (this.params == null)
            this.params = new Parameters();
        else
            voc.setLanguage(params.getLanguage());
    }


}
