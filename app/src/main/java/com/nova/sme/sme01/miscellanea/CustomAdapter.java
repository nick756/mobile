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
import com.nova.sme.sme01.TransactionActivity;
import java.util.ArrayList;


public class CustomAdapter extends ArrayAdapter<String> {
    private float           new_height;
    private Activity        activity;
    private RelativeLayout  base_layout;
    private ArrayList       data;
    private LayoutInflater  inflater;
    private SpinnerModel    spinner_model = null;

    public CustomAdapter(Activity activity, RelativeLayout base_layout, int textViewResourceId, ArrayList objects, float factor)
    {
        super(activity, textViewResourceId, objects);

        this.activity    = activity;
        this.base_layout = base_layout;
        this.data        = objects;

        this.inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        int    viewTop      = activity.getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();
        float  total_height = (float)(base_layout.getHeight() - viewTop);
        this.new_height     = total_height * factor;

    }

    @Override
    public View getDropDownView(int position, View convertView,ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {
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
        } catch(Exception err) {

        }


        return row;
    }
}
