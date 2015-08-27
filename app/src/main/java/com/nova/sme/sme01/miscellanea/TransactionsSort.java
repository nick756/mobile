package com.nova.sme.sme01.miscellanea;

import android.widget.ImageView;
import android.widget.TextView;

import com.nova.sme.sme01.xml_reader_classes.Operation;

import org.w3c.dom.Text;

import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

import static java.sql.DriverManager.println;

/*
 *****************************************
 *                                       *
 *   Sort transactions by some criteria  *
 *   in TransactionsViewActivity         *
 *                                       *
 *****************************************
 */
public class TransactionsSort {
    private int                  current_sort    = 0;
    private Vector<layout_item>  layout_items    = new Vector<layout_item>();
    private Vector<content_item> content_items   = new Vector<content_item>();

    private Vector<content_item>  sorted_by_date = new Vector<content_item>(); // initial, to avoid sorting ny date using 'Date' or 'Gregoriancalendar' objects

    //                   0                 1                   2                3                       4
    //String items[] = {"Date ascending", "Date descending", "Operation type", "Highest amount fisrt", "Lowest amount first"};

    public TransactionsSort() {

    }

    public layout_item createLayoutItem() {
        layout_item li = new layout_item();
        layout_items.add(li);
        return li;
    }
    public content_item createContentItem() {
        content_item ci = new content_item();
        content_items.add(ci);
        sorted_by_date.add(ci);
        return ci;
    }

    public void sort(int index) {
        if (index == current_sort)
            return;

        current_sort = index;

        switch (index) {
            case 0:
                sortByDateMinToMax();
                break;
            case 1:
                sortByDateMaxToMin();
                break;
            case 2:
                sortByOperationType();
                break;
            case 3:
                sortAmountFromHighest();
                break;
            case 4:
                sortAmountFromLowest();
                break;
        }
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
        content_items = null;
        try {
            content_items = new Vector<content_item>(sorted_by_date);
        } catch(OutOfMemoryError ee) {
            println(ee.getMessage().toString());
        } catch (Exception e) {
            println(e.getMessage().toString());
        }
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
            return Double.compare(c2.amount_dbl, c1.amount_dbl);
            }
        });
        update();
    }
    private void sortAmountFromLowest() {
        Collections.sort(content_items, new Comparator<content_item>() {
            @Override
            public int compare(content_item c1, content_item c2) {
            return Double.compare(c1.amount_dbl, c2.amount_dbl);
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

        try {
            if (li.code != null)
                li.code.setText(ci.code);
            if (li.type != null)
                li.type.setText(ci.type);
            if (li.date != null)
                li.date.setText(ci.date);
            if (li.icon_type != null)
                li.icon_type.setImageResource(ci.in_out);
            if (li.amount != null)
                li.amount.setText(ci.amount);
            if (li.descr != null)
                li.descr.setText(ci.descr);
            if (li.operator != null)
                li.operator.setText(ci.operator);
        } catch(Exception e) {
            println(e.getMessage().toString());
        }
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
