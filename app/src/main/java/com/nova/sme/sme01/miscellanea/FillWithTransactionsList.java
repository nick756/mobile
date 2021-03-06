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

import java.text.DecimalFormat;
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

    private String         maxLimit = "Purchase of Plants and Machineries";
    private String         maxName  = "Telephone, Fax and Internet";

    private float               textsize   = 0;
    private Vector<TextView>    texts = new Vector<TextView>();
    private int                 transactionsNumber = 0;

    private OperationsSelector  operationSelector;
    private TransactionsSort    TS = new TransactionsSort();

    public FillWithTransactionsList(Activity activity, ListTransactions listTransactions, int id, Vocabulary voc, RelativeLayout base_layout) {
        this.activity     = activity;
        this.listTransactions = listTransactions;
        this.id           = id;
        this.voc          = voc;
        this.base_layout  = base_layout;
        this.textFit      = new TextResizing(activity);

        FileManager FM   = new FileManager(activity);
        operationSelector = (OperationsSelector) FM.readFromFile("OperationsSelector.bin");
        if (operationSelector == null) {
            operationSelector = new OperationsSelector();
            FM.writeToFile("OperationsSelector.bin", operationSelector);
        }
        implement();
    }

    public void Sort(int index) {
        TS.sort(index);
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
            transactionsNumber = 0;
            for (int i = 0; i < list.size(); i++) {// todo
                if (!operationSelector.isCheckedFullName(list.get(i).getType()))
                    continue;

                transactionsNumber ++;

                LinearLayout ll = (LinearLayout) inflater.inflate(R.layout.tarnsaction_view_item_n, null);

                sv.addView(ll);
                setValues(ll, list.get(i));
                params = (ViewGroup.MarginLayoutParams) ll.getLayoutParams();
                params.topMargin    = 5;
                params.bottomMargin = 5;
            }
            setFontSize();
            return true;
        } catch (Exception err) {
            println(err.getMessage().toString());
        }
        return false;
    }
    public int getActualTransactions(){return transactionsNumber;}
    public float pxToDp(float px) {
        return px / activity.getResources().getDisplayMetrics().density;
    }
    private void setValues(LinearLayout layout, Record record) {
        View           view = null;
        String         tag  = "";
        String         type = null;
        TextView       text = null;
        LinearLayout   inner_layout = null;

        DecimalFormat df = new DecimalFormat("###,###,###.00");

        double  val  = 0;
        String money = "";

        TransactionsSort.layout_item  li = TS.createLayoutItem();
        TransactionsSort.content_item ci = TS.createContentItem();

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

                        li.code = text;
                        ci.code = record.getTranCode();
                    } else if (tag.equals("date")) {
                        text = (TextView) view;
                        text.setText(record.getDate());

                        li.date = text;
                        ci.date = record.getDate();
                    } else if (tag.equals("type")) {
                        type = record.getType().trim();
                        text = (TextView) view;

                        if (type.indexOf("IN:") == 0) {
                            li.icon_type = setTypeIcon(inner_layout, R.mipmap.ic_in_bound);
                            type         = type.substring(3);
                            ci.in_out    = R.mipmap.ic_in_bound;
                        } else if (type.indexOf("OUT:") == 0) {
                            li.icon_type = setTypeIcon(inner_layout, R.mipmap.ic_out_bound);
                            type         = type.substring(4);
                            ci.in_out    = R.mipmap.ic_out_bound;
                        }
                        type = type.trim();
                        text.setText(type);

                        if (type.length() > maxName.length())
                            if (type.length() <= maxLimit.length())
                                maxName = type;
                        // Purchase of Plants and Machineries
                        //Purchase of Plants and Machineries
                        li.type = text;
                        ci.type = type;
                    } else if (tag.equals("amount")) {
                        text = (TextView) view;
                        boolean err = false;
                        try {
                            val   = Double.parseDouble(record.getAmount());
                            money = df.format(val);
                            text.setText(money);
                        } catch(Exception e) {
                            err = true;
                        }
                        if (err) {
                            text.setText(record.getAmount());
                        } else {
                            li.amount     = text;
                            ci.amount     = money;
                            ci.amount_dbl = val;
                        }
                    } else if (tag.equals("descr")) {
                        text = (TextView) view;
                        text.setText(record.getDescr());

                        li.descr = text;
                        ci.descr = record.getDescr();
                    } else if (tag.equals("operator")) {
                        text = (TextView) view;
                        text.setText(record.getOperator());

                        li.operator = text;
                        ci.operator = record.getOperator();
                    }
                    if (view.getClass().getSimpleName().toUpperCase().indexOf("TEXTVIEW") != -1)
                        texts.add(text);
                }
            }
        }
    }
    private ImageView setTypeIcon(LinearLayout   inner_layout, int id) {
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
                    return img;
                }
            }
        }
        return null;
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
                textsize = textFit.getSizeWidth(tv, this.maxName, 1.4f, this.base_layout.getWidth());

            if (textsize > 0)
                tv.setTextSize(textsize);
        }

    }

}
