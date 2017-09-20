package com.example.victor.latrans.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.victor.latrans.R;

import java.util.List;

/**
 * Created by Victor on 9/18/2017.
 */

public class CustomSpinnerAdapter extends BaseAdapter implements android.widget.SpinnerAdapter {

    private final Context mContext;
    private List<String> mStrings;
    public CustomSpinnerAdapter(Context context, List<String> data) {
        this.mContext = context;
        this.mStrings =  data;
    }




    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
       // return super.getDropDownView(position, convertView, parent);
        TextView txt = new TextView(mContext);
        txt.setPadding(16, 16, 16, 16);
        txt.setTextSize(18);
        txt.setText(mStrings.get(position));
        txt.setTextColor(Color.parseColor("#000000"));
        return  txt;
    }

        @Override
        public View getView(int i, View view, ViewGroup viewgroup) {
        TextView txt = new TextView(mContext);
        txt.setGravity(Gravity.CENTER);
        txt.setPadding(16, 16, 16, 16);
        txt.setTextSize(16);
        txt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_down_arrow, 0);
        txt.setText(mStrings.get(i));
        txt.setTextColor(Color.parseColor("#000000"));
        return  txt;
    }

    @Override
    public int getCount() {
        return mStrings.size();
    }

    @Override
    public Object getItem(int i) {
        return mStrings.get(i);
    }

    @Override
    public long getItemId(int i) {
        return (long)i;
    }

}
