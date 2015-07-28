package com.nova.sme.sme01.miscellanea;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nova.sme.sme01.R;
import com.nova.sme.sme01.xml_reader_classes.Record;
import com.nova.sme.sme01.xml_reader_classes.ListTransactions;

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
    private ListTransactions listTransactions;
    private int            id;
    private Vocabulary     voc;
    private RelativeLayout base_layout;

    public FillWithTransactionsList(Activity activity, ListTransactions listTransactions, int id, Vocabulary voc, RelativeLayout base_layout) {
        this.activity     = activity;
        this.listTransactions = listTransactions;
        this.id           = id;
        this.voc          = voc;
        this.base_layout  = base_layout;

        implement();
    }

    private boolean implement() {
        if (this.listTransactions == null) {
            clean_scroll();
            return false;
        }

        List<Record> list;
        try {
            list = this.listTransactions.getRecordsList();

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
                LinearLayout ll = (LinearLayout) inflater.inflate(R.layout.tarnsaction_view_item_n, null);

                sv.addView(ll);
                setValues(ll, list.get(i));
                params = (ViewGroup.MarginLayoutParams) ll.getLayoutParams();
                params.topMargin    = 5;
                params.bottomMargin = 5;
            }
            return true;
        } catch (Exception err) {//java.lang.ClassCastException: android.widget.LinearLayout cannot be cast to android.widget.RelativeLayout
            println(err.getMessage().toString());
        }
        return false;
    }
    public float pxToDp(float px) {
        return px / activity.getResources().getDisplayMetrics().density;
    }
    private void setValues(LinearLayout layout, Record record) {
        View           view = null;
        String         tag  = "";
        String         type = null;
        TextView       text = null;
        LinearLayout   inner_layout = null;

         for (int i = 0; i < layout.getChildCount(); i ++) {
            inner_layout = (LinearLayout) layout.getChildAt(i);
            tag          = (String)inner_layout.getTag();
            for (int j = 0; j < inner_layout.getChildCount(); j ++) {
                view = inner_layout.getChildAt(j);
                tag = (String) view.getTag();

                if (tag != null) {
                    if (tag.equals("code")) {
                        text = (TextView) view;
                        text.setText(record.getTranCode());
                    } else if (tag.equals("date")) {
                        text = (TextView) view;
                        text.setText(record.getDate());
                    } else if (tag.equals("type")) {
                        type = record.getType().trim();
                        text = (TextView) view;

                        if (type.indexOf("IN:") == 0) {
                            setTypeIcon(inner_layout, R.mipmap.ic_in_bound);
                            type = type.substring(3);
                        } else {
                            setTypeIcon(inner_layout, R.mipmap.ic_out_bound);
                            type = type.substring(4);
                        }
                        text.setText(type);
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
    }
    private void setTypeIcon(LinearLayout   inner_layout, int id) {
        int    cnt = inner_layout.getChildCount();
        View   view;
        String tag;
        ImageView img;
        for (int  i = 0; i < cnt; i ++) {
            view = inner_layout.getChildAt(i);
            tag  = (String)view.getTag();
            if (tag != null) {
                if (tag.equals("in_or_out")) {
                    img = (ImageView) view;
                    img.setImageResource(id);
                    return;
                }
            }
        }
    }

    private void clean_scroll() {
        LinearLayout sv = (LinearLayout) activity.findViewById(id);
        sv.removeAllViews();
    }


}
