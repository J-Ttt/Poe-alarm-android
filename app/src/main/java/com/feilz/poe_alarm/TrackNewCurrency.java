package com.feilz.poe_alarm;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by dakerlun on 20/11/2017.
 */

public class TrackNewCurrency extends Activity {

    TextView showval,valueText;
    Spinner selectedCurrency, ltorgt,leagueSpinner;
    Button accept;
    SeekBar seekbar;
    double step = 0.1;
    MainActivity m;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newcurrencytotrack);
        Bundle extras = getIntent().getExtras();
        selectedCurrency=findViewById(R.id.rightDrawerSpinner);
        final String selCurr = extras.getString("selectedCurrency",getString(R.string.chaos_orb));
        final int max = extras.getInt("seekBarMaxVal",100);
        final int min = extras.getInt("seekBarMinVal",0);
        final int currStatus = extras.getInt("currVal",0);
        selectedCurrency.setSelection(selectInitialCurrency(selCurr));
        ltorgt=findViewById(R.id.spinner);
        //leagueSpinner = findViewById(R.id.leagueSpinner);
       // leagueSpinner.setAdapter(m.spinnerAdapter); //CREATE SPINNERADAPTER

        seekbar=findViewById(R.id.seekBar);
        int maxSeekBar = (int)((max-min)/step)+1;
        seekbar.setMax(maxSeekBar);
        int progressSeekBar = (int)((currStatus-min)/step);
        seekbar.setProgress(progressSeekBar);

        //Log.d("maxVal",String.valueOf(maxSeekBar));
        //Log.d("progressVal",String.valueOf(progressSeekBar));
        valueText=findViewById(R.id.textView4);
        valueText.setText(selCurr.equals(getString(R.string.chaos_orb)) ? getString(
                R.string.valInExaltedOrbs) : getString(R.string.valInChaosOrbs));
        showval=findViewById(R.id.textView3);
        String newVal = " " + (min + (progressSeekBar*step));
        showval.setText(newVal);
        this.setFinishOnTouchOutside(true);

        accept = findViewById(R.id.button2);
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String league = String.valueOf(leagueSpinner.getSelectedItem());
                String curr = String.valueOf(selectedCurrency.getSelectedItem());
                boolean type = String.valueOf(ltorgt.getSelectedItem()).equals("<=");

                m.addToAlarmList(new TrackedCurrency(curr,type,Double.valueOf(showval.getText().toString()),league));
                Toast.makeText(m.getApplicationContext(),"Added tracker for " + curr + " "+ type + showval.getText(),Toast.LENGTH_SHORT).show();
                //TODO: write to database method
                finish();
            }
        });
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                String newVal = " " + (min + (i*step));
                showval.setText(newVal);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private int selectInitialCurrency(String selCurr){
        int index=0;
        for (int i=0;i<selectedCurrency.getCount();i++){
            if (selectedCurrency.getItemAtPosition(i).toString().equalsIgnoreCase(selCurr)){
                index=i;
                break;
            }
        }
        return index;
    }
}
