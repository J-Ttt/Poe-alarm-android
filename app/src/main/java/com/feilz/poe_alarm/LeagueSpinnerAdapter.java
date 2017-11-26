package com.feilz.poe_alarm;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by dakerlun on 26/11/2017.
 */

public class LeagueSpinnerAdapter extends ArrayAdapter<String> {


    public LeagueSpinnerAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        String item = getItem(position);
        if (convertView==null){
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.spinneritemview,parent,false);
        }
        TextView txt = convertView.findViewById(R.id.spinnerItem);
        txt.setText(item);
        return convertView;
    }
}
