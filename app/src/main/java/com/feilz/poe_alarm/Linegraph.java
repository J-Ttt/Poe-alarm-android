package com.feilz.poe_alarm;

import android.app.Activity;
import android.util.Log;
import android.util.SparseArray;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Timestamp;
import java.util.ArrayList;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;


class Linegraph  {
    //TODO
    private LineChart graph;
    private String currCurrency;
    private int color;
    private Activity callingActivity;
    private TextView minVal,maxVal, average;
    private DatabaseReference database;
    private SparseArray<String> numMap;

    Linegraph(LineChart graph, int appTextColor, Activity activity, TextView minVal, TextView maxVal,TextView average){
        this.graph = graph;
        this.color = appTextColor;
        this.callingActivity = activity;
        this.minVal=minVal;
        this.maxVal=maxVal;
        this.average=average;
        database = FirebaseDatabase.getInstance().getReference();


        numMap = new SparseArray<>();
        makeNumMap();

        /*graph.setTitleColor(appTextColor);
        graph.setTitleTextSize(50);

        graph.getViewport().setScalable(true);
        graph.getViewport().setScrollable(true);
        graph.setFitsSystemWindows(true);*/
        //update(currency);
    }

    /*void update(String currency){
        updateTitle(currency);
    }*/
    void update(String league,String currency,String period){
        graph.clear();
        drawGraph(league,currency,period);
    }

    /*private void updateTitle(String currency){
        graph.setTitle(currency);
    }*/

    private void drawGraph(String league, final String currency, String period){
        currCurrency = currency;
        database.child(league).child(currency).child(period).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                LineData data = new LineData();
                List<Entry> values = new ArrayList<>();

                Float highest=0f,lowest=0f,averages=0f;

                Iterable<DataSnapshot> db =  dataSnapshot.getChildren();

                for (DataSnapshot i : db){

                    try {
                        //Log.i("test",currentHour.toString());
                        //Log.i("datatest",i.getKey());
                        values.add(new Entry(71-Float.valueOf(i.getKey()),Float.valueOf(i.child("0").getValue().toString())));
                        /*currentHour-=1;
                        if (currentHour<0){
                            currentHour+=24;
                        }*/
                        //Log.i("datatest2",i.child("0").getValue().toString());
                    } catch (Exception e) {
                       // e.printStackTrace();
                    }
                    if (i.getKey().equals("Highest")){
                        highest = Float.valueOf(dataSnapshot.child("Highest").getValue().toString());
                    } else if (i.getKey().equals("Lowest")) {
                        lowest = Float.valueOf(dataSnapshot.child("Lowest").getValue().toString());
                    } else if (i.getKey().equals("avrg")){
                        averages = Float.valueOf(dataSnapshot.child("avrg").getValue().toString());
                    }

                }
                //Log.i("datatest",values.toString());
                for (int i=0,j=values.size()-1;i<j;i++){
                    values.add(i,values.remove(j));
                }
                //Collections.reverse(values);
                //Log.i("datatest",values.toString());
                setNewMaxMin(highest,lowest,averages);
                LineDataSet ldata = new LineDataSet(values,currency);
                setSeriesSettings(ldata);
                data.addDataSet(ldata);

                graph.setData(data);
                setGraphSettings();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        /*
        LineDataSet ldata = new LineDataSet(values,"randomData");
        setSeriesSettings(ldata);
        data.addDataSet(ldata);

        graph.setData(data);
        setGraphSettings();*/
        /*
        graph.removeAllSeries();
        LineGraphSeries<DataPoint> ser = new LineGraphSeries<>(data(map));
        setSeriesSettings(ser);
        //graph.getViewport().setMaxX(Timestamp.valueOf("2017-11-10 00:00:00").getTime());
        //graph.getViewport().setMinX(Timestamp.valueOf("2017-10-09 00:00:00").getTime());
        graph.addSeries(ser);*/
    }
    /*
    public LineDataSet RandomData() {
        List<Entry> values = new ArrayList<>();
        SortedSet<Date> days = new TreeSet<>(map.keySet());
        int i = 0;
        for (Date m: days) {
            Date dd = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("HH", Locale.getDefault());
            NumberFormat form = new DecimalFormat("#0.00");
            Double nn=0.0;
            try{
                dd = sdf.parse(sdf.format(m.getTime()));
                nn=form.parse(form.format(map.get(m)));
            } catch (Exception e){
                e.printStackTrace();
            }
            Entry d = new Entry(dd,nn);
            values[i]=d;
            i+=1;
        }
         return values;
    }*/
    //For customizing the viewport
    private void setGraphSettings(){
        customizeXAxis();
        graph.setVisibleXRangeMaximum(24);
        graph.setVisibleXRangeMinimum(10);
        graph.fitScreen();
        graph.resetTracking();
        graph.resetViewPortOffsets();
        graph.setTouchEnabled(true);
        graph.setScaleEnabled(true);
        graph.setDragEnabled(true);
        graph.setScrollBarFadeDuration(1);
        graph.zoom(3,0,0,0);
        graph.moveViewToX(48);
        Description desc = new Description();
        desc.setText(currCurrency + "s in "+ ((currCurrency.equals("Chaos Orb"))?"Exalted Orbs":"Chaos Orbs"));
        graph.setDescription(desc);
        moveLegend();
    }
    //Customizing the line
    private void setSeriesSettings(LineDataSet ser){
        ser.setColor(color);
        ser.setCircleColor(color);
        ser.setCircleColorHole(color);

        ser.setLineWidth(1f);
        ser.setCircleRadius(2);


    }

