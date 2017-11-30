package com.feilz.poe_alarm;

import android.app.Activity;
import android.util.Log;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import java.util.Iterator;
import java.util.List;



class Linegraph  {
    //TODO
    private LineChart graph;
    private String defaultCurrency;
    private int color;
    private Activity callingActivity;
    private TextView minVal,maxVal, average;
    private DatabaseReference database;

    Linegraph(LineChart graph, int appTextColor, Activity activity, TextView minVal, TextView maxVal,TextView average){
        this.graph = graph;
        this.color = appTextColor;
        this.callingActivity = activity;
        this.minVal=minVal;
        this.maxVal=maxVal;
        this.average=average;
        database = FirebaseDatabase.getInstance().getReference();


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
        LineData data = new LineData();
        List<Entry> values = new ArrayList<>();

        database.child(league).child(currency).child(period).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                LineData data = new LineData();
                List<Entry> values = new ArrayList<>();
                Float highest = Float.valueOf(dataSnapshot.child("Highest").getValue().toString());
                Float lowest = Float.valueOf(dataSnapshot.child("Lowest").getValue().toString());
                Float averages = Float.valueOf(dataSnapshot.child("avrg").getValue().toString());
                Log.i("datatest",highest.toString());
                Log.i("datatest2",lowest.toString());

                Log.i("datatest2",averages.toString());

                setNewMaxMin(highest,lowest,averages);
                Iterable<DataSnapshot> db =  dataSnapshot.getChildren();

                for (DataSnapshot i : db){
                    Log.i("datatest",i.getKey());
                    String s = i.getKey()+"/0";
                    Log.i("datatest3",s);

                    Log.i("datatest2",i.getValue().toString());
                    /*try {
                        values.add(new Entry(Float.valueOf(i.getKey()),Float.valueOf(i.getValue().toString())));
                    } catch (Exception e) {

                    }*/


                }
                LineDataSet ldata = new LineDataSet(values,currency);
                setSeriesSettings(ldata);
                data.addDataSet(ldata);
                graph.setData(data);
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
        graph.setVisibleXRangeMaximum(72);
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
        customizeXAxis();
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
    private void moveLegend(){
        graph.getLegend().setEnabled(false);
    }
    private void customizeXAxis(){
        XAxis x = graph.getXAxis();
        x.setPosition(XAxis.XAxisPosition.BOTTOM);
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
        String newMax = "Max: " + max;
        maxVal.setText(newMax);
        String newMin = "Min: "+ min;
        minVal.setText(newMin);
        String newAvrg = "Average: " + avrg;
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
}
