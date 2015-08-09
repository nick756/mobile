package com.nova.sme.sme01.miscellanea;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nova.sme.sme01.CommonClass;
import com.nova.sme.sme01.R;
import com.nova.sme.sme01.TransactionActivity;
import java.util.ArrayList;
import java.util.Vector;

import static java.sql.DriverManager.println;


public class CustomAdapter extends ArrayAdapter<String> {
    private float            new_height;
    private Activity         activity;
    private RelativeLayout   base_layout;
    private ArrayList        data;
    private LayoutInflater   inflater;
    private SpinnerModel     spinner_model = null;

 //   private Vector<TextView> texts = new Vector<TextView>();
    private String           maxName = "";
    private float            fontSize;
    private ArrayList<SpinnerModel> spinner_array;
    private TextResizing            textFit;
    private float                   textsize = 0;
    private TextView                standard = null;

    public CustomAdapter(Activity activity, RelativeLayout base_layout, int textViewResourceId, ArrayList objects, float factor)
    {
        super(activity, textViewResourceId, objects);

        this.activity    = activity;
        this.base_layout = base_layout;
        this.data        = objects;
        this.textFit     = new TextResizing(activity);

        this.inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        int    viewTop      = activity.getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();
        float  total_height = (float)(base_layout.getHeight() - viewTop);
        this.new_height     = total_height * factor;


        spinner_array = (ArrayList<SpinnerModel>) objects;
        SpinnerModel spinnner_model;
        for (int j = 0; j < spinner_array.size(); j ++) {
            spinnner_model = spinner_array.get(j);
            if (spinnner_model.getOperationName().length() > maxName.length())
                maxName = spinnner_model.getOperationName();
        }

        try {
            View v = this.getView(0, null, null);
            LinearLayout ll;
            ll       = (LinearLayout) v;
            ll       = (LinearLayout) ll.getChildAt(0);
            standard = (TextView) ll.getChildAt(1);
        } catch(Exception e) {

        }
        if (standard != null)
            new FitTextSize(base_layout);

     }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        String       className = "", str = "";
        LinearLayout ll;
        View         view;
        TextView     tv;
        int          width;
        if (convertView != null) {
            className = convertView.getClass().getSimpleName();
            if (className.indexOf("LinearLayout") != -1) {
                ll = (LinearLayout) convertView;
                ll = (LinearLayout)ll.getChildAt(0);
                for (int j = 0; j < ll.getChildCount(); j ++) {
                    view = ll.getChildAt(j);
                    className = view.getClass().getSimpleName();
                    if (className.indexOf("TextView") != -1) {
                        tv = (TextView) view;
                        if (textsize > 0) {
//                            width = tv.getWidth();
//                            tv.setTextSize(textsize);
                        }
                    }
                }

            }
        }
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {
        String error = "";
        View row      = this.inflater.inflate(R.layout.operation_item, parent, false);
        spinner_model = (SpinnerModel) data.get(position);

        TextView name        = (TextView) row.findViewById(R.id.name_opeartion);
        ImageView is_checked = (ImageView)row.findViewById(R.id.in_out_bound);

        name.setText(spinner_model.getOperationName());
        is_checked.setImageResource(spinner_model.getImageId());

        try {
            LinearLayout           layout = (LinearLayout) name.getParent();
            ViewGroup.LayoutParams params = layout.getLayoutParams();

            params.height = (int)this.new_height;
        } catch(Exception e) {
            error = e.getMessage().toString();
            println(error);
        }

        return row;
    }


    public void findFontSize() {
        textsize = textFit.getSizeWidth(standard, this.maxName, 1.4f, this.base_layout.getWidth());
        standard.setTextSize(textsize);
    }

    public class FitTextSize {
        private RelativeLayout tv;

        public FitTextSize(RelativeLayout tv) {
            this.tv =  tv;

            tv.post(new Runnable() {
                public void run() {
                    findFontSize();
                }
            });
        }
    }

}
