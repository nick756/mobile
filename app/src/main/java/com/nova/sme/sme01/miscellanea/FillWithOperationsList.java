package com.nova.sme.sme01.miscellanea;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nova.sme.sme01.CommonClass;
import com.nova.sme.sme01.MainActivity;
import com.nova.sme.sme01.R;
import com.nova.sme.sme01.RegularLoginActivity;
import com.nova.sme.sme01.TransactionActivity;
import com.nova.sme.sme01.xml_reader_classes.ListOperations;
import com.nova.sme.sme01.xml_reader_classes.Operation;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;

import static java.sql.DriverManager.println;


/*
 **************************************************
 *                                                *
 *   Fill scrollview with operations list         *
 *                                                *
 **************************************************
 */
public class FillWithOperationsList {
    private Activity       activity;
    private ListOperations operations;
    private int            id;
    private Vocabulary     voc;
    private RelativeLayout base_layout;
    private Vector<item>   items = new Vector<item>();


    private String         maxLimit = "Maintenance of Office and Equipment";
    private String         maxName  = "Telephone, Fax and Internet";
    private String         maxType  = "Telephone, Fax and Internet";


    private Vector<TextView> names  = new Vector<TextView>();
    private Vector<TextView> types  = new Vector<TextView>();
    private TextResizing     textFit;

    private Vector<View> bases = new Vector<View>();
//    private LinearLayout     template_operation;

    private TextView t_v;

    private LinearLayout sv;

    public FillWithOperationsList(Activity activity, ListOperations operations, int id, Vocabulary voc, RelativeLayout base_layout) {
        this.activity = activity;
        this.operations = operations;
        this.id = id;
        this.voc = voc;
        this.base_layout = base_layout;
        this.textFit     = new TextResizing(activity);

//        this.template_operation = (LinearLayout) activity.findViewById(R.id.template_operations_id);
    }

    public boolean implement() {
        if (this.operations == null) {
            clean_scroll();
            bases.clear();
            return false;
        }

        List<Operation> list;
        try {
            list = this.operations.getOperationsList();

            if (list == null) {
                clean_scroll();
                return false;
            }
            if (list.size() == 0) {
                clean_scroll();
                return false;
            }

            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(activity.getBaseContext().LAYOUT_INFLATER_SERVICE);
            /*LinearLayout*/   sv   = (LinearLayout) activity.findViewById(id);
            sv.removeAllViews();


 //           int n = R.id.template_operations_id, id;
 //           if (n == 98798)
 //               return false;

            for (int i = 0; i < list.size(); i++) {
                LinearLayout ll = (LinearLayout) inflater.inflate(R.layout.operations_template, null);

 //               id = ll.getId();

                bases.add(ll);

                sv.addView(ll);
                setValues(ll, list.get(i));
            }

            ViewTreeObserver vto = sv.getViewTreeObserver();
            vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @SuppressWarnings("deprecation")
                @Override
                public void onGlobalLayout() {
                    sv.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    //setFontSize();
                }
            });

            for (int j = 0; j < bases.size(); j ++)
                bases.get(j).setTag("main_background_color");


            return true;
        } catch (Exception err) {
            println(err.getMessage().toString());
        }
        return false;
    }

    public Vector<View> getBases(){return bases;}

    private void clean_scroll() {
        LinearLayout sv = (LinearLayout) activity.findViewById(id);
        sv.removeAllViews();
    }

    private void setValues(LinearLayout layout, Operation operation) {
        View           view;
        RelativeLayout inner_layout;
        String         tag;
        TextView       text;
        ImageView      img;

        int            maxLength = maxLimit.length(), length;

        String err = "", operationName = "";
        try {
            for (int i = 0; i < layout.getChildCount(); i++) {
                inner_layout = (RelativeLayout) layout.getChildAt(i);
                for (int j = 0; j < inner_layout.getChildCount(); j++) {
                    view = inner_layout.getChildAt(j);
                    tag  = (String) view.getTag();

                    if (tag != null) {
                        if (tag.equals("name")) {//name
                            operationName = operation.getName().trim();
                            text          = (TextView) view;
                            text.setText(operationName);

                            length = text.getText().toString().length();
                            if (length > maxName.length() && length <= maxLength )
                                maxName = text.getText().toString();
                            names.add(text);
                        } else if (tag.equals("in_out_bound")) {
                            img = (ImageView) view;
                            if (operation.getInbound().equals("true")) {
                                img.setImageResource(R.mipmap.ic_in_bound);
                            } else {
                                img.setImageResource(R.mipmap.ic_out_bound);
                            }
                        } else if (tag.equals("type")) {//type
                            text = (TextView) view;
                            text.setText(operation.getType().trim());
                            length = text.getText().toString().length();
                            if (length > maxType.length() && length <= maxLength)
                                maxType = text.getText().toString();
                            types.add(text);
                        } else if (tag.equals("transaction_button")) {
                            img = (ImageView) view;
                            items.add(new item(operationName, img));
                        }
                    }
                }
            }
        } catch(Exception e) {
            err = e.getMessage().toString();
        }
    }

    public class item {
        private String operationName;

        public item(String operationName, ImageView img) {
            this.operationName = operationName;
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callTransactionActivity();
                }
            });
        }

        private void callTransactionActivity() {
            CommonClass c_c   = new CommonClass();
            c_c.operationName = this.operationName;

            Intent resultIntent = new Intent(base_layout.getContext(), TransactionActivity.class);
            resultIntent.putExtra(MainActivity.MAIN_INFO, c_c);
            activity.startActivity(resultIntent);
        }
    }
/*
    public class FitTextSize {
        private RelativeLayout rl;

        public FitTextSize(RelativeLayout rl) {
            this.rl =  rl;

            rl.post(new Runnable() {
                public void run() {
                    setFontSize();
                }
            });
        }
    }
*/
    public void setFontSize() {
        TextView tv;
        float    ts_name, ts_type, text_size;

        if (names.size() == 0)
            return;

        tv      = names.get(0);
        ts_name = textFit.getSizeWidth(tv, this.maxName, 1.4f, this.base_layout.getWidth());

        tv      = types.get(0);
        ts_type = textFit.getSizeWidth(tv, this.maxType, 1.4f, this.base_layout.getWidth());

        text_size = Math.min(ts_name, ts_type);

        for (int i = 0; i < names.size(); i ++) {
            names.get(i).setTextSize(text_size);
            types.get(i).setTextSize(text_size);
        }
    }
/*
    public class FitSize {
        private TextView tv;

        public FitSize(TextView tv) {
            this.tv =  tv;
            t_v = tv;

            tv.post(new Runnable() {
                public void run() {
             });
        }
    }
*/
/*
    private void set_TextView(View view) {
        String   className = view.getClass().getSimpleName();
        TextView tv;
        int      length;
        String   txt;
        if (className.indexOf("TextView") != -1) {

        } else  if ((view instanceof ViewGroup)) {
            ViewGroup vg = (ViewGroup) view;
            for (int i = 0; i < vg.getChildCount(); i++)
                set_TextView(vg.getChildAt(i));
        }
    }
*/

}
