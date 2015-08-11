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


import com.nova.sme.sme01.R;
import java.util.ArrayList;


import static java.sql.DriverManager.println;


public class CustomAdapter extends ArrayAdapter<String> {
    private float            new_height;
    private Activity         activity;
    private RelativeLayout   base_layout;
    private ArrayList        data;
    private LayoutInflater   inflater;
    private SpinnerModel     spinner_model = null;

    private String                  maxName  = "Telephone, Fax and Internet";
    private String                  maxLimit = "Purchase of Plants and Machineries";
                                              //Maintenance of Office and Equipment
    private ArrayList<SpinnerModel> spinner_array;
    private TextResizing            textFit;
    private float                   textsize = 0;
    private TextView                standard = null;
    private int                     counter  = 0;
    private String                  total = "";

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


        SpinnerModel spinnner_model;
        spinner_array  = (ArrayList<SpinnerModel>) objects;
        int max_length = maxLimit.length();
        int length;

        for (int j = 0; j < spinner_array.size(); j ++) {
            spinnner_model = spinner_array.get(j);
            length         = spinnner_model.getOperationName().length();
            if (length > maxName.length() && length <= max_length)
                maxName = spinnner_model.getOperationName();
        }


         float textSize;

        try {
            LinearLayout   ll;
            RelativeLayout rl;
            View v   = this.getView(0, null, null);
            ll       = (LinearLayout) v;
            rl       = (RelativeLayout) ll.getChildAt(0);
            standard = (TextView) rl.getChildAt(1);
        } catch(Exception e) {

        }
//        if (standard != null)
//            findFontSize();
     }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
/*
          if (convertView != null) {
            if (counter < spinner_array.size()) {
                String         className = "", str;
                LinearLayout   ll;
                RelativeLayout rl;
                View           view;
                TextView       tv;

                int            height;
                try {
                    className = convertView.getClass().getSimpleName();
                    if (className.indexOf("LinearLayout") != -1) {
                        ll = (LinearLayout) convertView;
                        rl = (RelativeLayout) ll.getChildAt(0);
                        for (int j = 0; j < rl.getChildCount(); j++) {
                            view = rl.getChildAt(j);
                            className = view.getClass().getSimpleName();
                            if (className.indexOf("TextView") != -1) {
                                tv = (TextView) view;
                                if (textsize > 0) {
                                    tv.setTextSize(textsize);
                                    counter++;
                                }
                            }
                        }
                    }
                } catch(Exception e) {

                }
            }
        }*/
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {
        String error  = "";
        View row      = this.inflater.inflate(R.layout.operation_item_n, parent, false);
        spinner_model = (SpinnerModel) data.get(position);

        TextView name        = (TextView) row.findViewById(R.id.name_opeartion);
        ImageView is_checked = (ImageView)row.findViewById(R.id.in_out_bound);

        name.setText(spinner_model.getOperationName());
        is_checked.setImageResource(spinner_model.getImageId());

        try {
            RelativeLayout     layout = (RelativeLayout) name.getParent();
            ViewGroup.LayoutParams params = layout.getLayoutParams();

            params.height = (int)this.new_height;
        } catch(Exception e) {
            error = e.getMessage().toString();
            println(error);
        }

        return row;
    }

    private int getHeight(TextView tv) {
        int height = tv.getHeight();

        if (height == 0) {
            tv.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            height = tv.getMeasuredHeight();
        }

        return height;
    }
    private int getWidth(TextView tv) {
        int width = tv.getWidth();

        if (width == 0) {
            tv.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            width = tv.getMeasuredWidth();
        }

        return width;
    }

    public void findFontSize() {
        this.textsize = textFit.getSizeWidth(standard, this.maxName, 1.4f, this.base_layout.getWidth());
        this.standard.setTextSize(textsize);
    }

    public class FitSize {
        private RelativeLayout rl;

        public FitSize(RelativeLayout rl) {
            this.rl =  rl;

            rl.post(new Runnable() {
                public void run() {
                    //        setFontSize();
                }
            });
        }
    }

}
