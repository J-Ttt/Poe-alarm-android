package com.feilz.poe_alarm;

import android.app.Activity;
import android.database.Cursor;
import android.support.constraint.ConstraintLayout;

import android.util.Log;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;


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


        drawGraph(currency);
    }

    void update(String currency){
        drawGraph(currency);
    }


    private void drawGraph(String currency){
        graph.setTitle(currency);
        graph.removeAllSeries();
        if (currency.equals(defaultCurrency)){
            LineGraphSeries<DataPoint> ser = new LineGraphSeries<>(new DataPoint[]{
                    new DataPoint(0,1),
                    new DataPoint(1,2),
                    new DataPoint(2,4),
                    new DataPoint(3,5),
                    new DataPoint(4,4),
            });
            setSeriesSettings(ser);
            graph.addSeries(ser);
        } else {
            LineGraphSeries<DataPoint> ser = new LineGraphSeries<>(new DataPoint[]{
                    new DataPoint(0,1),
                    new DataPoint(1,6),
                    new DataPoint(2,12),
                    new DataPoint(3,17),
                    new DataPoint(4,26),
            });
            setSeriesSettings(ser);
            graph.addSeries(ser);
        }
    }
    private void setSeriesSettings(LineGraphSeries<DataPoint> ser){
        ser.setColor(color);
        ser.setThickness(2);
        ser.setDrawDataPoints(true);
        ser.setDataPointsRadius(6);
        ser.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {
                Toast.makeText(callingActivity,graph.getTitle(),Toast.LENGTH_SHORT).show();
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
