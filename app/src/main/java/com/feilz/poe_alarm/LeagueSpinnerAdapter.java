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

import java.util.ArrayList;

public class LeagueSpinnerAdapter extends ArrayAdapter<String> {


    LeagueSpinnerAdapter(@NonNull Context context, int resourceId,int textViewId, ArrayList<String> resource) {
        super(context,resourceId,textViewId, resource);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        String item = getItem(position);
        if (convertView==null){
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.spinneritemview2,parent,false);
        }
        TextView txt = convertView.findViewById(R.id.spinnerItem);
        txt.setText(item);
        return convertView;
    }
}