    private Integer getCurrentHour(){
        Calendar date = new GregorianCalendar();
        return date.get(Calendar.HOUR_OF_DAY);
    }


    private void moveLegend(){
        graph.getLegend().setEnabled(false);
    }
    private void customizeXAxis(){
        XAxis x = graph.getXAxis();
        //x.setPosition(XAxis.XAxisPosition.BOTTOM);
        x.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return numMap.get((int)value);
            }
        });

        x.setTextSize(10f);
    }
    private void setNewMaxMin(Float max, Float min,Float avrg){
        /*NumberFormat form = new DecimalFormat("#0.0");
        try {
            max=(Float)form.parse(form.format(max));
            min=(Float)form.parse(form.format(min));
        }catch (Exception e){
            e.printStackTrace();
        }
        */

        String newMax = String.format("%.4f", max);
        maxVal.setText(newMax);
        String newMin = String.format("%.4f", min);
        minVal.setText(newMin);
        String newAvrg = String.format("%.4f", avrg);
        average.setText(newAvrg);
    }
        //Reasoning behind this is found at the bottom of mainactivity.
      /*  void setGraphSize(int newWidth,int newHeight) {
       // Log.d("getRight",Integer.toString(callingActivity.getWindow().getDecorView().getRight()));
       // Log.d("getHeight",Integer.toString(callingActivity.getWindow().getDecorView().getHeight()));

        ViewGroup.LayoutParams params = graph.getLayoutParams();
        //Log.d("curW",Integer.toString(params.width));
        //Log.d("curH",Integer.toString(params.height));
        params.width=newWidth;
        params.height=newHeight;
        //Log.d("curW",Integer.toString(params.width));
       // Log.d("curH",Integer.toString(params.height));
        graph.setLayoutParams(params);

    }*/
    /*
    //TESTING PURPOSES DURING DEVELOPMENT
    private Entry RandomData(float x) {
          return new Entry(x,(float)(Math.random() * 5 + 8));
    }
    */
    private void makeNumMap(){
        int now = getCurrentHour();
        boolean am=true;
        if (now>11){
            now-=12;
            am=false;
        }
        for (int i=0;i<72;i++){
            String startTime = ""+now+(am?"am":"pm");
            numMap.put(i,startTime);
            now+=1;
            if (now>12){
                now-=12;
                am=!am;
            }
        }
    }


}
