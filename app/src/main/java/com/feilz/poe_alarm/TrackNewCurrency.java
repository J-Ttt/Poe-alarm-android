package com.feilz.poe_alarm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by dakerlun on 20/11/2017.
 */

public class TrackNewCurrency extends Activity {

    TextView showval,valueText;
    Spinner selectedCurrency, ltorgt,leagueSpinner;
    Button accept;
    SeekBar seekbar;
    double step = 0.1;
    int scale = 1;
    MyApp mApp;


    @Override
    public void onCreate(Bundle savedInstanceState){
        mApp = (MyApp)getApplicationContext();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newcurrencytotrack);
        Bundle extras = getIntent().getExtras();
        selectedCurrency=findViewById(R.id.rightDrawerSpinner);
        final String selCurr = extras.getString("selectedCurrency",getString(R.string.chaos_orb));
        double max = extras.getDouble("seekBarMaxVal",100.0);
        double min = extras.getDouble("seekBarMinVal",0.0);
        double currStatus = extras.getDouble("currVal",10.0);
        selectedCurrency.setSelection(selectInitialCurrency(selCurr));
        ltorgt=findViewById(R.id.spinner);
        leagueSpinner = findViewById(R.id.leagueSpinner);
        leagueSpinner.setAdapter(mApp.getLeagueChoice());
        if (max<1){
            max=max*100;
            min=min*100;
            currStatus=currStatus*100;
            scale=100;
            step=1.0;
        }else if (max<10) {
            max = max * 10;
            min = min * 10;
            currStatus = currStatus*10;
            scale = 10;
            step=0.5;
        }
        final int newMin=(int)min;
        final int newMax=(int)max;
        final int newCurrStatus=(int)currStatus;
        seekbar=findViewById(R.id.seekBar);
        int maxSeekBar = (int)((newMax-newMin)/step)+1;
        seekbar.setMax(maxSeekBar);
        int progressSeekBar = (int)((newCurrStatus-newMin)/step);
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
                Intent data = new Intent();
                data.putExtra("currency",curr);
                data.putExtra("league",league);
                data.putExtra("lessThan",type);
                data.putExtra("value",showval.getText().toString());
                setResult(RESULT_OK,data);
               //TODO: write to database method
                finish();
            }
        });
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                String newVal = " " + ((newMin + (i*step))/scale);
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
    /*
    private void setSeekbarMaxMinVal(){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child(String.valueOf(leagueSpinner.getSelectedItem())).
                child(String.valueOf(selectedCurrency.getSelectedItem())).child("Day").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Float max = Float.valueOf(dataSnapshot.child("Highest").getValue().toString());
                Float min = Float.valueOf(dataSnapshot.child("Lowest").getValue().toString());
                max = max + (max * 0.2f);
                min = min - (min * 0.2f);
                if (max<100){

                } else if (max<10){

                } else if (max<1){

                }
                //seekbar.setMax();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }*/
}
