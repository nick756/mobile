package com.nova.sme.sme01.miscellanea;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.nova.sme.sme01.FormResizing;
import com.nova.sme.sme01.xml_reader_classes.ListOperations;
import com.nova.sme.sme01.xml_reader_classes.Operation;

import java.util.List;
import java.util.Vector;
import com.nova.sme.sme01.R;

/*
 ******************************
 *                            *
 *   Show list of operations  *
 *   to be selected           *
 *                            *
 ******************************
 */
public class SelectableOperationList {
    private Vocabulary     voc;
    private RelativeLayout base_layout;
    private FormResizing   FR;
    private FileManager    FM;
    private Context        context;
    private Dialog         dialog;

    private Vector<WideOperation> operations;// = new Vector<WideOperation>();

    public SelectableOperationList(Dialog dialog, Vocabulary voc, RelativeLayout base_layout, FormResizing FR) {
//        this.operations  = new Vector<WideOperation>();
        this.voc         = voc;
        this.base_layout = base_layout;
        this.context     = base_layout.getContext();
        this.FR          = FR;
        this.FM          = new FileManager(this.context);
        this.dialog      = dialog;

        fillOperations();

        LinearLayout layout     = (LinearLayout)dialog.findViewById(R.id.from_till_layout);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        for (int i = 0; i < operations.size(); i++) {
            LinearLayout ll = (LinearLayout) inflater.inflate(R.layout.operation_item_check, null);
            layout.addView(ll);
            setValues(ll, operations.get(i));
        }


    }
    public void setAllCheckBoxes(boolean checked) {
        LinearLayout layout   = (LinearLayout)dialog.findViewById(R.id.from_till_layout);//from_till_layout
        set_CheckBoxes(checked, layout);
    }

    private void set_CheckBoxes(boolean checked, View view) {
        String className = view.getClass().getName().toUpperCase().trim();
        CheckBox cb;
        if (className.indexOf(new String("CheckBox").toUpperCase()) != -1) {
            cb = (CheckBox) view;
            cb.setChecked(checked);

            WideOperation wo = (WideOperation)cb.getTag();
            wo.checked       = checked;
        } else  if ((view instanceof ViewGroup)) {
            ViewGroup vg = (ViewGroup) view;
            for (int i = 0; i < vg.getChildCount(); i++)
                set_CheckBoxes(checked, vg.getChildAt(i));
        }
    }

    public void save() {
        FM.writeToFile("wideOperations.bin", operations);
    }

    private void setValues(LinearLayout layout, WideOperation wo) {
        try {
            View           view;
            String         tag;
            RelativeLayout rl;
            ImageView      img;
            TextView       text;
            CheckBox       cb;

            for (int j = 0; j < layout.getChildCount(); j ++) {
                view = layout.getChildAt(j);
                tag  = (String) view.getTag();
                if (tag.equals("element")) {
                    rl = (RelativeLayout) view;
                    for (int k = 0; k < rl.getChildCount(); k ++) {
                        view = rl.getChildAt(k);
                        tag  = (String) view.getTag();
                        if (tag.equals("image")) {
                            img = (ImageView) view;
                            if(wo.in_out.equals("IN"))
                                img.setImageResource(R.mipmap.ic_in_bound);
                            else
                                img.setImageResource(R.mipmap.ic_out_bound);
                        } else if (tag.equals("text")) {
                            text = (TextView) view;
                            text.setText(wo.name);
                        } else if (tag.equals("check")) {
                            cb = (CheckBox) view;
                            cb.setChecked(wo.checked);
                            cb.setTag(wo);

                            cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                    CheckBox cb      = (CheckBox) buttonView;
                                    WideOperation wo = (WideOperation)cb.getTag();
                                    wo.checked       = isChecked;
                                }
                            });
                        }
                    }
                }
            }

        } catch(Exception e) {


        }

    }

    public Vector<WideOperation> getOperations() {return operations;}

    private void fillOperations() {
        WideOperation wo;
        this.operations = (Vector<WideOperation>)FM.readFromFile("wideOperations.bin");
        if (operations == null) {
            this.operations  = new Vector<WideOperation>();
            ListOperations listOpeartions   = (ListOperations) FM.readFromFile("operations_list.bin");
            if (listOpeartions == null)
                return;
            List<Operation> operations_list = listOpeartions.getOperationsList();
            if (operations_list == null)
                return;
            if (operations_list.size() == 0)
                return;

            Operation operation;
            for (int j = 0; j < operations_list.size(); j ++) {
                operation = operations_list.get(j);

                wo = new WideOperation();
                wo.name = operation.getName();
                if (operation.getInbound().equals("true"))
                    wo.in_out = "IN";
                else
                    wo.in_out = "OUT";

                wo.checked = true;
                operations.add(wo);
            }
            FM.writeToFile("wideOperations.bin", operations);
        } else {

        }

    }


}
