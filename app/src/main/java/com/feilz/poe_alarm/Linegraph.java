package com.feilz.poe_alarm;

import android.app.Activity;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import java.util.ArrayList;

import java.util.List;



class Linegraph  {
    //TODO
    private LineChart graph;
    private String defaultCurrency;
    private int color;
    private Activity callingActivity;
    private TextView minVal,maxVal;

    Linegraph(LineChart graph, String currency, int appTextColor, Activity activity, TextView minVal, TextView maxVal){
        this.graph = graph;
        this.defaultCurrency = currency;
        this.color = appTextColor;
        this.callingActivity = activity;
        this.minVal=minVal;
        this.maxVal=maxVal;
        drawGraph();

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
    void update(){
        graph.clear();
        drawGraph();

    }

    /*private void updateTitle(String currency){
        graph.setTitle(currency);
    }*/

    private void drawGraph(){
        LineData data = new LineData();
        List<Entry> values = new ArrayList<>();
        for (int i=0;i<72;i++){
            values.add(RandomData(i));
        }
        LineDataSet ldata = new LineDataSet(values,"randomData");
        setSeriesSettings(ldata);
        data.addDataSet(ldata);

        graph.setData(data);
        setGraphSettings();
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
        setNewMaxMin(ser.getYMax(),ser.getYMin());

    }
    private void moveLegend(){
        graph.getLegend().setEnabled(false);
    }
    private void customizeXAxis(){
        XAxis x = graph.getXAxis();
        x.setPosition(XAxis.XAxisPosition.BOTTOM);
        x.setTextSize(10f);
    }
    private void setNewMaxMin(Float max, Float min){
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
    private Entry RandomData(float x) {
          return new Entry(x,(float)(Math.random() * 5 + 8));
    }
}
