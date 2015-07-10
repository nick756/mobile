package com.nova.sme.sme01.miscellanea;


import android.app.Activity;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nova.sme.sme01.R;
import com.nova.sme.sme01.transactions.GetOperations;
import com.nova.sme.sme01.transactions.Operation;

import java.util.ArrayList;

import static java.sql.DriverManager.println;


/*
 **************************************************
 *                                                *
 *   Fill scrollview with operations list         *
 *                                                *
 **************************************************
 */
public class FillWithOperationsList {
    public FillWithOperationsList(Activity activity, GetOperations operations, int id, Vocabulary voc, RelativeLayout base_layout) {
        MyDialog my_dialog = new MyDialog(voc, base_layout);

        ArrayList<Operation> list;
        try {
            list = operations.getOperationsList();

            if (list == null) {
                my_dialog.show("Operations List is empty");
                return;
            }
            if (list.size() == 0) {
                my_dialog.show("Operations List is empty");
                return;
            }

            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(activity.getBaseContext().LAYOUT_INFLATER_SERVICE);//reg_op_list_scrollView
            LinearLayout sv = (LinearLayout) activity.findViewById(id);
            sv.removeAllViews();

            for (int i = 0; i < list.size(); i++) {
                LinearLayout ll = (LinearLayout) inflater.inflate(R.layout.operations_template, null);
                sv.addView(ll);
                setValues(ll, list.get(i));
            }
        } catch (Exception err) {
            println(err.getMessage().toString());
        }
    }

    private void setValues(LinearLayout layout, Operation operation) {
        View view;
        LinearLayout inner_layout;
        String       tag;
        TextView text;
        ImageView img;
        for (int i = 0; i < layout.getChildCount(); i ++) {
            inner_layout = (LinearLayout) layout.getChildAt(i);
            for (int j = 0; j < inner_layout.getChildCount(); j ++) {
                view = inner_layout.getChildAt(j);
                tag  = (String) view.getTag();
                if (tag != null) {
                    if (tag.equals("code")) {
                        text = (TextView) view;
                        text.setText(operation.getCode());
                    } else if (tag.equals("name")) {
                        text = (TextView) view;
                        text.setText(operation.getName());
                    } else if (tag.equals("inbound")) {
                        img = (ImageView) view;//R.drawable.someImageId
                        if (operation.getInbound().equals("true"))
                            img.setImageResource(R.mipmap.ic_checked);
                        else
                            img.setImageResource(R.mipmap.ic_uncheck);
                    } else if (tag.equals("outbound")) {
                        img = (ImageView) view;
                        if (operation.getOutbound().equals("true"))
                            img.setImageResource(R.mipmap.ic_checked);
                        else
                            img.setImageResource(R.mipmap.ic_uncheck);
                    } else if (tag.equals("type")) {
                        text = (TextView) view;
                        text.setText(operation.getType());
                    }
                }
            }
        }
    }
}
