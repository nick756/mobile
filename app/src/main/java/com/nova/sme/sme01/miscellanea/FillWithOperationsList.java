package com.nova.sme.sme01.miscellanea;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nova.sme.sme01.R;
import com.nova.sme.sme01.xml_reader_classes.ListOperations;
import com.nova.sme.sme01.xml_reader_classes.Operation;

import java.util.List;

import static java.sql.DriverManager.println;


/*
 **************************************************
 *                                                *
 *   Fill scrollview with operations list         *
 *                                                *
 **************************************************
 */
public class FillWithOperationsList {
    private Activity      activity;
    private ListOperations operations;
    private int           id;
    private Vocabulary    voc;
    private RelativeLayout base_layout;

    public FillWithOperationsList(Activity activity, ListOperations operations, int id, Vocabulary voc, RelativeLayout base_layout) {
        this.activity = activity;
        this.operations = operations;
        this.id = id;
        this.voc = voc;
        this.base_layout = base_layout;
    }

    public boolean implement() {
        if (this.operations == null) {
            clean_scroll();
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

            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(activity.getBaseContext().LAYOUT_INFLATER_SERVICE);//reg_op_list_scrollView
            LinearLayout   sv       = (LinearLayout) activity.findViewById(id);
            sv.removeAllViews();

            for (int i = 0; i < list.size(); i++) {
                LinearLayout ll = (LinearLayout) inflater.inflate(R.layout.operations_template, null);
                sv.addView(ll);
                setValues(ll, list.get(i));
            }
            return true;
        } catch (Exception err) {
            println(err.getMessage().toString());
        }
        return false;
    }

    private void clean_scroll() {
        LinearLayout   sv       = (LinearLayout) activity.findViewById(id);
        sv.removeAllViews();
    }

    private void setValues(LinearLayout layout, Operation operation) {
        View view;
        LinearLayout inner_layout;
        View         to_remove = null;
        String       tag;
        TextView text;
        ImageView img;

        to_remove = null;
        for (int i = 0; i < layout.getChildCount(); i ++) {
            inner_layout = (LinearLayout) layout.getChildAt(i);
            for (int j = 0; j < inner_layout.getChildCount(); j ++) {
                view      = inner_layout.getChildAt(j);
                tag       = (String) view.getTag();

                if (tag != null) {
                     if (tag.equals("name")) {
                        text = (TextView) view;
                        text.setText(operation.getName());
                    } else if (tag.equals("in_out_bound")) {
                        img = (ImageView) view;
                        if (operation.getInbound().equals("true")) {
                            img.setImageResource(R.mipmap.ic_in_bound);
                        } else {
                            img.setImageResource(R.mipmap.ic_out_bound);
                        }
                     } else if (tag.equals("type")) {
                        text = (TextView) view;
                        text.setText(operation.getType());
                    }
                }
            }
        }
    }
}
