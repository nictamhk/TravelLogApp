package com.example.travellog;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.graphics.Color;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StatsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StatsFragment extends Fragment {

    private static String TAG = "MainActivity";

    private float[] yData = {25.3f, 10.6f, 66.76f, 44.32f, 46.01f, 16.89f, 23.9f};
    private String[] xData = {"Restaurant", "Transport" , "Museum" , "ShoppingMall", "Hotel", "Park", "Cinema"};
    PieChart pieChart;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_stats, container, false);

        Log.d(TAG, "onCreate: starting to create chart");

        pieChart = view.findViewById(R.id.idPieChart);

        pieChart.setRotationEnabled(true);
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setText("");
        //pieChart.setHoleColor(Color.BLUE);
        //pieChart.setCenterTextColor(Color.BLACK);
        pieChart.setHoleRadius(50f);
        pieChart.setTransparentCircleAlpha(0);
        pieChart.setCenterText("Where did I stay?");
        pieChart.setCenterTextSize(13);
        pieChart.setEntryLabelColor(Color.rgb(105,48,195));
        //pieChart.setDrawEntryLabels(true);
        //pieChart.setEntryLabelTextSize(20);
        //More options just check out the documentation!

        addDataSet();

        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                Log.d(TAG, "onValueSelected: Value select from chart.");
                Log.d(TAG, "onValueSelected: " + e.toString());
                Log.d(TAG, "onValueSelected: " + h.toString());

                final int x = (int) e.getX();
                String staytype = xData[x];

//                int pos1 = e.toString().indexOf("(sum): ");
//                String sales = e.toString().substring(pos1 + 7);
//
//                for(int i = 0; i < yData.length; i++){
//                    System.out.println(sales);
//                    if(yData[i] == Float.parseFloat(sales)){
//                        pos1 = i;
//                        break;
//                    }
//                }
//                String staytype = xData[pos1 + 1];
            }

            @Override
            public void onNothingSelected() {

            }
        });

        return view;
    }

    private void addDataSet() {
        Log.d(TAG, "addDataSet started");
        ArrayList<PieEntry> yEntrys = new ArrayList<>();
        ArrayList<String> xEntrys = new ArrayList<>();

        for(int i = 0; i < yData.length; i++){
            yEntrys.add(new PieEntry(yData[i] , xData[i]));
        }

        for(int i = 1; i < xData.length; i++){
            xEntrys.add(xData[i]);
        }

        //create the data set
        PieDataSet pieDataSet = new PieDataSet(yEntrys, "StayTime");
        pieDataSet.setSliceSpace(2);
        pieDataSet.setValueTextSize(12);

        //add colors to dataset
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.rgb(255, 173, 173));
        colors.add(Color.rgb(255, 214, 165));
        colors.add(Color.rgb(253,255,182));
        colors.add(Color.rgb(202,255,191));
        colors.add(Color.rgb(155,246,255));
        colors.add(Color.rgb(189,178,255));
        colors.add(Color.rgb(255,198,255));

        pieDataSet.setColors(colors);

        //        //add legend to chart
        //        Legend legend = pieChart.getLegend();
        //        legend.setForm(Legend.LegendForm.CIRCLE);
        //        legend.setPosition(Legend.LegendPosition.LEFT_OF_CHART);

        //create pie data object
        PieData pieData = new PieData(pieDataSet);
        pieData.setValueFormatter(new PercentFormatter(pieChart));
        pieChart.setData(pieData);
        pieChart.invalidate();
    }
}