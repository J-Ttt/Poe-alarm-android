package com.feilz.poe_alarm;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by dakerlun on 20/11/2017.
 */

public class TrackedCurrencyAdapter extends ArrayAdapter<TrackedCurrency> {
    Database db;

    TrackedCurrencyAdapter(Context c, ArrayList<TrackedCurrency> trackedCurrencies) {
        super(c,0,trackedCurrencies);
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent){
        final TrackedCurrency trackedCurrency = getItem(pos);
        if (convertView==null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listviewitem,parent,false);
        }
        TextView currency = convertView.findViewById(R.id.currency);
        TextView lessThan = convertView.findViewById(R.id.lessorgreater);
        TextView value = convertView.findViewById(R.id.textviewvalue);
        TextView league = convertView.findViewById(R.id.league);
        FloatingActionButton delButton = convertView.findViewById(R.id.deleteButton);
        db = new Database(getContext());
        currency.setText(trackedCurrency.currency);
        String leagueText = "Server: " + trackedCurrency.league;
        league.setText(leagueText);
        lessThan.setText(trackedCurrency.lessThan ? "<=":">=");
        String va = " "+trackedCurrency.value;
        delButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.removeAlarm(trackedCurrency.currency,trackedCurrency.league);
                remove(trackedCurrency);
            }
        });
        value.setText(va);

        return convertView;
    }
}
