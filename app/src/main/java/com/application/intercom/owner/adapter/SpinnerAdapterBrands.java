package com.application.intercom.owner.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.application.intercom.R;

import java.util.List;

public class SpinnerAdapterBrands extends ArrayAdapter<String> {

    private Context context;
    private List<String> values;

    public SpinnerAdapterBrands(Activity context, int textViewResourceId, List<String> values) {
        super(context, textViewResourceId, values);
        this.context = context;
        this.values =  values;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return super.getView(position, convertView, parent);
    }

    // And here is when the "chooser" is popped up
    // Normally is the same view, but you can customize it if you want
    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        LayoutInflater mInflater = LayoutInflater.from(context);
        convertView = mInflater.inflate(R.layout.spinner_dropdown_item, parent ,false);

        TextView label = convertView.findViewById(R.id.text1);
        label.setText(values.get(position));

        return convertView;
    }

}