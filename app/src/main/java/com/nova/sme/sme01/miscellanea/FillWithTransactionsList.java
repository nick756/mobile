package com.nova.sme.sme01.miscellanea;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsoluteLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nova.sme.sme01.R;
import com.nova.sme.sme01.xml_reader_classes.GetOperations;
import com.nova.sme.sme01.xml_reader_classes.Operation;
import com.nova.sme.sme01.xml_reader_classes.Record;
import com.nova.sme.sme01.xml_reader_classes.Transactions;

import java.util.List;

import static java.sql.DriverManager.println;

/*
 **************************************************
 *                                                *
 *   Fill scrollview with transactions list       *
 *                                                *
 **************************************************
 */
public class FillWithTransactionsList {
    private Activity       activity;
    private Transactions   transactions;
    private int            id;
    private Vocabulary     voc;
    private RelativeLayout base_layout;

    public FillWithTransactionsList(Activity activity, Transactions transactions, int id, Vocabulary voc, RelativeLayout base_layout) {
        this.activity     = activity;
        this.transactions = transactions;
        this.id           = id;
        this.voc          = voc;
        this.base_layout  = base_layout;

        implement();
    }

    private boolean implement() {
        if (this.transactions == null) {
            clean_scroll();
            return false;
        }

        List<Record> list;
        try {
            list = this.transactions.getRecordsList();

            if (list == null) {
                clean_scroll();
                return false;
            }
            if (list.size() == 0) {
                clean_scroll();
                return false;
            }

            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(activity.getBaseContext().LAYOUT_INFLATER_SERVICE);
            LinearLayout   sv       = (LinearLayout) activity.findViewById(id);
            sv.removeAllViews();

            ViewGroup.MarginLayoutParams params;
            for (int i = 0; i < list.size(); i++) {
                RelativeLayout ll = (RelativeLayout) inflater.inflate(R.layout.transaction_view_item, null);

                sv.addView(ll);
                setValues(ll, list.get(i));
                params = (ViewGroup.MarginLayoutParams) ll.getLayoutParams();
                params.topMargin    = 5;
                params.bottomMargin = 5;
            }
            return true;
        } catch (Exception err) {
            println(err.getMessage().toString());
        }
        return false;
    }
    public float pxToDp(float px) {
        return px / activity.getResources().getDisplayMetrics().density;
    }
    private void setValues(RelativeLayout layout, Record record) {
        View           view;
        String         tag;
        TextView       text;

         for (int j = 0; j < layout.getChildCount(); j ++) {
            view      = layout.getChildAt(j);
            tag       = (String) view.getTag();

            if (tag != null) {
                if (tag.equals("code")) {
                    text = (TextView) view;
                    text.setText(record.getTranCode());
                } else if (tag.equals("date")) {
                    text = (TextView) view;
                    text.setText(record.getDate());
                } else if (tag.equals("type")) {
                    text = (TextView) view;
                    text.setText(record.getType());
                } else if (tag.equals("amount")) {
                    text = (TextView) view;
                    text.setText(record.getAmount());
                } else if (tag.equals("descr")) {
                    text = (TextView) view;
                    text.setText(record.getDescr());
                } else if (tag.equals("operator")) {
                    text = (TextView) view;
                    text.setText(record.getOperator());
                }
            }
        }
    }

    private void clean_scroll() {
        LinearLayout sv = (LinearLayout) activity.findViewById(id);
        sv.removeAllViews();
    }


}
