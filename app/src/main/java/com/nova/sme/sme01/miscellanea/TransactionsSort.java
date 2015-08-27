package com.nova.sme.sme01.miscellanea;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nova.sme.sme01.xml_reader_classes.Operation;

import org.w3c.dom.Text;

import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.Vector;

/*
 *****************************************
 *                                       *
 *   Sort transactions by some criteria  *
 *   in TransactionsViewActivity         *
 *                                       *
 *****************************************
 */
public class TransactionsSort {
    private Vector<layout_item>  layout_items    = new Vector<layout_item>();
    private Vector<content_item> content_items   = new Vector<content_item>();

    private Vector<content_item>  sorted_by_date = new Vector<content_item>(); // initial, to avoid sorting ny date using 'Date' or 'Gregoriancalendar' objects

    public TransactionsSort() {

    }
    public void init() { // just after filling
        Collections.copy(sorted_by_date, content_items); // it is sorted by date on the server side
    }

    public void sort(int index) {

    }

    private void sortByOperationType() {
        Collections.sort(this.content_items, new Comparator<content_item>() {
            @Override
            public int compare(content_item o1, content_item o2) {
                return o1.type.compareTo(o2.type);
            }
        });
        update();
    }
    private void sortByDateMinToMax() {
        content_items.clear();
        Collections.copy(content_items, sorted_by_date);
        update();
    }
    private void sortByDateMaxToMin() {
        content_items.clear();
        int size = sorted_by_date.size();
        for (int j = size - 1; j >= 0; j --)
            content_items.add(sorted_by_date.get(j));

        update();
    }
    private void sortAmountFromHighest() {
        Collections.sort(content_items, new Comparator<content_item>() {
            @Override
            public int compare(content_item c1, content_item c2) {
                return Double.compare(c1.amount_dbl, c2.amount_dbl);
            }
        });
        update();
    }
    private void sortAmountFromLowest() {
        Collections.sort(content_items, new Comparator<content_item>() {
            @Override
            public int compare(content_item c1, content_item c2) {
                return Double.compare(c2.amount_dbl, c1.amount_dbl);
            }
        });
        update();
    }
    private void update() {
        int size = content_items.size();
        for (int i = 0; i < size; i ++)
            bind(i);
    }


    private void bind(int index) {
        layout_item  li = layout_items.get(index);
        content_item ci = content_items.get(index);

        li.code.setText(ci.code);
        li.type.setText(ci.type);
        li.date.setText(ci.date);
        li.icon_type.setImageResource(ci.in_out);
        li.amount.setText(ci.amount);
        li.descr.setText(ci.descr);
        li.operator.setText(ci.operator);
    }

    public class layout_item {
        public TextView     code;
        public TextView     date;
        public TextView     type;
        public ImageView    icon_type;
        public TextView     amount;
        public TextView     descr;
        public TextView     operator;

        public layout_item (){

        }
    }
    public class content_item {
        public String       code;
        public String       date;
        public String       type;
        public int          in_out;     // id ogf image
        public String       amount;
        public String       descr;
        public String       operator;
        public content_item me;

        // for sorting
        public double            amount_dbl;

        public content_item() {
            me = this;
        }

    }
}
