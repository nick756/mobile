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
import java.util.Vector;

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
    private TextResizing   textFit;
    private String         sample = "Purchase of Office Equipment so";
    private boolean        first_text = false;
    private float          textsize   = 0;

    private Vector<TextView> texts = new Vector<TextView>();

    public FillWithTransactionsList(Activity activity, ListTransactions listTransactions, int id, Vocabulary voc, RelativeLayout base_layout) {
        this.activity     = activity;
        this.listTransactions = listTransactions;
        this.id           = id;
        this.voc          = voc;
        this.base_layout  = base_layout;
        this.textFit      = new TextResizing(activity);

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
/*                    
                    if (!first_text) {
                        if (view.getClass().getSimpleName().toUpperCase().indexOf("TEXTVIEW") != -1) {
                            first_text = true;
                            text = (TextView) view;

                            textsize = textFit.getSizeWidth(text, sample, 1.0f, base_layout.getWidth());
                        }
                    }
*/

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
                    if (view.getClass().getSimpleName().toUpperCase().indexOf("TEXTVIEW") != -1)
                        texts.add(text);
/*
                    if (textsize > 0)
                        if (view.getClass().getSimpleName().toUpperCase().indexOf("TEXTVIEW") != -1)
                            text.setTextSize(textsize);
                            */
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
    public void setFontSize() {
        TextView tv;
        for (int i = 0; i < texts.size();i ++) {
            tv = texts.elementAt(i);
            if (i == 0)
                textsize = textFit.getSizeWidth(tv, sample, 1.5f, base_layout.getWidth());

            tv.setTextSize(textsize);

        }

    }


}
