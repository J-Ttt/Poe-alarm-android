package com.feilz.poe_alarm;

import android.app.Activity;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;


class Linegraph  {
    //TODO
    private GraphView graph;
    private String defaultCurrency;
    private int color;
    private Activity callingActivity;
    Linegraph(GraphView graph, String currency, int appTextColor, Activity activity){
        this.graph = graph;
        this.defaultCurrency = currency;
        this.color = appTextColor;
        this.callingActivity = activity;
        graph.setTitleColor(appTextColor);
        graph.setTitleTextSize(40);

        update(currency);
    }

    void update(String currency){
        updateTitle(currency);
    }
    void update(Map<Date,Double> map){
        drawGraph(map);
    }

    private void updateTitle(String currency){
        graph.setTitle(currency);
    }

    private void drawGraph(Map<Date,Double> map){
        graph.removeAllSeries();
        LineGraphSeries<DataPoint> ser = new LineGraphSeries<>(data(map));
        setSeriesSettings(ser);
        graph.getViewport().setMaxX(Timestamp.valueOf("2017-11-10 00:00:00").getTime());
        graph.getViewport().setMinX(Timestamp.valueOf("2017-11-09 00:00:00").getTime());
        graph.addSeries(ser);
    }

    public DataPoint[] data(Map<Date,Double> map) {
        DataPoint[] values = new DataPoint[map.size()];
        SortedSet<Date> days = new TreeSet<>(map.keySet());
        int i = 0;
        for (Date m: days) {
            Date dd = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("MM.dd", Locale.getDefault());
            NumberFormat form = new DecimalFormat("#0.00");
            Double nn=0.0;
            try{
                dd = sdf.parse(sdf.format(m));
                nn=(double)form.parse(form.format(map.get(m)));
            } catch (Exception e){
                e.printStackTrace();
            }

            DataPoint d = new DataPoint(m,nn);
            values[i]=(d);
            i+=1;
        }
         return values;
    }


    private void setSeriesSettings(LineGraphSeries<DataPoint> ser){
        ser.setColor(color);
        ser.setThickness(2);
        ser.setDrawDataPoints(true);
        ser.setDataPointsRadius(6);
        ser.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {
                Toast.makeText(callingActivity,"max: " + Double.toString(series.getHighestValueY()) + " min: "+ Double.toString(series.getLowestValueY()),Toast.LENGTH_SHORT).show();
            }
        });
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
}
