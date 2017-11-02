package com.feilz.poe_alarm;

import android.view.View;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;


public class Linegraph  {

    //TODO
    public void onCreate(View v) {
        GraphView graph = (GraphView)v.findViewById(R.id.graph);
        update(graph);
    }

    public void update(GraphView graph){

    }

   public void initGraph(GraphView graph) {
        LineGraphSeries<DataPoint> ser = new LineGraphSeries<>(new DataPoint[]{
                new DataPoint(0,1),
                new DataPoint(2,6),
                new DataPoint(3,2),
                new DataPoint(1,9),
                new DataPoint(2,4),
        });
        graph.addSeries(ser);
    }
}
